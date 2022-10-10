package gachon.BLoom.member.controller;

import gachon.BLoom.email.EmailServiceImpl;
import gachon.BLoom.exception.DuplicateMemberException;
import gachon.BLoom.exception.NotMatchPasswordException;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.dto.RegistMemberDto;
import gachon.BLoom.member.dto.SessionConstants;
import gachon.BLoom.oauth.CustomOAuth2UserService;
import gachon.BLoom.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;
    private final EmailServiceImpl emailService;
    private final HttpSession httpSession;
    private final CustomOAuth2UserService customOAuth2UserService;
    private OAuth2AuthorizedClientService authorizedClientService;
    //일반 사용자 회원가입
    @PostMapping("/member/signup")
    @ResponseBody
    public ResponseEntity<?> memberSignUp(RegistMemberDto registMemberDto) {
        try {
            memberService.registMember(registMemberDto);
            return new ResponseEntity("회원가입에 성공했습니다.",HttpStatus.OK);
        } catch (DuplicateMemberException e) {
            return new ResponseEntity<>("이미 가입된 이메일이 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        Object member = httpSession.getAttribute(SessionConstants.LOGIN_MEMBER);
        model.addAttribute("member", member);
        return "index";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> logIn(HttpServletRequest request, @RequestBody LoginMemberDto loginMemberDto) {
        try {
            // 로그인 시도
            LoginInfoDto loginInfoDto = memberService.login(loginMemberDto);

            //세션 유지
            HttpSession session = request.getSession();
            session.setAttribute(SessionConstants.LOGIN_MEMBER, loginInfoDto);

            log.info(session.getId());
            //로그인 성공
            return new ResponseEntity<>(loginInfoDto,HttpStatus.OK);
        } catch (NotMatchPasswordException e) {
            return new ResponseEntity<>("아이디와 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST );
        }
    }

/*    @GetMapping("/login/twitter")
    @ResponseBody
    public LoginMemberDto twitterLogin() {

    }*/



    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //현재 유지하고 있는 세션 가져오기
        HttpSession session = request.getSession(Boolean.parseBoolean(SessionConstants.LOGIN_MEMBER));

        //세션 제거
        session.invalidate();

        //첫 화면으로 리다이렉트
        return "redirect: /login";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @PostMapping("/mailConfirm")
    @ResponseBody
    public void verifyCodeSend(@RequestBody String email) throws Exception {
        emailService.sendConfirmMessage(email);
    }

    @PostMapping("/mailVerify")
    @ResponseBody
    public ResponseEntity<?> codeVerify(@RequestBody String code) throws Exception {
        if(emailService.ePw.equals(code)) {
            return new ResponseEntity<>("인증이 완료되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }


//    //전문의 회원가입
//    @PostMapping("/doctor/signup")
//    public ResponseEntity<?> doctorSignUp() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
