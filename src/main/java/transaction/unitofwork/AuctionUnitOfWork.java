package transaction.unitofwork;

import datasource.datamapper.ListingMapper;
import domain.listing.AuctionListing;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.service.BidService;
import transaction.service.ListingService;

import java.util.ArrayList;
import java.util.List;

/**
 * For creating/updating/deleting auctions.
 */
public class AuctionUnitOfWork {
    private static final ThreadLocal<AuctionUnitOfWork> current = new ThreadLocal<>();

    private final List<AuctionListing> newObjects = new ArrayList<>();
    private final List<AuctionListing> dirtyObjects = new ArrayList<>();
    private final List<AuctionListing> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new AuctionUnitOfWork());
    }

    private static void setCurrent(AuctionUnitOfWork uow) {
        current.set(uow);
    }

    public static AuctionUnitOfWork getCurrent() {
        return current.get();
    }

    public void registerNew(AuctionListing auctionListing) {

        assert auctionListing != null;
        assert !dirtyObjects.contains(auctionListing);
        assert !deletedObjects.contains(auctionListing);
        assert !newObjects.contains(auctionListing);

        newObjects.add(auctionListing);
    }

    public void registerDirty(AuctionListing auctionListing) {
        assert auctionListing != null;
        assert !deletedObjects.contains(auctionListing);

        if (!dirtyObjects.contains(auctionListing) && !newObjects.contains(auctionListing)) {
            dirtyObjects.add(auctionListing);
        }
    }

    public void registerDeleted(AuctionListing auctionListing) {
        assert auctionListing != null;

        if (newObjects.remove(auctionListing)) return;
        dirtyObjects.remove(auctionListing);
        if (!deletedObjects.contains(auctionListing)) {
            deletedObjects.add(auctionListing);
        }
    }

    public void registerClean(AuctionListing auctionListing) {
        assert auctionListing != null;
    }

    public void commit() throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        for (AuctionListing auctionListing : newObjects) {
            ListingMapper.getInstance().insert(auctionListing, auctionListing.getSellers().get(0).getId());
        }
        for (AuctionListing auctionListing : dirtyObjects) {
            ListingMapper.getInstance().update(auctionListing);
        }
        for (AuctionListing auctionListing : deletedObjects) {
            ListingService.removeSeller(auctionListing.getSellers(), auctionListing.getId());
            BidService.delete(auctionListing.getBids());
            ListingMapper.getInstance().delete(auctionListing);
        }
    }
}
