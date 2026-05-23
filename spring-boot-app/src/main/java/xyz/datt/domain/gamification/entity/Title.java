package xyz.datt.domain.gamification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.global.entity.BaseEntity;

@Getter
@Entity
@Table(name = "title")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Builder
    private Title(
        String code,
        String name,
        String description
    ) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
}