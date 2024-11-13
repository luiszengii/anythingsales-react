package datasource.proxy;

import data.single.ListingData;
import domain.listing.Listing;
import exception.ConnectionException;
import transaction.service.ListingService;

public class ListingProxy implements ListingData {
    private Listing listing = null;
    private final long id;

    public ListingProxy(long id) {
        this.id = id;
    }

    @Override
    public Listing getListing() throws ConnectionException {
        if (listing == null) {
            listing = ListingService.find(id);
        }
        return listing;
    }
}
