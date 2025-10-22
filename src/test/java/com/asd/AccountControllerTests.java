package com.asd;

import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.model.User;
import com.asd.repository.AccountRepository;
import com.asd.repository.CustomerRepository;
import com.asd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private MockHttpSession adminSession;
    private MockHttpSession viewerSession;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Clean up
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();

        // Create admin user
        String adminEmail = "admin_" + System.currentTimeMillis() + "@bank.local";
        User admin = new User("Admin User", adminEmail, encoder.encode("password123"), User.Role.ADMIN);
        admin = userRepository.save(admin);

        // Create viewer user
        String viewerEmail = "viewer_" + System.currentTimeMillis() + "@bank.local";
        User viewer = new User("Viewer User", viewerEmail, encoder.encode("password123"), User.Role.READ_ONLY);
        viewer = userRepository.save(viewer);

        // Create test customer
        testCustomer = new Customer();
        testCustomer.setName("John Smith");
        testCustomer.setEmail("john.smith@example.com");
        testCustomer = customerRepository.save(testCustomer);

        // Setup admin session
        adminSession = new MockHttpSession();
        adminSession.setAttribute("userId", admin.getId());
        adminSession.setAttribute("userName", admin.getFullName());
        adminSession.setAttribute("userRole", admin.getRole().name());

        // Setup viewer session
        viewerSession = new MockHttpSession();
        viewerSession.setAttribute("userId", viewer.getId());
        viewerSession.setAttribute("userName", viewer.getFullName());
        viewerSession.setAttribute("userRole", viewer.getRole().name());
    }

    @Test
    void listAccounts_withAuthentication_returnsAccountPage() throws Exception {
        mockMvc.perform(get("/account").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(model().attributeExists("accounts"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void listAccounts_withoutAuthentication_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void createAccount_withValidData_createsSuccessfully() throws Exception {
        mockMvc.perform(post("/account/create")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("customerId", testCustomer.getId().toString())
                        .param("accountType", "SAVINGS")
                        .param("balance", "1000.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/account?msg=*"));

        // Verify account was created
        assert accountRepository.count() == 1;
    }

    @Test
    void freezeAccount_withValidId_freezesSuccessfully() throws Exception {
        // Create a test account
        Account account = new Account();
        account.setAccountNumber("ACC1234567890");
        account.setCustomerId(testCustomer.getId());
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setBalance(new BigDecimal("1000.00"));
        account = accountRepository.save(account);

        mockMvc.perform(post("/account/freeze/" + account.getId())
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/account?msg=*"));

        // Verify account was frozen
        Account frozenAccount = accountRepository.findById(account.getId()).orElseThrow();
        assert frozenAccount.getAccountStatus() == Account.AccountStatus.FROZEN;
    }

    @Test
    void unfreezeAccount_withFrozenAccount_unfreezesSuccessfully() throws Exception {
        // Create a frozen test account
        Account account = new Account();
        account.setAccountNumber("ACC9999999999");
        account.setCustomerId(testCustomer.getId());
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setAccountStatus(Account.AccountStatus.FROZEN);
        account.setBalance(new BigDecimal("1000.00"));
        account = accountRepository.save(account);

        mockMvc.perform(post("/account/unfreeze/" + account.getId())
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/account?msg=*"));

        // Verify account was unfrozen
        Account unfrozenAccount = accountRepository.findById(account.getId()).orElseThrow();
        assert unfrozenAccount.getAccountStatus() == Account.AccountStatus.OPEN;
    }

    @Test
    void closeAccount_withValidId_closesSuccessfully() throws Exception {
        // Create a test account
        Account account = new Account();
        account.setAccountNumber("ACC8888888888");
        account.setCustomerId(testCustomer.getId());
        account.setAccountType(Account.AccountType.TRANSACTIONAL);
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setBalance(new BigDecimal("500.00"));
        account = accountRepository.save(account);

        mockMvc.perform(post("/account/close/" + account.getId())
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/account?msg=*"));

        // Verify account was closed
        Account closedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assert closedAccount.getAccountStatus() == Account.AccountStatus.CLOSED;
    }

    @Test
    void deleteAccount_asAdmin_deletesSuccessfully() throws Exception {
        // Create a test account
        Account account = new Account();
        account.setAccountNumber("ACC7777777777");
        account.setCustomerId(testCustomer.getId());
        account.setAccountType(Account.AccountType.CREDIT);
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setBalance(new BigDecimal("0.00"));
        account = accountRepository.save(account);

        Long accountId = account.getId();

        mockMvc.perform(post("/account/delete/" + accountId)
                        .session(adminSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/account?msg=*"));

        // Verify account was deleted
        assert accountRepository.findById(accountId).isEmpty();
    }

    @Test
    void deleteAccount_asNonAdmin_deniesAccess() throws Exception {
        // Create a test account
        Account account = new Account();
        account.setAccountNumber("ACC6666666666");
        account.setCustomerId(testCustomer.getId());
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setBalance(new BigDecimal("1000.00"));
        account = accountRepository.save(account);

        Long accountId = account.getId();

        mockMvc.perform(post("/account/delete/" + accountId)
                        .session(viewerSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/account?msg=*"));

        // Verify account was NOT deleted
        assert accountRepository.findById(accountId).isPresent();
    }
}