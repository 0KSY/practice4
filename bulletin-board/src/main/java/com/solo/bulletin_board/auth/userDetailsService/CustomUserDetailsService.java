package com.solo.bulletin_board.auth.userDetailsService;

import com.solo.bulletin_board.auth.utils.CustomAuthorityUtils;
import com.solo.bulletin_board.exception.BusinessLogicException;
import com.solo.bulletin_board.exception.ExceptionCode;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils customAuthorityUtils;

    public CustomUserDetailsService(MemberRepository memberRepository, CustomAuthorityUtils customAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.customAuthorityUtils = customAuthorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByEmail(username);

        Member findMember = optionalMember.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setMemberId(findMember.getMemberId());
        customUserDetails.setEmail(findMember.getEmail());
        customUserDetails.setPassword(findMember.getPassword());
        customUserDetails.setNickname(findMember.getNickname());
        customUserDetails.setRoles(findMember.getRoles());

        customUserDetails.setAuthorities(customAuthorityUtils.createAuthorities(findMember.getRoles()));

        return customUserDetails;

    }
}
