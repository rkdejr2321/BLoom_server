package gachon.BLoom.member.service;

import gachon.BLoom.diagnose.dto.HashDto;
import gachon.BLoom.entity.Member;
import gachon.BLoom.member.dto.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface MemberService {
    void registMember(RegistMemberDto registMemberDto);

    LoginInfoDto login(LoginMemberDto loginMemberDto);

    Member findEmail(String email);

    void changePw(String userId, String newPassowrd);

    void checkPopUp(PopUpDto popUpDto);

    LocalDate recentCheckDate(MemberIdDto memberIdDto);

    Member memberInfo(Long id);

    void memberSetSns(HashDto hashDto);

    List<Date> checkMoreFour(Long id);

    List<Date> checkUnderFour(Long id);

}
