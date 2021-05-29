package demo.account;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {
    private static final AccountNumber ACCOUNT_NUMBER =
            new AccountNumber("098765432");

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findUserAccountsShouldReturnAccounts() throws Exception{
        this.entityManager.persist(new Account("jack", ACCOUNT_NUMBER));
        List<Account> accounts = this.accountRepository.findAccountsByUsername("jack");
        assertThat(accounts.size()).isEqualTo(1);
        Account actual = accounts.get(0);
        assertThat(actual.getAccountNumber()).isEqualTo(ACCOUNT_NUMBER);
        assertThat(actual.getUsername()).isEqualTo("jack");
    }

    @Test
    public void findAccountShouldReturnAccount() throws Exception{
        this.entityManager.persist(new Account("jill",ACCOUNT_NUMBER));
        Account account = this.accountRepository.findAccountByAccountNumber(ACCOUNT_NUMBER);
        assertThat(account).isNotNull();
        assertThat(account.getAccountNumber()).isEqualTo(ACCOUNT_NUMBER);
    }

    @Test
    public void findAccountShouldReturnNull() throws Exception{
        this.entityManager.persist(new Account("jack",ACCOUNT_NUMBER));
        Account account = this.accountRepository.findAccountByAccountNumber(new AccountNumber("000000000"));
        assertThat(account).isNull();
    }


}