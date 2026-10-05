package com.training.url_shortener.repository;

import com.training.url_shortener.TestcontainersConfiguration;
import com.training.url_shortener.entity.UrlMapEntry;
import com.training.url_shortener.generator.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.net.URI;
import java.util.Objects;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class UrlRepositoryImplIT {

    @MockitoSpyBean
    private IdGenerator idGenerator;

    @MockitoSpyBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("urls")).invalidate();
    }

    @Test
    void getById_WhenIdDoesNotExist_ShouldReturnNullFromCacheTheSecondTime() {
        String id = "a1b2c3d";

        var firstResult = urlRepository.getById(id);

        Assertions.assertThat(firstResult).isNull();

        var secondResult = urlRepository.getById(id);

        Assertions.assertThat(secondResult).isNull();

        verify(mongoTemplate).findById(id, UrlMapEntry.class);
    }

    @Test
    void getById_WhenIdDoesNotExist_ShouldReturnLongUrlAfterRegistration() {
        String id = "a1b2c3d";

        // URL does not exist
        var firstResult = urlRepository.getById(id);

        Assertions.assertThat(firstResult).isNull();

        // register a URL with the same id

        String longUrl = "https://example.com";
        when(idGenerator.generateId()).thenReturn(id);
        var registrationResult = urlRepository.saveLongUrl(longUrl);

        Assertions.assertThat(registrationResult).isNotNull();
        Assertions.assertThat(registrationResult.getId()).isEqualTo(id);
        Assertions.assertThat(registrationResult.getLongUrl()).isEqualByComparingTo(URI.create(longUrl));

        // URL should exist now.
        var secondResult = urlRepository.getById(id);

        Assertions.assertThat(secondResult).isNotNull();
        verify(mongoTemplate).findById(id, UrlMapEntry.class);
        verify(mongoTemplate).save(any(UrlMapEntry.class));
    }
}