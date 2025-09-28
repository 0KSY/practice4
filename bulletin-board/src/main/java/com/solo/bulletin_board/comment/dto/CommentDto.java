package com.solo.bulletin_board.comment.dto;

import com.solo.bulletin_board.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @Setter
    public static class Post{
        @Positive
        private long memberId;
        @Positive
        private long postingId;
        @NotBlank
        private String content;
        private long parentCommentId;

    }

    @Getter
    @Setter
    public static class Patch{
        private long commentId;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Response{
        private long commentId;
        private long postingId;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private MemberDto.MemberResponse memberResponse;
    }
}
