package transaction.unitofwork;

import datasource.datamapper.BidMapper;
import domain.purchase.Bid;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class BidUnitOfWork {
    private static final ThreadLocal<BidUnitOfWork> current = new ThreadLocal<>();

    private final List<Bid> newObjects = new ArrayList<>();
    private final List<Bid> dirtyObjects = new ArrayList<>();
    private final List<Bid> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new BidUnitOfWork());
    }

    private static void setCurrent(BidUnitOfWork uow) {
        current.set(uow);
    }

    public static BidUnitOfWork getCurrent() {
        return current.get();
    }

    public void registerNew(Bid bid) throws ConnectionException {
        checkForeignKey(bid);
        assert !dirtyObjects.contains(bid);
        assert !deletedObjects.contains(bid);
        assert !newObjects.contains(bid);

        newObjects.add(bid);
    }

    public void registerDirty(Bid bid) throws ConnectionException {
        checkForeignKey(bid);
        assert !deletedObjects.contains(bid);

        if (!dirtyObjects.contains(bid) && !newObjects.contains(bid)) {
            dirtyObjects.add(bid);
        }
    }

    public void registerDeleted(Bid bid) throws ConnectionException {
        checkForeignKey(bid);

        if (newObjects.remove(bid)) return;
        dirtyObjects.remove(bid);
        if (!deletedObjects.contains(bid)) {
            deletedObjects.add(bid);
        }
    }

    public void registerClean(Bid bid) throws ConnectionException {
        checkForeignKey(bid);
    }

    public void commit() throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        for (Bid bid : newObjects) {
            BidMapper.getInstance().insert(bid);
        }
        for (Bid bid : dirtyObjects) {
            BidMapper.getInstance().update(bid);
        }
        for (Bid bid : deletedObjects) {
            BidMapper.getInstance().delete(bid);
        }
    }

    private void checkForeignKey(Bid bid) throws ConnectionException {
        assert bid.getCustomer() != null;
        assert bid.getListing() != null;
    }
}
