package com.training.url_shortener.service;

import com.training.url_shortener.entity.UrlMapEntry;
import com.training.url_shortener.exception.MissingEntryException;
import com.training.url_shortener.exception.SelfReferenceException;
import com.training.url_shortener.repository.UrlRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepositoryImpl urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    @Test
    void getLongUrl_WhenIdDoesNotExist_ShouldThrowMissingEntryException() {
        String id = "lkj234a";
        when(urlRepository.getById(id)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> urlService.getLongUrl(id))
                .isInstanceOf(MissingEntryException.class)
                .hasMessage(String.format("The requested URL (id=%s) does not exist", id));

        verify(urlRepository).getById(id);
        verifyNoMoreInteractions(urlRepository);
    }

    @Test
    void getLongUrl_WhenIdExists_ShouldReturnLongUrl() {
        String id = "lkj234a";
        var longUrl = URI.create("https://abcd.com");
        var entry = UrlMapEntry.builder()
                .id(id)
                .longUrl(longUrl)
                .build();
        when(urlRepository.getById(id)).thenReturn(entry);

        var result = urlService.getLongUrl(id);

        Assertions.assertThat(result).isEqualByComparingTo(longUrl);

        verify(urlRepository).getById(id);
        verifyNoMoreInteractions(urlRepository);
    }

    @Test
    void registerUrl_WhenAppHostIsSameAsUrlHost_ShouldThrowSelfReferenceException() {
        var request = mock(HttpServletRequest.class);
        var longUrl = URI.create("http://localhost:8080");

        when(request.getServerName()).thenReturn("localhost");

        Assertions.assertThatThrownBy(() -> urlService.registerUrl(request, longUrl))
                .isInstanceOf(SelfReferenceException.class)
                .hasMessage("The given URL references to this app");

        verifyNoInteractions(urlRepository);
    }
/*
    @Test
    void registerUrl_WhenAppHostIsNotSameAsUrlHost_ShouldCreateReturnUrlResponse() {
        var request = mock(HttpServletRequest.class);
        var longUrl = URI.create("http://localhost:8080");

        when(request.getServerName()).thenReturn("host");

        Assertions.assertThatThrownBy(() -> urlService.registerUrl(request, longUrl))
                .isInstanceOf(SelfReferenceException.class)
                .hasMessage("The given URL references to this app");

        verifyNoInteractions(urlRepository);
    }*/
}