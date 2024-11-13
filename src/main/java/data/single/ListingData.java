package data.single;

import domain.listing.Listing;
import exception.ConnectionException;

public interface ListingData {
    Listing getListing() throws ConnectionException;
}
