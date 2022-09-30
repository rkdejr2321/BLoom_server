package gachon.BLoom.member.controller;

import gachon.BLoom.email.EmailService;
import gachon.BLoom.email.EmailServiceImpl;
import gachon.BLoom.exception.DuplicateMemberException;
import gachon.BLoom.exception.NotMatchPasswordException;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.dto.RegistMemberDto;
import gachon.BLoom.member.dto.SessionConstants;
import gachon.BLoom.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;
    private final EmailServiceImpl emailService;

    //일반 사용자 회원가입
    @PostMapping("/member/signup")
    public ResponseEntity<?> memberSignUp(RegistMemberDto registMemberDto) {
        try {
            memberService.registMember(registMemberDto);
            return new ResponseEntity("회원가입에 성공했습니다.",HttpStatus.OK);
        } catch (DuplicateMemberException e) {
            return new ResponseEntity<>("이미 가입된 이메일이 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(HttpServletRequest request, @RequestBody LoginMemberDto loginMemberDto) {
        try {

            LoginInfoDto loginInfoDto = memberService.login(loginMemberDto);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConstants.LOGIN_MEMBER, loginMemberDto);
            return new ResponseEntity<>(loginInfoDto,HttpStatus.OK);
        } catch (NotMatchPasswordException e) {
            return new ResponseEntity<>("아이디와 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST );
        }
    }

    @PostMapping("/mailConfirm")
    public void verifyCodeSend(@RequestBody String email) throws Exception {
        emailService.sendConfirmMessage(email);
    }


//    //전문의 회원가입
//    @PostMapping("/doctor/signup")
//    public ResponseEntity<?> doctorSignUp() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}