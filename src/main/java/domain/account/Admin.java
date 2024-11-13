package domain.account;

public class Admin extends Account {
    public Admin(long id, String username, String password, int version) {
        super(id, username, password, AccountType.ADMIN, version);
    }
}
