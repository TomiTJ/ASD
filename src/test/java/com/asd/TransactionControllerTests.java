package com.asd;
import com.asd.controller.TransactionController;
import com.asd.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TransactionControllerTests {

    private TransactionService transactionService;
    private TransactionController controller;

    @BeforeEach
    void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
        controller = new TransactionController(transactionService);
    }

    //void testNonEmptyDownload() throws IOException {
    //}


    @Test
    void testEmptyDownload() throws IOException {
        when(transactionService.findFilteredTransactions(anyString(), anyString(), anyString()))
                .thenReturn(List.of());

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = controller.handleDownloadRequest("search", "type", "status", redirectAttributes);

        assertEquals("redirect:/transactions", result);

        //Check if an error was thrown to avoid empty csv downloads after changes in code.
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMessage"));

    }

}