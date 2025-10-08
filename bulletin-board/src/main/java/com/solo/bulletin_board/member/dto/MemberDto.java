package com.solo.bulletin_board.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class MemberDto {

    @Getter
    @Setter
    public static class Post{
        @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String nickname;
    }

    @Getter
    @Setter
    public static class Patch{
        private String nickname;
    }

    @Getter
    @Setter
    public static class Password{
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private long memberId;
        private String email;
        private String nickname;
        private String image;
    }

    @Getter
    @Setter
    @Builder
    public static class ImageResponse{
        private long memberId;
        private String image;
    }

    @Getter
    @Setter
    @Builder
    public static class MemberResponse{
        private long memberId;
        private String email;
        private String nickname;
    }


}
