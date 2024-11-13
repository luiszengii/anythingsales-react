package data.lists;

import domain.listing.FixedPriceListing;
import exception.ConnectionException;

import java.util.List;

public interface FixedListingList {
    List<FixedPriceListing> getFixedListings() throws ConnectionException;

    void addFixedListing(FixedPriceListing listing) throws ConnectionException;

    void removeFixedListing(FixedPriceListing listing) throws ConnectionException;
}
