package gachon.BLoom.member.service;

import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.dto.RegistMemberDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    void registMember(RegistMemberDto registMemberDto);

    void memberInfoUpdate(RegistMemberDto registMemberDto);
    LoginInfoDto login(LoginMemberDto loginMemberDto);

}
