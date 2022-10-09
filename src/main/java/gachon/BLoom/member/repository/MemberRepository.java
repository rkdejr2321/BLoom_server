package gachon.BLoom.member.repository;

import gachon.BLoom.entity.Account;
import gachon.BLoom.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByUsername(String username);

    Member save(Member member);

}
