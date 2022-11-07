package gachon.BLoom.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateDto {

    private Long id;
    private String title;
    private String content;
    private String category;
}
