package demo.account;

import demo.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserService userService;

    public AccountService(AccountRepository accountRepository,
                          UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public List<Account> getUserAccounts(){
        return Optional
                .ofNullable(userService.getAuthenticatedUser())
                .map(u->accountRepository.findAccountsByUsername(u.getUsername()))
                .orElse(Collections.emptyList());

    }
}
