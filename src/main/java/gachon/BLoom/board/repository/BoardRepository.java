package gachon.BLoom.board.repository;

import gachon.BLoom.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board save(Board board);
}
