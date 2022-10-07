package gachon.BLoom.member.service;

import gachon.BLoom.entity.Account;
import gachon.BLoom.entity.Role;
import gachon.BLoom.entity.Member;
import gachon.BLoom.exception.DuplicateMemberException;
import gachon.BLoom.exception.NotMatchPasswordException;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.dto.RegistMemberDto;
import gachon.BLoom.member.repository.AccountRepository;
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

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final FileUtil fileUtil;


    @Override
    public void registMember(RegistMemberDto registMemberDto){
        Optional<Account> findAccount = memberRepository.findAccountByEmail(registMemberDto.getEmail());
        if(!findAccount.isEmpty()) {
            throw new DuplicateMemberException("이미 가입된 이메일입니다.");
        } else {
            //소셜 회원가입이 아닌 일반 회원가입
            Member member = Member.builder()
                    .email(registMemberDto.getEmail())
                    .username(registMemberDto.getUsername())
                    .userImage(fileUtil.saveMemberImage(registMemberDto.getUserImage()))
                    .provider("bloom")
                    .role(Role.MEMBER)
                    .build();

            Account account = Account.builder()
                    .email(registMemberDto.getEmail())
                    .password(passwordEncoder.encode(registMemberDto.getPassword()))
                    .build();

            accountRepository.save(registMemberDto.toEntity(account, member));
        }
    }

    @Override
    public LoginInfoDto login(LoginMemberDto loginMemberDto) {
        Optional<Account> account = accountRepository.findAccountByEmail(loginMemberDto.getEmail());
        log.info(account.get().getPassword());
        if(account.isEmpty() || !passwordEncoder.matches(loginMemberDto.getPassword(), account.get().getPassword())) {
            throw new NotMatchPasswordException("아이디와 비밀번호가 일치하지 않습니다.");
        } else {
            LoginInfoDto loginInfoDto = LoginInfoDto.builder()
                    .username(account.get().getMember().getUsername())
                    .email(account.get().getMember().getEmail())
                    .userImage("http://localhost:8080/images/member/" + account.get().getMember().getUserImage())
                    .build();

            return loginInfoDto;
        }
    }
}
