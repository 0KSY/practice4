package com.solo.bulletin_board.posting.dto;

import com.solo.bulletin_board.comment.dto.CommentDto;
import com.solo.bulletin_board.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class PostingDto {

    @Getter
    @Setter
    public static class Post{
        @Positive
        private long memberId;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    public static class Patch{
        private long postingId;
        private String title;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class Response{
        private long postingId;
        private String title;
        private String content;
        private int viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private MemberDto.MemberResponse memberResponse;
        private List<CommentDto.ParentCommentResponse> parentCommentResponses;
    }

    @Getter
    @Setter
    @Builder
    public static class PostingInfoResponse{
        private long postingId;
        private String title;
        private int viewCount;
        private int commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private MemberDto.MemberResponse memberResponse;
    }
}
