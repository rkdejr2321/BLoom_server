package gachon.BLoom.board.service;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.board.dto.BoardUpdateDto;
import gachon.BLoom.board.dto.CommentDto;
import gachon.BLoom.board.repository.BoardRepository;
import gachon.BLoom.board.repository.CommentRepository;
import gachon.BLoom.entity.Board;
import gachon.BLoom.entity.Comment;
import gachon.BLoom.entity.Member;
import gachon.BLoom.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final CommentRepository commentRepository;

    @Override
    public void boardInsert(BoardDto boardDto) {

        Optional<Member> memberByEmail = memberRepository.findById(boardDto.getMemberId());

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .category(boardDto.getCategory())
                .created(LocalDateTime.now())
                .updated(null)
                .member(memberByEmail.get())
                .build();

        boardRepository.save(board);
    }

    @Override
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }


    //페이징
    @Override
    public List<Board> memberBoardList(Pageable pageable, Long id) {
        return boardRepository.findBoardByMemberId(pageable, id).getContent();
    }


    //전체
    @Override
    public List<Board> memberBoardAllList(Long id) {
        return boardRepository.findMyAllBoardByMemberId(id);
    }



    //페이징
    @Override
    public Page<Board> BoardList(Pageable pageable) {
        Page<Board> page = boardRepository.findAll(pageable);
        return page;
    }

    @Override
    public List<Board> BoardAllList() {
        return boardRepository.findAll();
    }


    @Override
    public Board boardDetail(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        return board.get();
    }

    @Override
    public void update(BoardUpdateDto boardUpdateDto) {
        Optional<Board> byId = boardRepository.findById(boardUpdateDto.getId());
        Board board = byId.get();

        Board update = board.update(boardUpdateDto);
        boardRepository.save(update);
    }

    @Override
    public void comment(CommentDto commentDto) {
        log.info(commentDto.getContent());
        log.info(commentDto.getMember_id().toString());
        log.info(commentDto.getBoard_id().toString());
        Optional<Member> byId = memberRepository.findById(commentDto.getMember_id());
        Member member = byId.get();

        Optional<Board> byId1 = boardRepository.findById(commentDto.getBoard_id());
        Board board = byId1.get();


        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .created(LocalDateTime.now())
                .updated(null)
                .member(member)
                .board(board)
                .build();

        commentRepository.save(comment);
    }


    //페이징
    @Override
    public List<Comment> commentPageList(Pageable pageable,Long id) {
        List<Comment> allByBoardId = commentRepository.findCommentsByBoardId(pageable, id).getContent();
        return allByBoardId;
    }

    //전체
    @Override
    public List<Comment> commentAllList(Long id) {
        return commentRepository.findAllByBoardId(id);
    }
}
