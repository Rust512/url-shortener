package com.training.url_shortener.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.net.URI;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "urlMap")
public class UrlMapEntry {
    @Id
    private String id;

    @Field(targetType = FieldType.STRING)
    private URI longUrl;
}
