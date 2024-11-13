package datasource.proxy;

import data.lists.AuctionList;
import domain.listing.AuctionListing;
import exception.ConnectionException;
import transaction.service.AuctionService;

import java.util.List;

public class AuctionListProxy implements AuctionList {
    private List<AuctionListing> listings = null;
    private final long sellerID;

    public AuctionListProxy(long sellerID) {
        this.sellerID = sellerID;
    }

    @Override
    public List<AuctionListing> getAuctionListings() throws ConnectionException {
        load();
        return listings;
    }

    @Override
    public void addAuctionListing(AuctionListing listing) throws ConnectionException {
        load();
        listings.add(listing);
    }

    @Override
    public void removeAuctionListing(AuctionListing listing) throws ConnectionException {
        load();
        listings.remove(listing);
    }

    private void load() throws ConnectionException {
        if (listings == null) {
            listings = AuctionService.getSellerAuctions(sellerID);
        }
    }
}
