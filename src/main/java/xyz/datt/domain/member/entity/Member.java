package xyz.datt.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.global.entity.BaseEntity;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int exp;

    private Member(
        String email,
        String password,
        String nickname,
        MemberRole role
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.level = 1;
        this.exp = 0;
    }

    public static Member createUser(
            String email,
            String password,
            String nickname
    ) {
        return new Member(
            email,
            password,
            nickname,
            MemberRole.USER
        );
    }
}
