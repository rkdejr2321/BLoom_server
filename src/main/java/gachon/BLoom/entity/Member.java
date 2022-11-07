package gachon.BLoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gachon.BLoom.member.dto.RegistMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "user_image", nullable = true)
    private String userImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "provider",nullable = false)
    private String provider;

    @Column(name = "sns_id")
    private String snsId;

    @Column(name = "checking")
    @ColumnDefault("false")
    private boolean checking;

    @JsonBackReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Diagnose> diagnoseList;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Board> board;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Comment> comment;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Questionnaire> questionnaires;

    public Member update(String username, String userImage, String email) {
        this.username = username;
        this.userImage = userImage;
        this.email = email;
        return this;
    }

    public Member updateSns(String snsId, String provider) {
        this.snsId = snsId;
        this.provider = provider;
        return this;
    }

    public Member updateCheck() {
        if(this.checking == false) {
            this.checking = true;
        } else {
            this.checking = false;
        }
        return this;
    }
}
