package gachon.BLoom.board.controller;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.board.service.BoardService;
import gachon.BLoom.entity.Member;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.dto.SessionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/write")
    @ResponseBody
    public ResponseEntity<?> write(HttpSession session, BoardDto boardDto) throws Exception{

        LoginInfoDto loginMember = (LoginInfoDto) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        boardService.boardCreate(loginMember,boardDto);
        return new ResponseEntity<>("등록이 완료 되었습니다.", HttpStatus.OK);
    }
}
