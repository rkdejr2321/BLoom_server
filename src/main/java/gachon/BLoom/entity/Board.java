package gachon.BLoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;


    @Enumerated(EnumType.STRING)
    @Column(name = "delete")
    private Delete delete;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insertDate = LocalDateTime.now();

    @Column(name = "delete_time", nullable = true)
    private LocalDateTime deleteDate;

    @Column(name = "update_date", nullable = true)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_id")
    private Member member;


    public Board delete() {
        this.delete = Delete.Y;
        this.deleteDate = LocalDateTime.now();
        return this;
    }
}
