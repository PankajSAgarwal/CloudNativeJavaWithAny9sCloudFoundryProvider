package demo.account;

import demo.address.Addresss;
import demo.creditcard.CreditCard;
import demo.data.BaseEntity;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    // Intellij will flag an error but it is ok to ignore as
    //@Converter(autoApply = true) is applied on AccountNumberAttributeConverter
   // @Convert(converter = AccountNumberAttributeConverter.class)
    private AccountNumber accountNumber;
    private Boolean defaultAccount;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<CreditCard> creditCards;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Addresss> addressses;


    public Account() {
        this.creditCards = new HashSet<>();
        this.addressses = new HashSet<>();
        this.defaultAccount = false;
    }

    public Account(String username, AccountNumber accountNumber) {
        this();
        this.username = username;
        this.accountNumber = accountNumber;
        this.defaultAccount = false;
    }

    public Account(String username,String accountNumber) {
        this();
        this.username = username;
        this.accountNumber = new AccountNumber(accountNumber);
        this.defaultAccount= false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public Set<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public Set<Addresss> getAddressses() {
        return addressses;
    }

    public void setAddressses(Set<Addresss> addressses) {
        this.addressses = addressses;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", accountNumber=" + accountNumber +
                ", defaultAccount=" + defaultAccount +
                ", creditCards=" + creditCards +
                ", addressses=" + addressses +
                '}';
    }
}
