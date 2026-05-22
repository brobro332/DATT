package xyz.datt.domain.member.dto;

import xyz.datt.domain.gamification.entity.MemberTitle;

public record SelectedTitleResponse(
    Long titleId,
    String code,
    String name
) {

    public static SelectedTitleResponse from(MemberTitle memberTitle) {
        if (memberTitle == null) {
            return null;
        }

        return new SelectedTitleResponse(
            memberTitle.getTitle().getId(),
            memberTitle.getTitle().getCode(),
            memberTitle.getTitle().getName()
        );
    }
}