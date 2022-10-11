package gachon.BLoom.board.service;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.member.dto.LoginInfoDto;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {

    void boardInsert(LoginInfoDto loginInfoDto, BoardDto boardDto);
    void boardDelete(LoginInfoDto member, Long id);
}
