package xyz.datt.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String keyword;
    private String address;
    @Column(length = 2000) private String imageUrls;
    @Column(length = 1000, unique = true) private String placeUrl;
    private Double rating;
    private Integer reviewCount;
    @Enumerated(EnumType.STRING) private Platform platform;

    public void updateInfo(String name, Integer visitorReviewCount, List<String> imageUrls, Double rating) {
        if (name != null && !name.equals(this.name)) this.name = name;
        if (visitorReviewCount != null) this.reviewCount = visitorReviewCount;
        if (rating != null) this.rating = rating;

        if (imageUrls != null && !imageUrls.isEmpty()) {
            this.imageUrls = String.join("|", imageUrls);
        } else {
            this.imageUrls = null;
        }
    }
}