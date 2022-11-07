package gachon.BLoom.diagnose.service;

import gachon.BLoom.diagnose.dto.*;
import gachon.BLoom.entity.Diagnose;

import java.util.HashSet;
import java.util.List;

public interface DiagService {

    //진단하기
    DiagResultDto diagnose(Long memberId, FeedDto feed);

    FeedDto scrapping(SnsDto snsDto);


    //나의 최근 진단 결과
    List<Diagnose> recentDiagnose(Long id);

    //해쉬값 생성
    int snsIdHash(String snsId);

    boolean checkHash(HashDto hashDto);

    void checkingDiagnose(Long id);

}
