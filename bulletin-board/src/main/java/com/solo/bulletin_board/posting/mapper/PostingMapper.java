package com.solo.bulletin_board.posting.mapper;

import com.solo.bulletin_board.member.dto.MemberDto;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.posting.dto.PostingDto;
import com.solo.bulletin_board.posting.entity.Posting;
import org.mapstruct.Mapper;

import java.util.List;

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

        return response;
    }

    List<PostingDto.PostingInfoResponse> postingsToPostingInfoResponseDtos(List<Posting> postings);
}
