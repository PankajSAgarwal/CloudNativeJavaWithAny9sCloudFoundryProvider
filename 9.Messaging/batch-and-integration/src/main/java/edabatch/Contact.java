package edabatch;

public class Contact {
    private String fullName, email;
    private boolean validEmail;
    private long id;

    public Contact(String fullName, String email, long id) {
        this.fullName = fullName;
        this.email = email;
        this.id = id;
    }

    public Contact(boolean validEmail,String fullName, String email, long id) {
        this.fullName = fullName;
        this.email = email;
        this.validEmail = validEmail;
        this.id = id;
    }

    public Contact() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidEmail() {
        return validEmail;
    }

    public void setValidEmail(boolean validEmail) {
        this.validEmail = validEmail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", validEmail=" + validEmail +
                ", id=" + id +
                '}';
    }
}
