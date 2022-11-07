package gachon.BLoom.board.dto;

import gachon.BLoom.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class BoardResponseDto {

    private String id;
    private String category;
    private String content;
    private String title;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Member member;

}
