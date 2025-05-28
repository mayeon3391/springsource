package com.example.movie.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.movie.dto.AuthMemberDTO;
import com.example.movie.dto.MemberDTO;
import com.example.movie.dto.PasswordDTO;
import com.example.movie.entity.Member;
import com.example.movie.entity.MemberRole;
import com.example.movie.repository.MemberReposiroty;
import com.example.movie.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberReposiroty memberRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 탈퇴
    @Transactional
    public void leaveMember(MemberDTO memberDTO) {
        // 비밀번호가 일치하는지 확인
        Member member = memberRepository.findByEmail(memberDTO.getEmail());
        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(memberDTO.getPassword(), member.getPassword())) {
            throw new IllegalStateException("현재 비밀번호가 다릅니다.");
        } else {
            // 일치 => 리뷰 제거, 회원 탈퇴
            reviewRepository.deleteByMember(member);
            memberRepository.delete(member);
        }
    }

    // 닉네임 변경
    @Transactional
    public void updateNickname(MemberDTO memberDTO) {
        // 수정하고자 하는 멤버 찾기 => 수정할 부분 변경 => save
        // Member member = memberRepository.findByEmail(null);
        // member.changeNickname("null");
        // memberRepository.save(member);
        memberRepository.updateNickname(memberDTO.getNickname(), memberDTO.getEmail());

    }

    // 비밀번호 변경
    // 닉네임 변경
    public void updatePassword(PasswordDTO passwordDTO) throws IllegalStateException {
        // 수정하고자 하는 멤버 찾기 => 수정할 부분 변경 => save
        Member member = memberRepository.findByEmail(passwordDTO.getEmail());

        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), member.getPassword())) {
            throw new IllegalStateException("현재 비밀번호가 다릅니다.");
        } else {
            // 수정
            member.changePassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            memberRepository.save(member);
        }
    }

    // 회원가입
    public void register(MemberDTO dto) throws IllegalStateException {
        // dto => entity
        // 비밀번호 암호화
        // 중복확인
        validateEmail(dto.getEmail());

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .memberRole(dto.getMemberRole())
                .build();

        memberRepository.save(member);
    }

    // 이메일 중복 여부
    private void validateEmail(String email) {
        Member member = memberRepository.findByEmail(email);

        // IllegalStateException : RuntimeException(실행해야 나오는 예외)
        if (member != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("email {}", email);

        Member member = memberRepository.findByEmail(email);

        if (member == null)
            throw new UsernameNotFoundException("이메일 확인");

        // entity => dto
        MemberDTO memberDTO = MemberDTO
                .builder()
                .mid(member.getMid())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .memberRole(member.getMemberRole())
                .build();

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(memberDTO);

        return authMemberDTO;
    }
}
