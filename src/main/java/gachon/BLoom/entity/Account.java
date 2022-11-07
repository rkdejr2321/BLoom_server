package gachon.BLoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinColumn(name = "member_id")
    private Member member;

    public Account(Member member, String userId, String password) {
        this.member = member;
        this.userId = userId;
        this.password = password;
    }

    public Account changePw(String pw) {
        this.password = pw;
        return this;
    }
}
