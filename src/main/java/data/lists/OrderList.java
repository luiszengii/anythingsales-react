package data.lists;

import domain.purchase.Order;
import exception.ConnectionException;

import java.util.List;

public interface OrderList {
    List<Order> getOrders() throws ConnectionException;

    void addOrder(Order order) throws ConnectionException;

    void removeOrder(Order order) throws ConnectionException;
}
