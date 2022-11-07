package gachon.BLoom.board.service;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.board.dto.BoardUpdateDto;
import gachon.BLoom.board.dto.CommentDto;
import gachon.BLoom.entity.Board;
import gachon.BLoom.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {

    //게시글 등록
    void boardInsert(BoardDto boardDto);

    //게시글 삭제
    void boardDelete(Long id);

    //게시글 디테일
    Board boardDetail(Long id);

    //게시글 업데이트
    void update(BoardUpdateDto boardUpdateDto);

    //게시판 페이징
    Page<Board> BoardList(Pageable pageable);

    //전체 게시글
    List<Board> BoardAllList();


    //내 게시글 페이징
    List<Board> memberBoardList(Pageable pageable, Long id);

    //내 게시글 전체
    List<Board> memberBoardAllList(Long id);


    //댓글 등록
    void comment(CommentDto commentDto);

    //댓글 페이징
    List<Comment> commentPageList(Pageable pageable,Long id);

    //전체 댓글
    List<Comment> commentAllList(Long id);

}
