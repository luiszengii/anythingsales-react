package transaction.unitofwork;

import datasource.datamapper.ListingMapper;
import domain.listing.FixedPriceListing;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.service.ListingService;
import transaction.service.OrderService;

import java.util.ArrayList;
import java.util.List;

/**
 * For creating/updating/deleting fixed price listings.
 */
public class FixedPriceUnitOfWork {
    private static final ThreadLocal<FixedPriceUnitOfWork> current = new ThreadLocal<>();

    private final List<FixedPriceListing> newObjects = new ArrayList<>();
    private final List<FixedPriceListing> dirtyObjects = new ArrayList<>();
    private final List<FixedPriceListing> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new FixedPriceUnitOfWork());
    }

    private static void setCurrent(FixedPriceUnitOfWork uow) {
        current.set(uow);
    }

    public static FixedPriceUnitOfWork getCurrent() {
        return current.get();
    }

    public void registerNew(FixedPriceListing fixedPriceListing) {

        assert fixedPriceListing != null;
        assert !dirtyObjects.contains(fixedPriceListing);
        assert !deletedObjects.contains(fixedPriceListing);
        assert !newObjects.contains(fixedPriceListing);

        newObjects.add(fixedPriceListing);
    }

    public void registerDirty(FixedPriceListing fixedPriceListing) {
        assert fixedPriceListing != null;
        assert !deletedObjects.contains(fixedPriceListing);

        if (!dirtyObjects.contains(fixedPriceListing) && !newObjects.contains(fixedPriceListing)) {
            dirtyObjects.add(fixedPriceListing);
        }
    }

    public void registerDeleted(FixedPriceListing fixedPriceListing) {
        assert fixedPriceListing != null;

        if (newObjects.remove(fixedPriceListing)) return;
        dirtyObjects.remove(fixedPriceListing);
        if (!deletedObjects.contains(fixedPriceListing)) {
            deletedObjects.add(fixedPriceListing);
        }
    }

    public void registerClean(FixedPriceListing fixedPriceListing) {
        assert fixedPriceListing != null;
    }

    public void commit() throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        for (FixedPriceListing fixedPriceListing : newObjects) {
            ListingMapper.getInstance().insert(fixedPriceListing, fixedPriceListing.getSellers().get(0).getId());
        }
        for (FixedPriceListing fixedPriceListing : dirtyObjects) {
            ListingMapper.getInstance().update(fixedPriceListing);
        }
        for (FixedPriceListing fixedPriceListing : deletedObjects) {
            ListingService.removeSeller(fixedPriceListing.getSellers(), fixedPriceListing.getId());
            OrderService.delete(fixedPriceListing.getOrders());
            ListingMapper.getInstance().delete(fixedPriceListing);
        }
    }
}
