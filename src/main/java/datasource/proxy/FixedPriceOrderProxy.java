package datasource.proxy;

import data.lists.OrderList;
import domain.purchase.Order;
import exception.ConnectionException;
import transaction.service.OrderService;

import java.util.List;

public class FixedPriceOrderProxy implements OrderList {
    private List<Order> orders = null;
    private final long listingID;

    public FixedPriceOrderProxy(long listingID) {
        this.listingID = listingID;
    }

    @Override
    public List<Order> getOrders() throws ConnectionException {
        load();
        return orders;
    }

    @Override
    public void addOrder(Order order) throws ConnectionException {
        load();
        orders.add(order);
    }

    @Override
    public void removeOrder(Order order) throws ConnectionException {
        load();
        orders.remove(order);
    }

    private void load() throws ConnectionException {
        if (orders == null) {
            orders = OrderService.getFixedPriceOrders(listingID);
        }
    }
}
