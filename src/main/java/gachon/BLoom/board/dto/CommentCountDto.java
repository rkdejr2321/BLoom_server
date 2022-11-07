package gachon.BLoom.board.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentCountDto {

    private int total;

    public CommentCountDto(int total) {
        this.total = total;
    }
}
