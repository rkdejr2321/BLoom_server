package gachon.BLoom.board.service;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.board.repository.BoardRepository;
import gachon.BLoom.entity.Board;
import gachon.BLoom.entity.Member;
import gachon.BLoom.entity.Delete;
import gachon.BLoom.member.dto.LoginInfoDto;
import gachon.BLoom.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public void boardInsert(LoginInfoDto member, BoardDto boardDto) {

        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(member.getEmail());

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(member.getUsername())
                .insertDate(LocalDateTime.now())
                .member(memberByEmail.get())
                .delete(Delete.N)
                .updateDate(null)
                .build();

        boardRepository.save(board);
    }

    @Override
    public void boardDelete(LoginInfoDto member, Long id) {
        Board board = boardRepository.findById(id).get();

        if (board.getMember().getUsername() == member.getUsername()) {
            board.delete();
            boardRepository.save(board);
        }
    }

    public List<Board> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        return boardList;
    }

}
