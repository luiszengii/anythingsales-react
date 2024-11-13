package transaction.unitofwork;

import datasource.datamapper.ListingMapper;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.transactionclass.SellerListing;

import java.util.ArrayList;
import java.util.List;

public class ListingUnitOfWork {
    private static final ThreadLocal<ListingUnitOfWork> current = new ThreadLocal<>();

    private final List<SellerListing> newObjects = new ArrayList<>();
    private final List<SellerListing> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new ListingUnitOfWork());
    }

    private static void setCurrent(ListingUnitOfWork uow) {
        current.set(uow);
    }

    public static ListingUnitOfWork getCurrent() {
        return current.get();
    }

    public void registerNew(SellerListing sl) {
        assert sl != null;
        assert !deletedObjects.contains(sl);
        assert !newObjects.contains(sl);

        newObjects.add(sl);
    }

    public void registerDirty(SellerListing sl) {
        assert sl != null;
        assert !deletedObjects.contains(sl);
    }

    public void registerDeleted(SellerListing sl) {
        assert sl != null;

        if (newObjects.remove(sl)) return;
        if (!deletedObjects.contains(sl)) {
            deletedObjects.add(sl);
        }
    }

    public void registerClean(SellerListing sl) {
        assert sl != null;
    }

    public void commit() throws ConnectionException, InsertException, NotFoundException {
        for (SellerListing sl : newObjects) {
            ListingMapper.getInstance().addSeller(sl.getSellerId(), sl.getListingId());
        }
        for (SellerListing sl : deletedObjects) {
            ListingMapper.getInstance().deleteSeller(sl.getSellerId(), sl.getListingId());
        }
    }
}
