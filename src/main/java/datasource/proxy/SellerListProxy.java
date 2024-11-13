package datasource.proxy;

import domain.account.user.Seller;
import data.lists.SellerList;
import exception.ConnectionException;
import transaction.service.ListingService;

import java.util.List;

public class SellerListProxy implements SellerList {
    private List<Seller> sellers = null;
    private final long listingID;

    public SellerListProxy(long listingID) {
        this.listingID = listingID;
    }

    @Override
    public List<Seller> getSellers() throws ConnectionException {
        load();
        return sellers;
    }

    @Override
    public void addSeller(Seller seller) throws ConnectionException {
        load();
        sellers.add(seller);
    }

    @Override
    public void removeSeller(Seller seller) throws ConnectionException {
        load();
        sellers.remove(seller);
    }

    private void load() throws ConnectionException {
        if (sellers == null) {
            sellers = ListingService.findSellers(listingID);
        }
    }
}
