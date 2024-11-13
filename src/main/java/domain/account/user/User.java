package domain.account.user;

import domain.account.Account;
import domain.account.AccountType;

public abstract class User extends Account {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;

    public User(String username, String password, AccountType type) {
        super(username, password, type);
    }

    public User(long id, String username, String password, String firstName, String lastName,
                String email, String address, String phoneNumber, AccountType type, int version) {
        super(id, username, password, type, version);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
