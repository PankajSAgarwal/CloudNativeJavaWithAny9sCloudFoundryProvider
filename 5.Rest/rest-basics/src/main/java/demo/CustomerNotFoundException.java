package demo;

public class CustomerNotFoundException extends RuntimeException{
    private Long id;

    public CustomerNotFoundException(Long id) {
        super("customer-not-found" + id);
        this.id = id;
    }

    public Long getCustomerId() {
        return id;
    }
}
