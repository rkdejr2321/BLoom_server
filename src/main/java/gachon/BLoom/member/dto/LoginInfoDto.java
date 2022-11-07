package gachon.BLoom.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDto {

    private String token;
    private String memberId;
    private String username;
    private String email;
    private String userImage;

}
