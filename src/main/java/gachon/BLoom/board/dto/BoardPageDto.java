package gachon.BLoom.board.dto;

import gachon.BLoom.entity.Board;
import lombok.Data;

import java.util.List;

@Data
public class BoardPageDto {

    private List<Board> boardList;
    private String pageSize;
    private String pageNumber;

    public BoardPageDto(List<Board> boardList, String pageNumber, String pageSize) {
        this.boardList = boardList;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
