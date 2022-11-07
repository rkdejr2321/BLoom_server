package gachon.BLoom.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BoardCountDto {

    private int total;

    public BoardCountDto(int count) {
        this.total = count;
    }
}
