package gachon.BLoom.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnose_table")
@Getter
public class Diagnose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diagnose_result", nullable = false)
    private int result;

    @Column(name = "diagnose_date", nullable = false)
    private LocalDateTime diagnoseDate;

    @Column(name = "social_account", nullable = false)
    private String socialAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}

