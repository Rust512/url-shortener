package com.training.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "url_map_entry")
public class UrlMapEntry {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private String id;

    private String longUrl;
}
