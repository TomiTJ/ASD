package com.asd;

import com.asd.controller.DashboardController;
import com.asd.dto.DashboardMetricDto;
import com.asd.services.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   // ✅ replaces @MockBean
    private DashboardService dashboardService;

    @Test
    void testGetDashboardMetrics() throws Exception {
        DashboardMetricDto dto = new DashboardMetricDto(6, 2, 5);
        when(dashboardService.getMetrics()).thenReturn(dto);

        mockMvc.perform(get("/api/dashboard/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(6))
                .andExpect(jsonPath("$.totalAccounts").value(2))
                .andExpect(jsonPath("$.totalTransactions").value(5));
    }
}