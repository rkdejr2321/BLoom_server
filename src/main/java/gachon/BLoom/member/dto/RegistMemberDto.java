package gachon.BLoom.member.dto;

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
}
