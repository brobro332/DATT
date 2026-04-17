package xyz.datt.domain.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "keyword_histories")
public class KeywordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private String category;
    @Enumerated(EnumType.STRING) private Platform platform;

    private LocalDateTime lastScrapedAt;

    public KeywordHistory(String keyword, String category, Platform platform) {
        this.keyword = keyword;
        this.category = category;
        this.platform = platform;
        this.lastScrapedAt = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.lastScrapedAt = LocalDateTime.now();
    }
}