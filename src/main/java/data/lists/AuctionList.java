package data.lists;

import domain.listing.AuctionListing;
import exception.ConnectionException;

import java.util.List;

public interface AuctionList {
    List<AuctionListing> getAuctionListings() throws ConnectionException;

    void addAuctionListing(AuctionListing listing) throws ConnectionException;

    void removeAuctionListing(AuctionListing listing) throws ConnectionException;
}
