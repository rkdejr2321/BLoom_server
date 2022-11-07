package gachon.BLoom.diagnose.repository;

import gachon.BLoom.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiagRepository extends JpaRepository<Diagnose, Long> {

    @Query()
    List<Diagnose> findTop4ByMember_IdOrderByDiagnoseDateDesc(Long id);


    @Query(value = "select MAX(d.DIAGNOSE_DATE) from Diagnose d",nativeQuery = true)
    LocalDateTime findDiagnoseDateByMemberId(Long id);
}
