package gachon.BLoom.member.repository;

import gachon.BLoom.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PopRepository extends JpaRepository<Questionnaire, Long> {

    Questionnaire save(Questionnaire questionnaire);

    Optional<Questionnaire> findTop1ByMemberIdOrderByCheckTimeDesc(Long id);


    @Query(value = "select q.check_time from Questionnaire q where q.check_num >= 4", nativeQuery = true)
    List<Date> findQuestionnaireByMemberId(Long id);

    @Query(value = "select q.check_time from Questionnaire q where q.check_num < 4", nativeQuery = true)
    List<Date> findCheckTimeByMemberId(Long id);

    void deleteById(Long id);
}
