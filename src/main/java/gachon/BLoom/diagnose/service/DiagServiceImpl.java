package gachon.BLoom.diagnose.service;

import gachon.BLoom.diagnose.dto.*;
import gachon.BLoom.diagnose.repository.DiagRepository;
import gachon.BLoom.entity.Diagnose;
import gachon.BLoom.entity.Member;
import gachon.BLoom.member.repository.MemberRepository;
import gachon.BLoom.member.repository.PopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DiagServiceImpl implements DiagService {

    private DiagRepository diagRepository;

    private PopRepository popRepository;
    private MemberRepository memberRepository;

    public DiagServiceImpl(DiagRepository diagRepository, PopRepository popRepository, MemberRepository memberRepository) {
        this.diagRepository = diagRepository;
        this.popRepository = popRepository;
        this.memberRepository = memberRepository;
    }

    private WebClient diagWebClient = WebClient.builder().baseUrl("http://ec2-13-209-31-58.ap-northeast-2.compute.amazonaws.com/depression").build();


    private WebClient scrappingWebClient = WebClient.builder().baseUrl("https://bloom-scraping.herokuapp.com/scraping").build();

    private WebClient snsVerifyWebClient = WebClient.builder().baseUrl("https://bloom-scraping.herokuapp.com/verify").build();


    public FeedDto scrapping(SnsDto snsDto) {

        Optional<Member> member = memberRepository.findById(snsDto.getMemberId());
        SnsIdDto snsIdDto = new SnsIdDto(member.get().getSnsId());
        FeedDto feedDto = scrappingWebClient.method(HttpMethod.GET)
                .uri("/"+member.get().getProvider())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(snsIdDto)
                .retrieve()
                .bodyToMono(FeedDto.class)
                .block();

        feedDto.setSnsName(member.get().getProvider());
        return feedDto;
    }

    public DiagResultDto diagnose(Long memberId, FeedDto feed) {
        //진단
        int result = diagWebClient.method(HttpMethod.POST)
                .uri("/")
                .bodyValue(feed)
                .retrieve()
                .bodyToMono(DiagResultDto.class)
                .block().getScore();

        log.info(memberId.toString());
        //저장
        Optional<Member> byId = memberRepository.findById(memberId);
        Member member = byId.get();

        Diagnose diagnose = Diagnose.builder()
                .checking(true)
                .result(result)
                .socialAccount(feed.getSnsName())
                .diagnoseDate(LocalDateTime.now())
                .member(member)
                .build();

        diagRepository.save(diagnose);
        DiagResultDto diagResult = getResult(result);
        return diagResult;

    }

    private DiagResultDto getResult(int result) {
        String comment;
        if(result >= 0 && result <= 10) {
            comment = "건강한 삶을 살고 계시네요. 앞으로도 당신의 길을 응원합니다.";
        } else if (result > 10 && result <=30 ) {
            comment = "가끔씩 무언가 답답한 경우가 있으신가요? 산책이나 활동을 통해 기분 전환을 해보시는게 어떠실까요?";
        } else if (result > 30 && result <= 60) {
            comment = "전문가의 도움이 필요한 상태는 아니지만 주의할 필요가 있습니다. 주변 사람들과 마음을 열고 진솔한 대화를 해보는건 어떨까요?";
        } else if (result > 60 && result <= 90) {
            comment = "우울증 위험이 높습니다. 심각한 수준은 아니지만 전문가와의 상담이 당신에게 도움이 될 수 있습니다.";
        } else {
            comment = "지금 당장 전문가와 상담받는 것이 좋을 것 같습니다. 근처 상담센터 기능을 참고하여 가까운 병원에 방문에 방문해보세요.";
        }

        return new DiagResultDto(result, comment);
    }

    @Override
    public List<Diagnose> recentDiagnose(Long id) {
        List<Diagnose> recentDiag = diagRepository.findTop4ByMember_IdOrderByDiagnoseDateDesc(id);
        log.info("최근 결과 사이즈={}",recentDiag.size());
        return recentDiag;
    }

    @Override
    public int snsIdHash(String snsId) {
        return snsId.hashCode();
    }

    @Override
    public boolean checkHash(HashDto hashDto) {
        ArrayList block = snsVerifyWebClient.method(HttpMethod.GET)
                .uri("/" + hashDto.getSns())
                .bodyValue(new SnsIdDto(hashDto.getSnsId()))
                .retrieve()
                .bodyToMono(ArrayList.class)
                .block();

        log.info("해시 리스트={}",block.get(0));
        log.info("sns Id 해시 값={}", hashDto.getSnsId().hashCode());
        if (Integer.parseInt(block.get(0).toString()) == hashDto.getSnsId().hashCode()) {
            Member member = memberRepository.findById(hashDto.getMemberId()).get();
            Member member1 = member.updateSns(hashDto.getSnsId(), hashDto.getSns());
            memberRepository.save(member1);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void checkingDiagnose(Long id) {
        Member member = memberRepository.findById(id).get();
        Member member1 = member.updateCheck();

        memberRepository.save(member1);
    }

}