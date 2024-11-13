package datasource.datamapper;

import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import util.DBConnection;
import datasource.proxy.AuctionListProxy;
import datasource.proxy.BidListProxy;
import datasource.proxy.OrderListProxy;
import domain.account.Account;
import domain.account.AccountType;
import domain.account.Admin;
import domain.account.user.Customer;
import domain.account.user.Seller;
import datasource.proxy.FixedListingListProxy;
import domain.account.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserMapper {
    private static UserMapper instance;

    public static UserMapper getInstance() {
        if (instance == null) instance = new UserMapper();
        return instance;
    }

    public List<Account> getAll() throws ConnectionException {
        String sql = "SELECT * FROM accounts";
        ArrayList<Account> result = new ArrayList<>();
        long id;

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, new ArrayList<>());

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {

                switch (rs.getString("account_type")) {
                    case "SELLER":
                        id = rs.getLong(1);
                        result.add(new Seller(id, rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new FixedListingListProxy(id), new AuctionListProxy(id), rs.getInt("version")));
                        break;
                    case "CUSTOMER":
                        id = rs.getLong(1);
                        result.add(new Customer(id, rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new OrderListProxy(id), new BidListProxy(id), rs.getInt("version")));
                        break;
                    case "ADMIN":
                        result.add(new Admin(rs.getLong(1),
                                rs.getString(2), rs.getString(3), rs.getInt("version")));
                        break;
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return result;
    }

    public Account find(long id) throws ConnectionException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(id)));
        Account account = null;

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                switch (rs.getString("account_type")) {
                    case "SELLER":
                        account = new Seller(rs.getLong(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new FixedListingListProxy(id), new AuctionListProxy(id), rs.getInt("version"));
                        break;
                    case "CUSTOMER":
                        account = new Customer(rs.getLong(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new OrderListProxy(id), new BidListProxy(id), rs.getInt("version"));
                        break;
                    case "ADMIN":
                        account = new Admin(rs.getLong(1),
                                rs.getString(2), rs.getString(3), rs.getInt("version"));
                        break;
                }
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }

        return account;
    }

    public Account find(String username, String password) throws ConnectionException {
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
        ArrayList<String> input = new ArrayList<>(Arrays.asList(username, password));
        Account account = null;

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                switch (rs.getString("account_type")) {
                    case "SELLER":
                        account = new Seller(rs.getLong(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new FixedListingListProxy(rs.getLong(1)),
                                new AuctionListProxy(rs.getLong(1)), rs.getInt("version"));
                        break;
                    case "CUSTOMER":
                        account = new Customer(rs.getLong(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new OrderListProxy(rs.getLong(1)),
                                new BidListProxy(rs.getLong(1)), rs.getInt("version"));
                        break;
                    case "ADMIN":
                        account = new Admin(rs.getLong(1),
                                rs.getString(2), rs.getString(3), rs.getInt("version"));
                        break;
                }
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }

        return account;
    }

    public ArrayList<Account> findAll(String searchTerm) throws ConnectionException {
        String sql = "SELECT * FROM accounts WHERE username LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR email LIKE ?";
        ArrayList<Account> toReturn = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        input.add("%"+searchTerm+"%");
        input.add("%"+searchTerm+"%");
        input.add("%"+searchTerm+"%");
        input.add("%"+searchTerm+"%");

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {

                switch (rs.getString("account_type")) {
                    case "SELLER":
                        toReturn.add(new Seller(rs.getLong(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new FixedListingListProxy(rs.getLong(1)),
                                new AuctionListProxy(rs.getLong(1)), rs.getInt("version")));
                        break;
                    case "CUSTOMER":
                        toReturn.add(new Customer(rs.getLong(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7), rs.getString(8),
                                new OrderListProxy(rs.getLong(1)),
                                new BidListProxy(rs.getLong(1)), rs.getInt("version")));
                        break;
                    case "ADMIN":
                        toReturn.add(new Admin(rs.getLong(1),
                                rs.getString(2), rs.getString(3), rs.getInt("version")));
                        break;
                }
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return toReturn;
    }

    public void insert(Account account) throws ConnectionException, InsertException {

        String username = account.getUsername();
        String password = account.getPassword();

        //get attributes of customer/seller
        String sql = null;
        ArrayList<String> input = new ArrayList<>();
        input.add(username);
        input.add(password);

        if (!account.getType().equals(AccountType.ADMIN)) {

            if(account.getType().equals(AccountType.CUSTOMER)) {
                 sql = "INSERT into accounts(username, password, account_type, version) VALUES(?, ?, 'CUSTOMER'::account_type, 1);";
            }
            else if(account.getType().equals(AccountType.SELLER)){
                 sql = "INSERT into accounts(username, password, account_type, version) VALUES(?, ?, 'SELLER'::account_type, 1);";
            }}
        else {
             sql =  "INSERT into accounts(username, password, version) VALUES(?, ?, 'ADMIN'::account_type, 1);";
        }

        try {
            assert sql != null;
            PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

            assert statement != null;

            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new InsertException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void delete(Account account) throws ConnectionException, NotFoundException {
        long id = account.getId();
        PreparedStatement statement;

        String sql = "DELETE FROM accounts WHERE account_id = ?;";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(id)));

        try {
            statement = DBConnection.getConnection().prepare(sql, input);
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new NotFoundException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void update(Account account) throws ConnectionException, ConcurrencyException {
        //check if input is valid
        PreparedStatement statement;

        String sql;

        ArrayList<String> input= new ArrayList<>();
        input.add(account.getUsername());
        input.add(account.getPassword());

        if (!account.getType().equals(AccountType.ADMIN)) {
            input.add(((User) account).getFirstName());
            input.add(((User) account).getLastName());
            input.add(((User) account).getEmail());
            input.add(((User) account).getAddress());
            input.add(((User) account).getPhoneNumber());

            if(account.getType().equals(AccountType.CUSTOMER)) {
                sql = "UPDATE accounts SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, address = ?, phone_number = ?, version = ? WHERE account_id = ? AND account_type = 'CUSTOMER' AND version = ?;";
            } else {
                sql = "UPDATE accounts SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, address = ?, phone_number = ?, version = ? WHERE account_id = ? AND account_type = 'SELLER' AND version = ?;";
            }
        } else {
            sql = "UPDATE accounts SET username = ?, password = ?, version = ? WHERE account_id = ? AND account_type = 'ADMIN' AND version = ?;";
        }

        input.add(String.valueOf(account.getVersion() + 1));
        input.add(String.valueOf(account.getId()));
        input.add(String.valueOf(account.getVersion()));

        try {
            statement = DBConnection.getConnection().prepare(sql, input);

            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new ConcurrencyException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }
}
