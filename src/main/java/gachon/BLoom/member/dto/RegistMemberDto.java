package gachon.BLoom.member.dto;

import gachon.BLoom.entity.Account;
import gachon.BLoom.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistMemberDto {

    private String email;
    private String username;
    private String password;
    private MultipartFile userImage;

    public Account toEntity(Account account, Member member) {
        return new Account(member, account.getEmail(), account.getPassword());
    }
}
