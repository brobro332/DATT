package xyz.datt.domain.member.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    @Test
    @DisplayName("경험치를 추가하면 exp가 증가한다.")
    void givenExpAmount_whenAddExp_thenIncreaseExp() {
        Member member = Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );

        member.addExp(30);

        assertThat(member.getExp()).isEqualTo(30);
        assertThat(member.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("경험치가 100 이상이면 레벨업한다.")
    void givenEnoughExp_whenAddExp_thenLevelUp() {
        Member member = Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );

        member.addExp(100);

        assertThat(member.getExp()).isEqualTo(100);
        assertThat(member.getLevel()).isEqualTo(2);
    }

    @Test
    @DisplayName("0 이하 경험치는 반영하지 않는다.")
    void givenInvalidExp_whenAddExp_thenDoNothing() {
        Member member = Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );

        member.addExp(0);
        member.addExp(-10);

        assertThat(member.getExp()).isEqualTo(0);
        assertThat(member.getLevel()).isEqualTo(1);
    }
}