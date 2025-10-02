package com.asd;

import com.asd.model.User;
import com.asd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;

    @BeforeEach
    void cleanDb() {
        // ✅ clear users before each test
        userRepo.deleteAll();
    }

    @Test
    void login_withValidCredentials_redirectsToDashboard() throws Exception {
        User user = new User("Test Admin", "testadmin@bank.local",
                encoder.encode("password123"), User.Role.ADMIN);
        userRepo.save(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "testadmin@bank.local")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void login_withInvalidPassword_redirectsWithError() throws Exception {
        User user = new User("Viewer", "viewer1@bank.local",
                encoder.encode("correctpw"), User.Role.READ_ONLY);
        userRepo.save(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "viewer1@bank.local")
                        .param("password", "wrongpw"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/login?error=*"));
    }
}

