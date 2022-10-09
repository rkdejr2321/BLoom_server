package gachon.BLoom.diagnose.service;

import java.util.List;

public interface DiagService {

    //진단하기
    void diagnoseToSns(String sns);

    //진단결과

    //나의 최근 진단 결과
    List<String> recentDiagnose(String email);
}
