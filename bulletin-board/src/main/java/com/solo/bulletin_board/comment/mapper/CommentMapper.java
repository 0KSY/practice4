package com.solo.bulletin_board.comment.mapper;

import com.solo.bulletin_board.comment.dto.CommentDto;
import com.solo.bulletin_board.comment.entity.Comment;
import com.solo.bulletin_board.member.dto.MemberDto;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.posting.entity.Posting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default Comment commentPostDtoToComment(CommentDto.Post commentPostDto){

        Member member = new Member();
        member.setMemberId(commentPostDto.getMemberId());
        Posting posting = new Posting();
        posting.setPostingId(commentPostDto.getPostingId());

        Comment comment = new Comment();
        comment.setContent(commentPostDto.getContent());
        comment.setMember(member);
        comment.setPosting(posting);

        if(commentPostDto.getParentCommentId() != 0){

            Comment parentComment = new Comment();
            parentComment.setCommentId(commentPostDto.getParentCommentId());

            comment.setParentComment(parentComment);

        }

        return comment;

    }

    Comment commentPatchDtoToComment(CommentDto.Patch commentPatchDto);

    default CommentDto.Response commentToCommentResponseDto(Comment comment){

        CommentDto.Response response = CommentDto.Response.builder()
                .commentId(comment.getCommentId())
                .postingId(comment.getPosting().getPostingId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .memberResponse(MemberDto.MemberResponse.builder()
                        .memberId(comment.getMember().getMemberId())
                        .email(comment.getMember().getEmail())
                        .nickname(comment.getMember().getNickname())
                        .build()
                ).build();

        return response;

    }
}
