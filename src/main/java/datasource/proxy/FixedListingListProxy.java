package datasource.proxy;

import data.lists.FixedListingList;
import domain.listing.FixedPriceListing;
import exception.ConnectionException;
import transaction.service.FixedPriceService;

import java.util.List;

public class FixedListingListProxy implements FixedListingList {
    private List<FixedPriceListing> listings = null;
    private final long sellerID;

    public FixedListingListProxy(long sellerID) {
        this.sellerID = sellerID;
    }

    @Override
    public List<FixedPriceListing> getFixedListings() throws ConnectionException {
        load();
        return listings;
    }

    @Override
    public void addFixedListing(FixedPriceListing listing) throws ConnectionException {
        load();
        listings.add(listing);
    }

    @Override
    public void removeFixedListing(FixedPriceListing listing) throws ConnectionException {
        load();
        listings.remove(listing);
    }

    private void load() throws ConnectionException {
        if (listings == null) {
            listings = FixedPriceService.getSellerListings(sellerID);
        }
    }
}
