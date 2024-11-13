package transaction.service;

import datasource.datamapper.UserMapper;
import domain.account.Account;
import domain.account.AccountType;
import domain.account.user.Seller;
import exception.*;
import transaction.unitofwork.AccountUnitOfWork;

import java.util.List;

public class AccountService {
    public static Account login(String username, String password) throws ConnectionException, InvalidPasswordException {
        AccountUnitOfWork.newCurrent();
        Account user = UserMapper.getInstance().find(username, password);
        if (user == null && findWithUsername(username) != null) throw new InvalidPasswordException();
        AccountUnitOfWork.getCurrent().registerClean(user);
        return user;
    }

    public static Account get(long id) throws ConnectionException {
        AccountUnitOfWork.newCurrent();
        Account user = UserMapper.getInstance().find(id);
        AccountUnitOfWork.getCurrent().registerClean(user);
        return user;
    }

    public static List<Account> getAll() throws ConnectionException {
        AccountUnitOfWork.newCurrent();
        List<Account> accounts = UserMapper.getInstance().getAll();
        for (Account account : accounts) {
            AccountUnitOfWork.getCurrent().registerClean(account);
        }
        return accounts;
    }

    public static List<Account> find(String search) throws ConnectionException {
        AccountUnitOfWork.newCurrent();
        List<Account> accounts = UserMapper.getInstance().findAll(search);
        for (Account account : accounts) {
            AccountUnitOfWork.getCurrent().registerClean(account);
        }
        return accounts;
    }

    public static void create(Account account) throws ConnectionException, UsernameTakenException, ConcurrencyException, InsertException, NotFoundException {
        AccountUnitOfWork.newCurrent();
        if (findWithUsername(account.getUsername()) != null) throw new UsernameTakenException();
        AccountUnitOfWork.getCurrent().registerNew(account);
        AccountUnitOfWork.getCurrent().commit();
    }

    public static void update(Account account) throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        AccountUnitOfWork.newCurrent();
        AccountUnitOfWork.getCurrent().registerDirty(account);
        AccountUnitOfWork.getCurrent().commit();
    }

    public static void delete(Account account) throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        AccountUnitOfWork.newCurrent();
        AccountUnitOfWork.getCurrent().registerDeleted(account);
        AccountUnitOfWork.getCurrent().commit();
    }

    public static Seller findSeller(String username) throws ConnectionException {
        AccountUnitOfWork.newCurrent();

        List<Account> all = UserMapper.getInstance().getAll();
        for (Account account : all) {
            AccountUnitOfWork.getCurrent().registerClean(account);
        }

        Seller seller = null;
        for (Account account : all) {
            if (account.getType().equals(AccountType.SELLER) && account.getUsername().equals(username)) {
                seller = (Seller) account;
                break;
            }
        }
        return seller;
    }

    public static Account findWithUsername(String username) throws ConnectionException {
        AccountUnitOfWork.newCurrent();

        List<Account> all = UserMapper.getInstance().getAll();
        for (Account a : all) {
            AccountUnitOfWork.getCurrent().registerClean(a);
        }

        Account account = null;
        for (Account a : all) {
            if (a.getUsername().equals(username)) {
                account = a;
                break;
            }
        }
        return account;
    }
}
