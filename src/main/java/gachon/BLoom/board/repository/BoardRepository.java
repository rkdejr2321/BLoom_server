package gachon.BLoom.board.repository;

import gachon.BLoom.board.dto.BoardDto;
import gachon.BLoom.entity.Board;
import gachon.BLoom.member.dto.LoginInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board save(Board board);

    //삭제되지 않은 게시글만 가져옴
    @Query(value = "select * from Board b where b.delete = 'N'", nativeQuery = true)
    List<Board> findAll();

    Optional<Board> findById(Long id);
}
