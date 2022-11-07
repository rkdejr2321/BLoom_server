package gachon.BLoom.board.repository;

import gachon.BLoom.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board save(Board board);

    void deleteById(Long id);

    //게시글 페이징
    Page<Board> findAll(Pageable pageable);

    //전체 페이지
    List<Board> findAll();

    //내 게시물 페이징
    Page<Board> findBoardByMemberId(Pageable pageable, Long id);

    List<Board> findMyAllBoardByMemberId(Long id);


    Optional<Board> findById(Long id);


}
