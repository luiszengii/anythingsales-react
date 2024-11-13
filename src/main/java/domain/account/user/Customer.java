package domain.account.user;

import data.lists.BidList;
import data.lists.OrderList;
import domain.account.AccountType;
import domain.purchase.Bid;
import domain.purchase.Order;
import exception.ConnectionException;

import java.util.List;

public class Customer extends User implements OrderList, BidList {
    private final OrderList orderList;
    private final BidList bidList;

    public Customer(String username, String password) {
        super(username, password, AccountType.CUSTOMER);
        orderList = null;
        bidList = null;
    }

    public Customer(long id, String username, String password, String firstName,
                    String lastName, String email, String address,
                    String phoneNumber, OrderList orderList, BidList bidList, int version) {
        super(id, username, password, firstName, lastName, email, address, phoneNumber, AccountType.CUSTOMER, version);

        this.orderList = orderList;
        this.bidList = bidList;
    }

    @Override
    public List<Order> getOrders() throws ConnectionException {
        assert orderList != null;
        return orderList.getOrders();
    }

    @Override
    public void addOrder(Order order) throws ConnectionException {
        assert orderList != null;
        orderList.addOrder(order);
    }

    @Override
    public void removeOrder(Order order) throws ConnectionException {
        assert orderList != null;
        orderList.removeOrder(order);
    }

    @Override
    public List<Bid> getBids() throws ConnectionException {
        assert bidList != null;
        return bidList.getBids();
    }

    @Override
    public void addBid(Bid bid) throws ConnectionException {
        assert bidList != null;
        bidList.addBid(bid);
    }

    @Override
    public void removeBid(Bid bid) throws ConnectionException {
        assert bidList != null;
        bidList.removeBid(bid);
    }
}
