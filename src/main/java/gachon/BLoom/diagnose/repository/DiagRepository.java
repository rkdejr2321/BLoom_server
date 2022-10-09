package gachon.BLoom.diagnose.repository;

import gachon.BLoom.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagRepository extends JpaRepository<Diagnose, Long> {


}
