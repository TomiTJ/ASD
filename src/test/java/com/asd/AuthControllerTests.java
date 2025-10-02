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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;

    @BeforeEach
    void cleanDb() {
        userRepo.deleteAll();
    }

    @Test
    void login_withValidCredentials_redirectsToDashboard() throws Exception {
        String email = "testadmin_" + System.currentTimeMillis() + "@bank.local";
        User user = new User("Test Admin", email,
                encoder.encode("password123"), User.Role.ADMIN);
        userRepo.save(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void login_withInvalidPassword_redirectsWithError() throws Exception {
        String email = "viewer_" + System.currentTimeMillis() + "@bank.local";
        User user = new User("Viewer", email,
                encoder.encode("correctpw"), User.Role.READ_ONLY);
        userRepo.save(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", "wrongpw"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/login?error=*"));
    }
}


