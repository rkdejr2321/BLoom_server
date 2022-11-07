package gachon.BLoom.board.controller;

import gachon.BLoom.board.dto.*;
import gachon.BLoom.board.service.BoardServiceImpl;
import gachon.BLoom.entity.Board;
import gachon.BLoom.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardServiceImpl boardService;

    /*
    * 게시글 쓰기
    * 완료
    * */
    @PostMapping("/board/write")
    public ResponseEntity<?> write(HttpSession session, BoardDto boardDto, Authentication authentication) throws Exception{

        boardService.boardInsert(boardDto);
        return new ResponseEntity<>("등록이 완료 되었습니다.", HttpStatus.OK);
    }

    /*
    * 페이징
    * 완료
    * */
    @GetMapping("/board/list")
    public List<Board> boardList(@PageableDefault(size = 10, sort = "created",direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "page_number", required = false) Long page_number) throws Exception
    {
        List<Board> boards = null;

        if (page_number == null) {
            boards = boardService.BoardAllList();
        } else {
            boards = boardService.BoardList(pageable).getContent();
        }
        return  boards;
    }

    /*
    * 게시글 전체 갯수
    * 완료
    * */
    @GetMapping("/board/count")
    public BoardCountDto boardCount() {
        List<Board> boardList = boardService.BoardAllList();
        int count = boardList.size();
        BoardCountDto boardCountDto = new BoardCountDto(count);
        return boardCountDto;
    }

    /*
    * 게시글 삭제
    * 완료
    * */
    @DeleteMapping("/board/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            boardService.boardDelete(id);
            return new ResponseEntity<>("삭제가 완료 되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    /*
    * 댓글 등록
    * 완료
    * */
    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody CommentDto commentDto) {
        boardService.comment(commentDto);

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }


    /*
    * 댓글 내용
    * 완료
    * */
    @GetMapping("/comment/list")
    @ResponseBody
    public List<Comment> comments(@PageableDefault(size = 5, sort = "created",direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(value = "page_number", required = false) Long page_number,
                                  @RequestParam(value = "board_id") Long board_id)
    {
        List<Comment> comments = null;

        if (page_number == null) {
           comments =  boardService.commentAllList(board_id);
        } else {
            comments = boardService.commentPageList(pageable, board_id);
        }
        return comments;
    }


    /*
    * 댓글 갯수
    * 완료
    * */
    @GetMapping("/comment/count")
    @ResponseBody
    public CommentCountDto commentCount(@RequestParam(value = "board_id", required = false)Long boardId) {
        List<Comment> comments = boardService.commentAllList(boardId);
        int count = comments.size();

        CommentCountDto commentCountDto = new CommentCountDto(count);
        return commentCountDto;
    }

    /*
    * 게시판 수정
    * 완료
    * */
    @PutMapping("/board")
    @ResponseBody
    public ResponseEntity<?> update(BoardUpdateDto boardUpdateDto) {

        log.info("수정 데이터 제목={}", boardUpdateDto.getTitle());
        boardService.update(boardUpdateDto);

        return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.OK);
    }

    /*
    * 내 게시글 보기
    * 완료
    * */
    @GetMapping("/board/user/list")
    public List<Board> myBoard(@PageableDefault(size = 10, sort = "created",direction = Sort.Direction.DESC) Pageable pageable,
                                     @RequestParam(value = "page_number", required = false) Long page_number,
                                     @RequestParam(value = "member_id") Long member_id)
    {
        List<Board> boards = null;

        if (page_number == null) {
            boards = boardService.memberBoardAllList(member_id);
        } else {
            boards = boardService.memberBoardList(pageable, member_id);
        }
        return boards;
    }


    /*
    * 내 게시글 갯수
    * 완료
    * */
    @GetMapping("/board/user/count/{member_id}")
    @ResponseBody
    public BoardCountDto memberBoardCount(@PathVariable Long member_id) {
        List<Board> boards = boardService.memberBoardAllList(member_id);
        int total = boards.size();

        BoardCountDto boardCountDto = new BoardCountDto(total);

        return boardCountDto;
    }


    /*
    * 게시글 보기
    * 완료
    * */
    @GetMapping("/board/{id}")
    public Board BoardDetail(@PathVariable Long id) {

        Board board = boardService.boardDetail(id);
        return board;
    }
}
