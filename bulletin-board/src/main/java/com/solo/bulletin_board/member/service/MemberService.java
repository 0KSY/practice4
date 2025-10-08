package com.solo.bulletin_board.member.service;

import com.solo.bulletin_board.auth.userDetailsService.CustomUserDetails;
import com.solo.bulletin_board.auth.utils.CustomAuthorityUtils;
import com.solo.bulletin_board.exception.BusinessLogicException;
import com.solo.bulletin_board.exception.ExceptionCode;
import com.solo.bulletin_board.image.S3FileUploadService;
import com.solo.bulletin_board.member.entity.Member;
import com.solo.bulletin_board.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final S3FileUploadService s3FileUploadService;

    public MemberService(MemberRepository memberRepository, CustomAuthorityUtils customAuthorityUtils,
                         PasswordEncoder passwordEncoder, S3FileUploadService s3FileUploadService) {
        this.memberRepository = memberRepository;
        this.customAuthorityUtils = customAuthorityUtils;
        this.passwordEncoder = passwordEncoder;
        this.s3FileUploadService = s3FileUploadService;
    }

    public Member findVerifiedMember(long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        Member findMember = optionalMember.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return findMember;
    }

    public void verifyExistsMember(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if(optionalMember.isPresent()){
            if(optionalMember.get().getSignupType() == Member.SignupType.SERVER){
                throw new BusinessLogicException(ExceptionCode.MEMBER_SERVER_USER);
            }
            else{
                throw new BusinessLogicException(ExceptionCode.MEMBER_GOOGLE_OAUTH2_USER);
            }
        }
    }

    public void verifyExistsNickname(String nickname){
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);

        if(optionalMember.isPresent()){
            throw new BusinessLogicException(ExceptionCode.MEMBER_NICKNAME_EXISTS);
        }
    }

    public void checkMemberId(long memberId, CustomUserDetails customUserDetails){

        if(memberId != customUserDetails.getMemberId()){
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_MATCHED);
        }
    }

    public void checkFileForm(String fileForm){

        List<String> imageForm = Arrays.asList("jpg", "jpeg", "png", "gif");

        if(!imageForm.contains(fileForm)){
            throw new BusinessLogicException(ExceptionCode.FILE_TYPE_NOT_ALLOWED);
        }
    }

    public Member createMember(Member member){

        verifyExistsMember(member.getEmail());
        verifyExistsNickname(member.getNickname());

        member.setSignupType(Member.SignupType.SERVER);

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = customAuthorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        return memberRepository.save(member);

    }

    public Member uploadImage(MultipartFile multipartFile, CustomUserDetails customUserDetails){

        Member findMember = findVerifiedMember(customUserDetails.getMemberId());

        if(multipartFile.isEmpty()){
            if(findMember.getImage() != null){
                s3FileUploadService.deleteImageFile(findMember.getImage());
            }
            findMember.setImage(null);
        }
        else{
            String fileForm = multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf(".") +1).toLowerCase();

            checkFileForm(fileForm);

            if(findMember.getImage() != null){
                s3FileUploadService.deleteImageFile(findMember.getImage());
            }

            String imageUrl = s3FileUploadService.uploadImageFile(multipartFile, fileForm);
            findMember.setImage(imageUrl);
        }

        return memberRepository.save(findMember);

    }

    public Member updateMember(Member member, CustomUserDetails customUserDetails){

        Member findMember = findVerifiedMember(customUserDetails.getMemberId());

        Optional.ofNullable(member.getNickname())
                .ifPresent(nickname -> {
                    if(!nickname.equals(findMember.getNickname())){
                        verifyExistsNickname(nickname);
                    }
                    findMember.setNickname(nickname);
                });

        return memberRepository.save(findMember);

    }

    public Member findMember(CustomUserDetails customUserDetails){

        return findVerifiedMember(customUserDetails.getMemberId());
    }

    public void deleteMember(String password, CustomUserDetails customUserDetails){

        Member findMember = findVerifiedMember(customUserDetails.getMemberId());

        if(!passwordEncoder.matches(password, findMember.getPassword())){
            throw new BusinessLogicException(ExceptionCode.MEMBER_PASSWORD_NOT_MATCHED);
        }

        memberRepository.delete(findMember);
    }

}
