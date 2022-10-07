package gachon.BLoom.member.repository;

import gachon.BLoom.entity.Account;
import gachon.BLoom.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Account> findAccountByEmail(String email);
    Optional<Member> findMemberByEmail(String email);
    Member save(Member member);

}
