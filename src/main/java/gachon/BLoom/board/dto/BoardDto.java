package gachon.BLoom.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    private String token;
    private Long memberId;
    private String category;
    private String title;
    private String content;
}
