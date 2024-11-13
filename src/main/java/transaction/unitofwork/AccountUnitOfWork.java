package transaction.unitofwork;

import datasource.datamapper.UserMapper;
import domain.account.Account;
import domain.account.user.Customer;
import domain.account.user.Seller;
import domain.account.user.User;
import domain.account.AccountType;
import domain.listing.AuctionListing;
import domain.listing.FixedPriceListing;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * For updating user details and creating/deleting user accounts.
 */
public class AccountUnitOfWork {
    private static final ThreadLocal<AccountUnitOfWork> current = new ThreadLocal<>();

    private final List<Account> newObjects = new ArrayList<>();
    private final List<Account> dirtyObjects = new ArrayList<>();
    private final List<Account> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        current.remove();
        setCurrent(new AccountUnitOfWork());
    }

    private static void setCurrent(AccountUnitOfWork uow) {
        current.set(uow);
    }

    public static AccountUnitOfWork getCurrent() {
        return current.get();
    }

    public void registerNew(Account account) {
        assert account != null;
        assert !dirtyObjects.contains(account);
        assert !deletedObjects.contains(account);
        assert !newObjects.contains(account);

        newObjects.add(account);
    }

    public void registerDirty(Account account) {
        assert account != null;
        assert !deletedObjects.contains(account);

        if (!dirtyObjects.contains(account) && !newObjects.contains(account)) {
            dirtyObjects.add(account);
        }
    }

    public void registerDeleted(Account user) {
        assert user != null;

        if (newObjects.remove(user)) return;
        dirtyObjects.remove(user);
        if (!deletedObjects.contains(user)) {
            deletedObjects.add(user);
        }
    }

    public void registerClean(Account user) {
        assert user != null;
    }

    public void commit() throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        for (Account account : newObjects) {
            UserMapper.getInstance().insert(account);
        }
        for (Account account : dirtyObjects) {
            UserMapper.getInstance().update(account);
        }
        for (Account account : deletedObjects) {
            if (!account.getType().equals(AccountType.ADMIN)) {
                User user = (User) account;

                if (user.getType().equals(AccountType.CUSTOMER)) {
                    Customer customer = (Customer) account;
                    BidService.delete(customer.getBids());
                    OrderService.delete(customer.getOrders());

                } else if (user.getType().equals(AccountType.SELLER)) {
                    Seller seller = (Seller) account;
                    ArrayList<FixedPriceListing> fixedPriceListings = new ArrayList<>();
                    ArrayList<AuctionListing> auctionListings = new ArrayList<>();

                    for (FixedPriceListing listing : seller.getFixedListings()) {
                        if (listing.getSellers().size() == 1) {
                            fixedPriceListings.add(listing);
                        } else {
                            List<Seller> sellers = new ArrayList<>();
                            sellers.add(seller);
                            ListingService.removeSeller(sellers, listing.getId());
                        }
                    }

                    for (AuctionListing listing : seller.getAuctionListings()) {
                        if (listing.getSellers().size() == 1) {
                            auctionListings.add(listing);
                        } else {
                            List<Seller> sellers = new ArrayList<>();
                            sellers.add(seller);
                            ListingService.removeSeller(sellers, listing.getId());
                        }
                    }

                    FixedPriceService.delete(fixedPriceListings);
                    AuctionService.delete(auctionListings);
                }
            }
            UserMapper.getInstance().delete(account);
        }
    }
}
