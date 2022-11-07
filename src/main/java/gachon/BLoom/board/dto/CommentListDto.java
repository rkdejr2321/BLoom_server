package gachon.BLoom.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListDto {

    private Long board_id;
    private int page_number;
    private int page_size;
}
