package com.training.url_shortener.controller;

import com.training.url_shortener.dto.UrlRequest;
import com.training.url_shortener.exception.MissingEntryException;
import com.training.url_shortener.exception.SelfReferenceException;
import com.training.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortenerController.class)
class ShortenerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UrlService urlService;

    @Test
    void redirect_WhenMissingUrlExceptionIsThrown_ShouldReturnStatus404() throws Exception {
        var id = "a1b8c3d";

        var ex = new MissingEntryException(id);
        when(urlService.getLongUrl(id)).thenThrow(ex);

        var path = String.format("/%s", id);
        mockMvc.perform(get("/{id}", id))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.exceptionName").value(ex.getClass().getSimpleName()))
                .andExpect(jsonPath("$.path").value(path))
                .andExpect(jsonPath("$.message").isNotEmpty());

        verify(urlService).getLongUrl(id);
        verifyNoMoreInteractions(urlService);
    }

    @Test
    void getShortUrl_WhenSelfReferenceExceptionIsThrow_ShouldReturnStatus400() throws Exception {
        var urlRequest = new UrlRequest("https://example.com");

        var ex = new SelfReferenceException();
        when(urlService.registerUrl(any(HttpServletRequest.class), eq(urlRequest.toUri())))
                .thenThrow(ex);

        var path = "/v1/api/shorten";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(urlRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.exceptionName").value(ex.getClass().getSimpleName()))
                .andExpect(jsonPath("$.path").value(path))
                .andExpect(jsonPath("$.message").isNotEmpty());

        verify(urlService).registerUrl(any(HttpServletRequest.class), eq(urlRequest.toUri()));
        verifyNoMoreInteractions(urlService);
    }
}