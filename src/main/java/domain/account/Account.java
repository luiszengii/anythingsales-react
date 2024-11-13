package domain.account;

public abstract class Account {
    private long id;
    private String username;
    private String password;
    private final AccountType type;
    private int version;

    public Account(String username, String password, AccountType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public Account(long id, String username, String password, AccountType type, int version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.version = version;
    }

    public String getPassword(){ return password; }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
