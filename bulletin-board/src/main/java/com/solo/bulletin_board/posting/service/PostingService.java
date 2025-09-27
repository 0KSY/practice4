package com.solo.bulletin_board.posting.service;

import com.solo.bulletin_board.exception.BusinessLogicException;
import com.solo.bulletin_board.exception.ExceptionCode;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.member.service.MemberService;
import com.solo.bulletin_board.posting.entity.Posting;
import com.solo.bulletin_board.posting.repository.PostingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberService memberService;

    public PostingService(PostingRepository postingRepository, MemberService memberService) {
        this.postingRepository = postingRepository;
        this.memberService = memberService;
    }

    public Posting findVerifiedPosting(long postingId){
        Optional<Posting> optionalPosting = postingRepository.findById(postingId);

        Posting findPosting = optionalPosting.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.POSTING_NOT_FOUND));

        return findPosting;
    }

    public Posting createPosting(Posting posting){

        Member findMember = memberService.findVerifiedMember(posting.getMember().getMemberId());
        posting.setMember(findMember);

        return postingRepository.save(posting);

    }

    public Posting updatePosting(Posting posting){

        Posting findPosting = findVerifiedPosting(posting.getPostingId());

        Optional.ofNullable(posting.getTitle())
                .ifPresent(title -> findPosting.setTitle(title));
        Optional.ofNullable(posting.getContent())
                .ifPresent(content -> findPosting.setContent(content));

        findPosting.setModifiedAt(LocalDateTime.now());

        return postingRepository.save(findPosting);

    }

    public Posting findPosting(long postingId){

        Posting findPosting = findVerifiedPosting(postingId);
        findPosting.setViewCount(findPosting.getViewCount() + 1);

        return postingRepository.save(findPosting);
    }

    public Page<Posting> findPostings(int page, int size){

        return postingRepository.findAll(
                PageRequest.of(page, size, Sort.by("postingId").descending()));

    }

    public void deletePosting(long postingId){
        Posting findPosting = findVerifiedPosting(postingId);
        postingRepository.delete(findPosting);
    }

}
