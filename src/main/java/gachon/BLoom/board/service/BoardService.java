package gachon.BLoom.board.service;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.board.repository.BoardRepository;
import gachon.BLoom.entity.Board;
import gachon.BLoom.entity.Member;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.dto.LoginMemberDto;
import gachon.BLoom.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void boardCreate(LoginInfoDto member, BoardDto boardDto) {

        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(member.getEmail());

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(member.getUsername())
                .insertDate(LocalDateTime.now())
                .member(memberByEmail.get())
                .deleteYn("N")
                .updateDate(null)
                .build();

        boardRepository.save(board);
    }
}
