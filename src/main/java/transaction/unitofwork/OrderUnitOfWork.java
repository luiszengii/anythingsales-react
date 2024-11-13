package transaction.unitofwork;

import datasource.datamapper.OrderMapper;
import domain.purchase.Order;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class OrderUnitOfWork {
    private static final ThreadLocal<OrderUnitOfWork> current = new ThreadLocal<>();

    private final List<Order> newObjects = new ArrayList<>();
    private final List<Order> dirtyObjects = new ArrayList<>();
    private final List<Order> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new OrderUnitOfWork());
    }

    private static void setCurrent(OrderUnitOfWork uow) {
        current.set(uow);
    }

    public static OrderUnitOfWork getCurrent() {
        return current.get();
    }

    public void registerNew(Order order) throws ConnectionException {
        checkForeignKey(order);
        assert !dirtyObjects.contains(order);
        assert !deletedObjects.contains(order);
        assert !newObjects.contains(order);

        newObjects.add(order);
    }

    public void registerDirty(Order order) throws ConnectionException {
        checkForeignKey(order);
        assert !deletedObjects.contains(order);

        if (!dirtyObjects.contains(order) && !newObjects.contains(order)) {
            dirtyObjects.add(order);
        }
    }

    public void registerDeleted(Order order) throws ConnectionException {
        checkForeignKey(order);

        if (newObjects.remove(order)) return;
        dirtyObjects.remove(order);
        if (!deletedObjects.contains(order)) {
            deletedObjects.add(order);
        }
    }

    public void registerClean(Order order) throws ConnectionException {
        checkForeignKey(order);
    }

    public void commit() throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        for (Order order : newObjects) {
            OrderMapper.getInstance().insert(order);
        }
        for (Order order : dirtyObjects) {
            OrderMapper.getInstance().update(order);
        }
        for (Order order : deletedObjects) {
            OrderMapper.getInstance().delete(order);
        }
    }

    private void checkForeignKey(Order order) throws ConnectionException {
        assert order.getCustomer() != null;
        assert order.getListing() != null;
    }
}
