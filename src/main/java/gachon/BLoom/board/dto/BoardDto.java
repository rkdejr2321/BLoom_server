package gachon.BLoom.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime insertTime;

}
