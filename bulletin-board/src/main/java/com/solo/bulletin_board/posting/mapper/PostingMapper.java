package com.solo.bulletin_board.posting.mapper;

import com.solo.bulletin_board.comment.dto.CommentDto;
import com.solo.bulletin_board.member.dto.MemberDto;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.posting.dto.PostingDto;
import com.solo.bulletin_board.posting.entity.Posting;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostingMapper {

    default Posting postingPostDtoToPosting(PostingDto.Post postingPostDto){

        Member member = new Member();
        member.setMemberId(postingPostDto.getMemberId());

        Posting posting = new Posting();
        posting.setTitle(postingPostDto.getTitle());
        posting.setContent(postingPostDto.getContent());
        posting.setViewCount(0);
        posting.setMember(member);

        return posting;

    }

    Posting postingPatchDtoToPosting(PostingDto.Patch postingPatchDto);

    default PostingDto.Response postingToPostingResponseDto(Posting posting){

        PostingDto.Response response = PostingDto.Response.builder()
                .postingId(posting.getPostingId())
                .title(posting.getTitle())
                .content(posting.getContent())
                .viewCount(posting.getViewCount())
                .createdAt(posting.getCreatedAt())
                .modifiedAt(posting.getModifiedAt())
                .memberResponse(MemberDto.MemberResponse.builder()
                        .memberId(posting.getMember().getMemberId())
                        .email(posting.getMember().getEmail())
                        .nickname(posting.getMember().getNickname())
                        .build()
                ).build();

        List<CommentDto.ParentCommentResponse> parentCommentResponses
                = posting.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(comment -> CommentDto.ParentCommentResponse.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .memberResponse(MemberDto.MemberResponse.builder()
                                .memberId(comment.getMember().getMemberId())
                                .email(comment.getMember().getEmail())
                                .nickname(comment.getMember().getNickname())
                                .build()
                        ).childCommentResponses(comment.getChildComments().stream()
                                .map(childComment -> CommentDto.ChildCommentResponse.builder()
                                        .commentId(childComment.getCommentId())
                                        .parentCommentId(childComment.getParentComment().getCommentId())
                                        .content(childComment.getContent())
                                        .createdAt(childComment.getCreatedAt())
                                        .modifiedAt(childComment.getModifiedAt())
                                        .memberResponse(MemberDto.MemberResponse.builder()
                                                .memberId(childComment.getMember().getMemberId())
                                                .email(childComment.getMember().getEmail())
                                                .nickname(childComment.getMember().getNickname())
                                                .build()
                                        ).build()
                                ).collect(Collectors.toList())
                        ).build()
                ).collect(Collectors.toList());

        response.setParentCommentResponses(parentCommentResponses);

        return response;

    }

    default PostingDto.PostingInfoResponse postingToPostingInfoResponseDto(Posting posting){

        PostingDto.PostingInfoResponse response = PostingDto.PostingInfoResponse.builder()
                .postingId(posting.getPostingId())
                .title(posting.getTitle())
                .viewCount(posting.getViewCount())
                .createdAt(posting.getCreatedAt())
                .modifiedAt(posting.getModifiedAt())
                .memberResponse(MemberDto.MemberResponse.builder()
                        .memberId(posting.getMember().getMemberId())
                        .email(posting.getMember().getEmail())
                        .nickname(posting.getMember().getNickname())
                        .build()
                ).build();

        int parentCommentCount = (int) posting.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)
                .count();

        int childCommentCount = posting.getComments().stream()
                .filter(comment -> comment.getParentComment() == null)
                .mapToInt(comment -> comment.getChildComments().size())
                .sum();

        response.setCommentCount(parentCommentCount + childCommentCount);


        return response;
    }

    List<PostingDto.PostingInfoResponse> postingsToPostingInfoResponseDtos(List<Posting> postings);
}
