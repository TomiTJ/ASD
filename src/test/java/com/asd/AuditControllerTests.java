package com.asd;

import com.asd.model.Action;
import com.asd.model.Audit;
import com.asd.model.ResourceType;
import com.asd.model.User;
import com.asd.repository.AuditRepository;
import com.asd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuditControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepo;
    @Autowired private AuditRepository auditRepo;

    private final ZoneId SYD = ZoneId.of("Australia/Sydney");

    @BeforeEach
    void cleanDb() {
        auditRepo.deleteAll();
        userRepo.deleteAll();
    }

    private User newAdmin(String suffix) {
        User u = new User(
                "Admin " + suffix,
                "admin" + suffix + "@bank.local",
                "secret",
                User.Role.ADMIN
        );
        u.setStatus(User.Status.ACTIVE);
        return userRepo.save(u);
    }

    private Audit addAudit(User actor, Action action, LocalDate when) {
        Audit e = new Audit();
        e.setUser(actor);
        e.setAction(action);
        e.setResourceType(ResourceType.USER);
        e.setResourceId(UUID.randomUUID());
        e.setRequestId(UUID.randomUUID());
        Instant ts = when.atTime(12, 0).atZone(SYD).toInstant();
        e.setCreatedAt(ts);
        return auditRepo.save(e);
    }


    @Test
    void auditPage_requiresLogin_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/audit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void api_requiresLogin_returns401() throws Exception {
        mockMvc.perform(get("/api/audits"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void api_returnsData_whenLoggedIn_noFilters() throws Exception {
        User admin = newAdmin("A");
        addAudit(admin, Action.CREATE, LocalDate.now(SYD).minusDays(2));
        addAudit(admin, Action.UPDATE, LocalDate.now(SYD));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", admin.getId());

        mockMvc.perform(get("/api/audits").session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].action", anyOf(is("CREATE"), is("UPDATE"))))
                .andExpect(jsonPath("$[0].createdAt", notNullValue()));
    }

    @Test
    void api_filtersByActionAndDate() throws Exception {
        User admin = newAdmin("B");
        // Three events across different days/actions
        addAudit(admin, Action.CREATE, LocalDate.now(SYD).minusDays(5));
        addAudit(admin, Action.UPDATE, LocalDate.now(SYD).minusDays(1)); // should match
        addAudit(admin, Action.DELETE, LocalDate.now(SYD));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", admin.getId());

        String from = LocalDate.now(SYD).minusDays(2).toString(); // YYYY-MM-DD
        String to   = LocalDate.now(SYD).toString();

        mockMvc.perform(get("/api/audits")
                        .session(session)
                        .param("action", "UPDATE")
                        .param("from", from)
                        .param("to", to))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].action", is("UPDATE")));
    }
}