package gachon.BLoom.member.service;

import gachon.BLoom.diagnose.dto.HashDto;
import gachon.BLoom.diagnose.repository.DiagRepository;
import gachon.BLoom.entity.Account;
import gachon.BLoom.entity.Questionnaire;
import gachon.BLoom.entity.Role;
import gachon.BLoom.entity.Member;
import gachon.BLoom.exception.DuplicateMemberException;
import gachon.BLoom.exception.NotMatchPasswordException;
import gachon.BLoom.member.dto.*;

import gachon.BLoom.member.repository.AccountRepository;
import gachon.BLoom.member.repository.MemberRepository;
import gachon.BLoom.member.repository.PopRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.PropertySource;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:application.properties")
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final AccountRepository accountRepository;

    private final PopRepository popRepository;

    private final DiagRepository diagRepository;


    private final PasswordEncoder passwordEncoder;

    @Override
    public void registMember(RegistMemberDto registMemberDto){
        Optional<Account> findAccount = accountRepository.findAccountByUserId(registMemberDto.getUserId());
        if(!findAccount.isEmpty()) {
            throw new DuplicateMemberException("이미 가입된 아이디입니다.");
        } else {
            //소셜 회원가입이 아닌 일반 회원가입
            Member member = Member.builder()
                    .email(registMemberDto.getEmail())
                    .userId(registMemberDto.getUserId())
                    .username(registMemberDto.getUsername())
                    .provider("bloom")
                    .role(Role.MEMBER)
                    .build();

            Account account = Account.builder()
                    .userId(registMemberDto.getUserId())
                    .password(passwordEncoder.encode(registMemberDto.getPassword()))
                    .build();
            log.info(account.getUserId());
            accountRepository.save(registMemberDto.toEntity(account, member));
        }
    }



    @Override
    public LoginInfoDto login(LoginMemberDto loginMemberDto) {
        Optional<Account> account = accountRepository.findAccountByUserId(loginMemberDto.getUserId());
        if(account.isEmpty() || !passwordEncoder.matches(loginMemberDto.getPassword(), account.get().getPassword())) {
            throw new NotMatchPasswordException("아이디와 비밀번호가 일치하지 않습니다.");
        } else {

            Date now = new Date();

            String secret = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";

            Map<String, Object> jwtHeader = new HashMap<>();
            jwtHeader.put("typ", "JWT");
            jwtHeader.put("alg", "HS256");
            jwtHeader.put("regDate", System.currentTimeMillis());

            Member memberByEmail = memberRepository.findMemberByUserId(loginMemberDto.getUserId()).get();
            LocalDateTime recentDiagnose = diagRepository.findDiagnoseDateByMemberId(memberByEmail.getId());
            Map<String, Object> claim = new HashMap<>();
            claim.put("email", memberByEmail.getEmail());
            claim.put("userId", memberByEmail.getUserId());
            claim.put("username", memberByEmail.getUsername());
            claim.put("member_id", memberByEmail.getId());
            if(recentDiagnose == null) {
                claim.put("recentDiagnose", null);
            } else {
                claim.put("recentDiagnose", recentDiagnose.toString());
            }

            claim.put("exp",new Date(now.getTime() + Duration.ofMinutes(30).toMillis()));

            String token = Jwts.builder()
                    .setSubject(String.valueOf(memberByEmail.getId()))
                    .setHeader(jwtHeader)
                    .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                    .setClaims(claim)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();


            LoginInfoDto loginInfoDto = LoginInfoDto.builder()
                    .token(token)
                    .username(account.get().getMember().getUsername())
                    .email(account.get().getMember().getEmail())
                    .userImage("http://localhost:8080/images/member/" + account.get().getMember().getUserImage())
                    .build();

            return loginInfoDto;
        }
    }

    @Override
    public Member findEmail(String email) {
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(email);
        if(memberByEmail.isEmpty()) {
            return null;
        }
        else {
            return memberByEmail.get();
        }
    }

    @Override
    public void changePw(String userId,String newPassword) {
        Optional<Account> accountByUserId = accountRepository.findAccountByUserId(userId);
        Account account = accountByUserId.get().changePw(passwordEncoder.encode(newPassword));

        accountRepository.save(account);
    }

    @Override
    public void checkPopUp(PopUpDto popUpDto) {
        log.info(popUpDto.getUserId());
        Optional<Member> memberByUserId = memberRepository.findMemberByUserId(popUpDto.getUserId());
        Member member = memberByUserId.get();

        Questionnaire questionnaire = Questionnaire.builder()
                .checkNum(popUpDto.getCheckNum())
                .checkTime(LocalDate.now())
                .member(member)
                .build();

            popRepository.save(questionnaire);
    }

    @Override
    public LocalDate recentCheckDate(MemberIdDto memberIdDto) {
        Optional<Questionnaire> oneByMemberId = popRepository.findTop1ByMemberIdOrderByCheckTimeDesc(memberIdDto.getMemberId());
        if (oneByMemberId.isEmpty()) {
            return null;
        } else {
            return oneByMemberId.get().getCheckTime();
        }
    }

    @Override
    public Member memberInfo(Long id) {
        Optional<Member> byId = memberRepository.findById(id);
        return byId.get();
    }

    @Override
    public void memberSetSns(HashDto hashDto) {
        Member member = memberRepository.findById(hashDto.getMemberId()).get();
         memberRepository.save(member.updateSns(hashDto.getSnsId(), hashDto.getSns()));
    }

    @Override
    public List<java.sql.Date> checkMoreFour(Long id) {
        List<java.sql.Date> localDateTimeList = popRepository.findQuestionnaireByMemberId(id);
        return localDateTimeList;
    }

    @Override
    public List<java.sql.Date> checkUnderFour(Long id) {
        return popRepository.findCheckTimeByMemberId(id);
    }


}
