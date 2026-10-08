package com.training.url_shortener.controller;

import com.training.url_shortener.TestcontainersConfiguration;
import com.training.url_shortener.dto.UrlRequest;
import com.training.url_shortener.dto.UrlResponse;
import com.training.url_shortener.entity.UrlMapEntry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerControllerIT {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void afterEachTest() {
        mongoTemplate.remove(new Query(), UrlMapEntry.class);
    }

    @Test
    void redirect_WhenIdExists_ShouldReturnCorrespondingLongUrl() {
        // pre-populate data
        var id = "d1e2f3u";
        var longUrl = URI.create("https://something/path");
        var entry = UrlMapEntry.builder()
                .id(id)
                .longUrl(longUrl)
                .build();
        mongoTemplate.save(entry);

        // call API and verify result.
        var requestUri = UriComponentsBuilder.fromUriString("/{id}")
                .buildAndExpand(id)
                .toUri();

        var response = restTestClient.get()
                .uri(requestUri)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().location(longUrl.toString())
                .returnResult();

        Assertions.assertThat(response)
                .isNotNull();
    }

    @Test
    void getShortUrl_WithValidRequest_ShouldRegisterUrl() {
        // no need to pre-populate data, just call the API and verify the result.
        var longUrl = URI.create("https://something-else/different-path");
        var urlRequest = new UrlRequest(longUrl.toString());

        var response = restTestClient.post()
                .uri("/v1/api/shorten")
                .body(urlRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(UrlResponse.class)
                .getResponseBody();

        Assertions.assertThat(response)
                .isNotNull();

        var responseLongUrl = response.longUrl();
        var shortUrl = response.shortUrl();

        Assertions.assertThat(responseLongUrl)
                .isNotNull();
        Assertions.assertThat(shortUrl)
                .isNotNull();
    }
}