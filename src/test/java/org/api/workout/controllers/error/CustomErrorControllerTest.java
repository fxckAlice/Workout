package org.api.workout.controllers.error;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomErrorControllerTest {

    private CustomErrorController errorController;

    @BeforeEach
    void setUp() {
        errorController = new CustomErrorController();
    }

    @Test
    void handleError_ShouldReturnCorrectErrorResponse() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("jakarta.servlet.error.status_code")).thenReturn(404);

        ResponseEntity<Map<String, Object>> response = errorController.handleError(mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);
        assertEquals("Something went wrong", body.get("error"));
        assertEquals(404, body.get("status"));
    }

    @Test
    void handleError_With500_ShouldReturnInternalServerErrorResponse() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getAttribute("jakarta.servlet.error.status_code")).thenReturn(500);

        ResponseEntity<Map<String, Object>> response = errorController.handleError(mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        Assertions.assertNotNull(body);
        assertEquals("Something went wrong", body.get("error"));
        assertEquals(500, body.get("status"));
    }
}
