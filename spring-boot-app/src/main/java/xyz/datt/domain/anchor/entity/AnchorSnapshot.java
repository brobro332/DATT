package xyz.datt.domain.anchor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AnchorSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String keyword;

    @Column(columnDefinition = "TEXT")
    private String contentJson;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public AnchorSnapshot(String keyword, String contentJson) {
        this.keyword = keyword;
        this.contentJson = contentJson;
    }
}
