package gachon.BLoom.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gachon.BLoom.board.dto.BoardUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;


    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();


    @Column(name = "updated", nullable = true)
    private LocalDateTime updated;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment;


    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    public Board update(BoardUpdateDto boardUpdateDto) {
        this.title = boardUpdateDto.getTitle();
        this.category = boardUpdateDto.getCategory();
        this.content = boardUpdateDto.getContent();
        this.updated = LocalDateTime.now();
        return this;
    }
}
