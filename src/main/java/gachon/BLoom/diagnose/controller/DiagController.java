package gachon.BLoom.diagnose.controller;

import gachon.BLoom.diagnose.dto.*;
import gachon.BLoom.diagnose.service.DiagServiceImpl;
import gachon.BLoom.entity.Diagnose;
import gachon.BLoom.entity.Member;
import gachon.BLoom.member.dto.MemberIdDto;
import gachon.BLoom.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class DiagController {

    private final DiagServiceImpl diagService;
    private final MemberServiceImpl memberService;

    /*
    * 진단하기
    * 완료
    * */
    @PostMapping("/diagnose")
    @ResponseBody
    public ResponseEntity<?> diagnose(@RequestBody SnsDto snsDto) {
        Member member = memberService.memberInfo(snsDto.getMemberId());
        if (member.getProvider().equals("bloom")) {
            return new ResponseEntity<>("sns가 등록되어 있지 않습니다.", HttpStatus.BAD_REQUEST);
        } else {
            diagService.checkingDiagnose(snsDto.getMemberId());
            FeedDto scrapDto = diagService.scrapping(snsDto);
            DiagResultDto result = diagService.diagnose(snsDto.getMemberId(), scrapDto);
            diagService.checkingDiagnose(snsDto.getMemberId());
            return new ResponseEntity<>(result.getScore(),HttpStatus.OK);
        }
    }


    /*
    * 최근 4건 진단 결과
    * 완료
    * GET
    * */
    @PostMapping("/member/recent/result")
    @ResponseBody
    public List<Diagnose> recentDiag(@RequestBody MemberIdDto memberIdDto) {
        List<Diagnose> diagnoses = diagService.recentDiagnose(memberIdDto.getMemberId());

        return diagnoses;
    }


    /*
    * 해쉬값 생성
    * 완료
    * */
    @PostMapping("/hashcode")
    @ResponseBody
    public int getHash(@RequestBody String snsId) {
        int i = diagService.snsIdHash(snsId);
        log.info("생성한 hash값={}",i);
        return i;
    }

    /*
    * 해시 값 일치 체크
    * 완료
    * */
    @PostMapping("/check/hash")
    @ResponseBody
    public ResponseEntity<?> checkHash(@RequestBody HashDto hashDto) {
        log.info("해시값={}",hashDto.getHashCode());
        boolean bool = diagService.checkHash(hashDto);
        if (bool) {
            memberService.memberSetSns(hashDto);
            return new ResponseEntity<>("Sns 등록이 완료되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("아이디와 해쉬값이 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * 점수에 따른 멘트
    *
    * */
    @PostMapping("/diagnose/comment")
    @ResponseBody
    public String diagComment(@RequestBody String diagScore) {
        String comment = null;
        int i = Integer.parseInt(diagScore);
        if(i  == -1) {
            comment = "검사를 먼저 진행해주세요!";
        } else if(i >= 0 && i <= 10) {
            comment = "건강한 삶을 살고 계시네요. 앞으로도 당신의 길을 응원합니다.";
        } else if (i > 10 && i <=30 ) {
            comment = "가끔씩 무언가 답답한 경우가 있으신가요? 산책이나 활동을 통해 기분 전환을 해보시는게 어떠실까요?";
        } else if (i > 30 && i <= 60) {
            comment = "전문가의 도움이 필요한 상태는 아니지만 주의할 필요가 있습니다. 주변 사람들과 마음을 열고 진솔한 대화를 해보는건 어떨까요?";
        } else if (i > 60 && i <= 90) {
            comment = "우울증 위험이 높습니다. 심각한 수준은 아니지만 전문가와의 상담이 당신에게 도움이 될 수 있습니다.";
        } else {
            comment = "지금 당장 전문가와 상담받는 것이 좋을 것 같습니다. 근처 상담센터 기능을 참고하여 가까운 병원에 방문에 방문해보세요.";
        }

        return comment;
    }

    @PostMapping("/diagnose/check")
    @ResponseBody
    public boolean isDiagnose(@RequestBody MemberIdDto memberIdDto) {
        Member member = memberService.memberInfo(memberIdDto.getMemberId());
        return member.isChecking();
    }
}
