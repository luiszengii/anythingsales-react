package transaction.service;

import datasource.datamapper.OrderMapper;
import domain.listing.FixedPriceListing;
import domain.purchase.Order;
import exception.*;
import transaction.unitofwork.OrderUnitOfWork;

import java.util.List;

public class OrderService {
    public static Order get(long customerID, long listingID) throws ConnectionException {
        OrderUnitOfWork.newCurrent();
        Order order = OrderMapper.getInstance().find(customerID, listingID);
        OrderUnitOfWork.getCurrent().registerClean(order);
        return order;
    }

    public static List<Order> getCustomerOrders(long customerID) throws ConnectionException {
        OrderUnitOfWork.newCurrent();
        List<Order> orders = OrderMapper.getInstance().findAllCustomerOrders(customerID);
        assert orders != null;
        for (Order order : orders) {
            OrderUnitOfWork.getCurrent().registerClean(order);
        }
        return orders;
    }

    public static List<Order> getFixedPriceOrders(long listingID) throws ConnectionException {
        OrderUnitOfWork.newCurrent();
        List<Order> orders = OrderMapper.getInstance().findAllFixedPriceOrders(listingID);
        assert orders != null;
        for (Order order : orders) {
            OrderUnitOfWork.getCurrent().registerClean(order);
        }
        return orders;
    }

    public static void create(Order order) throws ConnectionException, ConcurrencyException, InvalidOrderException, InsertException, NotFoundException {
        if (((FixedPriceListing) order.getListing()).getQuantityWithOrders() < order.getQuantity()) throw new InvalidOrderException();
        OrderUnitOfWork.newCurrent();
        OrderUnitOfWork.getCurrent().registerNew(order);
        OrderUnitOfWork.getCurrent().commit();
    }

    public static void update(Order order) throws ConnectionException, ConcurrencyException, InvalidOrderException, InsertException, NotFoundException {
        Order old = get(order.getCustomer().getId(), order.getListing().getId());
        int stock = ((FixedPriceListing) order.getListing()).getQuantityWithOrders() + old.getQuantity();
        if (stock < order.getQuantity()) throw new InvalidOrderException();

        OrderUnitOfWork.newCurrent();
        OrderUnitOfWork.getCurrent().registerDirty(order);
        OrderUnitOfWork.getCurrent().commit();
    }

    public static void delete(Order order) throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        OrderUnitOfWork.newCurrent();
        OrderUnitOfWork.getCurrent().registerDeleted(order);
        OrderUnitOfWork.getCurrent().commit();
    }

    public static void delete(List<Order> orders) throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        OrderUnitOfWork.newCurrent();
        for (Order order : orders) OrderUnitOfWork.getCurrent().registerDeleted(order);
        OrderUnitOfWork.getCurrent().commit();
    }
}
