package gachon.BLoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnose")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "checking")
    private boolean checking;


    @ManyToOne()
    @JoinColumn(name = "member_id")
    @JsonManagedReference
    private Member member;
}

