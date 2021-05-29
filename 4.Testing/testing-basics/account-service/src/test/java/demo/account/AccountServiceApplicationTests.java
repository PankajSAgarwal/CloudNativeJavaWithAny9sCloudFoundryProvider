package demo.account;

import demo.account.Account;
import demo.account.AccountNumber;
import demo.account.AccountRepository;
import demo.account.AccountService;
import demo.user.User;
import demo.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class AccountServiceApplicationTests {

	@MockBean
	private UserService userService;

	@MockBean
	private AccountRepository accountRepository;

	private AccountService accountService;

	@BeforeEach
	public void before() {
		accountService = new AccountService(accountRepository, userService);
	}
	@Test
	public void getUserAccountsReturnsSingleAccount() throws Exception {
		given(this.accountRepository
				.findAccountsByUsername("user"))
				.willReturn(Collections
						.singletonList(
								new Account("user",
										new AccountNumber("123456789"))));
		given(this.userService
				.getAuthenticatedUser())
				.willReturn(new User(0L, "user", "John", "Doe"));

		List<Account> actual = accountService.getUserAccounts();
		System.out.println();
		assertThat(actual).size().isEqualTo(1);
		assertThat(actual.get(0)
				.getAccountNumber())
				.isEqualTo(new AccountNumber("123456789"));

	}


}
