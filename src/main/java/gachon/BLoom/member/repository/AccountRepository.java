package gachon.BLoom.member.repository;

import gachon.BLoom.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUserId(String userId);

    Account save(Account account);
}
