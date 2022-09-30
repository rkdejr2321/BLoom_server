package gachon.BLoom.member.service;

import gachon.BLoom.entity.Authority;
import gachon.BLoom.entity.Member;
import gachon.BLoom.exception.DuplicateMemberException;
import gachon.BLoom.exception.NotMatchPasswordException;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.dto.RegistMemberDto;
import gachon.BLoom.member.repository.MemberRepository;
import gachon.BLoom.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final FileUtil fileUtil;


    @Override
    public void registMember(RegistMemberDto registMemberDto){
        Optional<Member> findMember = memberRepository.findByEmail(registMemberDto.getEmail());
        if(!findMember.isEmpty()) {
            throw new DuplicateMemberException("이미 가입된 이메일입니다.");
        } else {
            Member member = Member.builder()
                    .email(registMemberDto.getEmail())
                    .username(registMemberDto.getUsername())
                    .password(passwordEncoder.encode(registMemberDto.getPassword()))
                    .userImage(fileUtil.saveMemberImage(registMemberDto.getUserImage()))
                    .authority(Authority.MEMBER)
                    .build();

            memberRepository.save(member);
        }
    }

    @Override
    public LoginInfoDto login(LoginMemberDto loginMemberDto) {
        Optional<Member> findMember = memberRepository.findByEmail(loginMemberDto.getEmail());
        log.info(findMember.get().getPassword());
        if(findMember.isEmpty() || !passwordEncoder.matches(loginMemberDto.getPassword(), findMember.get().getPassword())) {
            throw new NotMatchPasswordException("아이디와 비밀번호가 일치하지 않습니다.");
        } else {
            LoginInfoDto loginInfoDto = LoginInfoDto.builder()
                    .username(findMember.get().getUsername())
                    .email(findMember.get().getEmail())
                    .userImage("http://localhost:8080/images/member/" + findMember.get().getUserImage())
                    .build();

            return loginInfoDto;
        }
    }
}
