package datasource.proxy;

import data.lists.OrderList;
import domain.purchase.Order;
import exception.ConnectionException;
import transaction.service.OrderService;

import java.util.List;

public class OrderListProxy implements OrderList {
    private List<Order> orders = null;
    private final long customerID;

    public OrderListProxy(long customerID) {
        this.customerID = customerID;
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
            orders = OrderService.getCustomerOrders(customerID);
        }
    }
}
