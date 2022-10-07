package gachon.BLoom.member.dto;

import gachon.BLoom.entity.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {

    private String username;
    private String email;
    private String userImage;

    public SessionMember(Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.userImage = member.getUserImage();
    }
}
