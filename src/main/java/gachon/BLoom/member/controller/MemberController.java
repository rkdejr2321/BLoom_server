package gachon.BLoom.member.controller;

import gachon.BLoom.email.EmailServiceImpl;
import gachon.BLoom.entity.Member;
import gachon.BLoom.exception.DuplicateMemberException;
import gachon.BLoom.exception.NotMatchPasswordException;
import gachon.BLoom.member.dto.*;
import gachon.BLoom.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;
    private final EmailServiceImpl emailService;
    private final HttpSession httpSession;

    /*
    * 회원가입
    * 완료
    * */
    @PostMapping("/member/signup")
    @ResponseBody
    public ResponseEntity<?> memberSignUp(@RequestBody RegistMemberDto registMemberDto) {
        try {
            log.info(registMemberDto.getUserId());
            memberService.registMember(registMemberDto);
            return new ResponseEntity("회원가입에 성공했습니다.",HttpStatus.OK);
        } catch (DuplicateMemberException e) {
            return new ResponseEntity<>("이미 가입된 이메일이 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 로그인
    * 완료
    * */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> logIn(HttpServletRequest request, @RequestBody LoginMemberDto loginMemberDto) {
        try {
            // 로그인 시도
            LoginInfoDto loginInfoDto = memberService.login(loginMemberDto);

            //세션 유지
            HttpSession session = request.getSession();
            session.setAttribute(SessionConstants.LOGIN_MEMBER, loginInfoDto);

            LoginInfoDto member = (LoginInfoDto) session.getAttribute(SessionConstants.LOGIN_MEMBER);
            log.info(member.getUsername());
            //로그인 성공
            return new ResponseEntity<>(loginInfoDto,HttpStatus.OK);
        } catch (NotMatchPasswordException e) {
            return new ResponseEntity<>("아이디와 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST );
        }
    }



    /*
    * 이메일 코드 전송
    * 완료
    * */
    @PostMapping("/mailConfirm")
    @ResponseBody
    public void verifyCodeSend(@RequestBody String email) throws Exception {
        log.info(email);
        emailService.sendConfirmMessage(email);
    }


    /*
    * 이메일 인증 코드 일치 체크
    * 완료
    * */
    @PostMapping("/mailVerify")
    @ResponseBody
    public ResponseEntity<?> codeVerify(@RequestBody VerifyDto verifyDto) throws Exception {
        log.info(verifyDto.getCode());
        if(emailService.ePw.equals(verifyDto.getCode())) {
            Member member = memberService.findEmail(verifyDto.getEmail());
            if(member == null) {
                return new ResponseEntity<>("이메일로 가입된 아이디가 존재하지 않습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(member.getUserId(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 비밀번호 변경 가능 체크
    * 완료
    * */
    @PostMapping("/checkChange")
    @ResponseBody
    public ResponseEntity<?> checkChange(@RequestBody CheckChangeDto checkChangeDto) throws Exception {
        log.info(checkChangeDto.getCode());
        if(emailService.ePw.equals(checkChangeDto.getCode())) {
            Member member = memberService.findEmail(checkChangeDto.getEmail());
            if(member.getUserId().equals(checkChangeDto.getUserId())) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 비밀번호 변경
    * 완료
    * */
    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<?> changePw(@RequestBody ChangePwDto changeDto) {
        memberService.changePw(changeDto.getUserId(), changeDto.getNewPassword());
        return new ResponseEntity<>("변경이 완료되었습니다.", HttpStatus.OK);
    }

    /*
    * 사용자 정보
    * 완료
    * */
    @GetMapping("/user/{member_id}")
    @ResponseBody
    public Member memberInfo(@PathVariable Long member_id) {
        return memberService.memberInfo(member_id);
    }

    /*
    * 문진표 작성
    * 완료
    * */
    @PostMapping("/popup")
    @ResponseBody
    public ResponseEntity<?> popUp(@RequestBody PopUpDto popUpDto) {
        memberService.checkPopUp(popUpDto);
        return new ResponseEntity<>("저장이 완료되었습니다.", HttpStatus.OK);
    }


    /*
    * 마지막으로 문진표 작성 날짜
    * 완료
    * GET
    * */
    @PostMapping("/checkdate")
    @ResponseBody
    public LocalDate checkDate(@RequestBody MemberIdDto memberIdDto) {
        log.info("member");
        LocalDate localDateTime = memberService.recentCheckDate(memberIdDto);
        if (localDateTime == null) {
            LocalDate noDate = LocalDate.of(1000,1,1);
            return noDate;
        }
        return localDateTime;
    }

    @GetMapping("/moreFour/{id}")
    @ResponseBody
    private List<Date> moreFour(@PathVariable(value = "id") Long id) {
        return memberService.checkMoreFour(id);
    }

    @GetMapping("/lessFour/{id}")
    @ResponseBody
    private List<Date> lessFour(@PathVariable(value = "id") Long id) {
        return memberService.checkUnderFour(id);
    }

}
