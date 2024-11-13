package domain.listing;

import data.lists.OrderList;
import domain.account.user.Seller;
import data.lists.SellerList;
import domain.purchase.Order;
import exception.ConnectionException;

import java.sql.Timestamp;
import java.util.List;

public class FixedPriceListing extends Listing implements OrderList {
    private double price;
    private int quantity;
    private final OrderList orderList;

    public FixedPriceListing(String name, int quantity, String description, String category, double price, SellerList sellerList) {
        super(name, description, category, ListingType.FIXED_PRICE, sellerList);
        this.price = price;
        this.orderList = null;
        this.quantity = quantity;
    }

    public FixedPriceListing(long id, String name, String description, String category, int quantity, double price, Timestamp timeCreated, SellerList sellerList, OrderList orderList, int version) {
        super(id, name, description, category, ListingType.FIXED_PRICE, timeCreated, sellerList, version);
        this.quantity = quantity;
        this.price = price;
        this.orderList = orderList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public List<Seller> getSellers() throws ConnectionException {
        return super.getSellers();
    }

    @Override
    public void addSeller(Seller seller) throws ConnectionException {
        super.addSeller(seller);
    }

    @Override
    public void removeSeller(Seller seller) throws ConnectionException {
        super.removeSeller(seller);
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

    public int getQuantity() throws ConnectionException {
        return quantity;
    }

    public int getQuantityWithOrders() throws ConnectionException {
        int current = quantity;

        if (orderList != null) {
            for (Order order : orderList.getOrders()) {
                current -= order.getQuantity();
            }
        }

        return current;
    }

    public void setQuantity(int quantity) throws ConnectionException {
        this.quantity += quantity;
    }
}
