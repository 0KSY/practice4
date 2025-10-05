package com.solo.bulletin_board.postingLike.service;

import com.solo.bulletin_board.auth.userDetailsService.CustomUserDetails;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.member.service.MemberService;
import com.solo.bulletin_board.posting.entity.Posting;
import com.solo.bulletin_board.posting.service.PostingService;
import com.solo.bulletin_board.postingLike.entity.PostingLike;
import com.solo.bulletin_board.postingLike.repository.PostingLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PostingLikeService {

    private final PostingLikeRepository postingLikeRepository;
    private final MemberService memberService;
    private final PostingService postingService;

    public PostingLikeService(PostingLikeRepository postingLikeRepository,
                              MemberService memberService,
                              PostingService postingService) {
        this.postingLikeRepository = postingLikeRepository;
        this.memberService = memberService;
        this.postingService = postingService;
    }

    public Posting createPostingLike(PostingLike postingLike, CustomUserDetails customUserDetails){

        Member findMember = memberService.findVerifiedMember(customUserDetails.getMemberId());
        postingLike.setMember(findMember);

        Optional<PostingLike> optionalPostingLike = postingLikeRepository.findByMemberMemberIdAndPostingPostingId(
                postingLike.getMember().getMemberId(), postingLike.getPosting().getPostingId());

        if(optionalPostingLike.isPresent()){
            postingLikeRepository.delete(optionalPostingLike.get());
        }
        else{
            postingLikeRepository.save(postingLike);
        }

        return postingService.findVerifiedPosting(postingLike.getPosting().getPostingId());

    }
}
