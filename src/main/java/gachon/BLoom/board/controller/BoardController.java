package gachon.BLoom.board.controller;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.board.service.BoardServiceImpl;
import gachon.BLoom.entity.Board;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.SessionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;

    @PostMapping("/board/write")
    @ResponseBody
    public ResponseEntity<?> write(HttpSession session, BoardDto boardDto) throws Exception{

        LoginInfoDto loginMember = (LoginInfoDto) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        boardService.boardInsert(loginMember,boardDto);
        return new ResponseEntity<>("등록이 완료 되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/board")
    @ResponseBody
    public List<Board> boardList() {
        List<Board> boardList = boardService.getBoardList();
        return boardList;
    }

    @PostMapping("/board/delete")
    public ResponseEntity<?> delete(@RequestParam Long id, HttpSession session) {
        try {
            LoginInfoDto loginMember = (LoginInfoDto) session.getAttribute(SessionConstants.LOGIN_MEMBER);
            boardService.boardDelete(loginMember, id);
            return new ResponseEntity<>("삭제가 완료 되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

    }
}
