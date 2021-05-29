package demo.account;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.Assert;

import java.util.Objects;

public class AccountNumber {
    private String accountNumber;

    public AccountNumber(String accountNumber) {
        Assert.notNull(accountNumber,
                "Account number must not be null");

        Assert.isTrue(accountNumber.length()==9,
                "Account number must be exactly 9 characters");

        this.accountNumber = accountNumber;
    }

    @JsonValue
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(!(o instanceof AccountNumber))
            return false;

        AccountNumber that = (AccountNumber) o;

        return getAccountNumber() != null ?
                getAccountNumber().equals(that.getAccountNumber())
                :that.getAccountNumber()==null;
    }

    @Override
    public int hashCode() {
        return getAccountNumber() != null ? getAccountNumber().hashCode() : 0;
    }
}
