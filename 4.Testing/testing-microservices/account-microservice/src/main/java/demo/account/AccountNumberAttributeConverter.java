package demo.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AccountNumberAttributeConverter
        implements AttributeConverter<AccountNumber,String> {

    @Override
    public String convertToDatabaseColumn(AccountNumber accountNumber) {
        return accountNumber.toString();
    }

    @Override
    public AccountNumber convertToEntityAttribute(String s) {
        return new AccountNumber(s);
    }
}
