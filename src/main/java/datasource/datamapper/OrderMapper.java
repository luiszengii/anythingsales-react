package datasource.datamapper;

import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import util.DBConnection;
import datasource.proxy.CustomerProxy;
import datasource.proxy.ListingProxy;
import domain.purchase.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderMapper {
    private static OrderMapper instance;

    public static OrderMapper getInstance() {
        if (instance == null) instance = new OrderMapper();
        return instance;
    }

    public Order find(long customerID, long listingID) throws ConnectionException {
        String sql = "SELECT * FROM orders WHERE customer_id = ? AND listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Arrays.asList(Long.toString(customerID), Long.toString(listingID)));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                Timestamp timePlaced = rs.getTimestamp("time_placed");
                int version = rs.getInt("version");

                statement.close();

                return new Order(quantity, timePlaced, new CustomerProxy(customerID), new ListingProxy(listingID), version);
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return null;
    }

    public List<Order> findAllCustomerOrders (long customerID) throws ConnectionException {
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(customerID)));

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                long listingID = rs.getInt("listing_id");
                int quantity = rs.getInt("quantity");
                Timestamp timePlaced = rs.getTimestamp("time_placed");
                int version = rs.getInt("version");

                orders.add(new Order(quantity, timePlaced, new CustomerProxy(customerID), new ListingProxy(listingID), version));
            }

            statement.close();

            return orders;

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public List<Order> findAllFixedPriceOrders(long listingID) throws ConnectionException {
        String sql = "SELECT * FROM orders WHERE listing_id = ?";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(listingID)));

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                long customerID = rs.getInt("customer_id");
                int quantity = rs.getInt("quantity");
                Timestamp timePlaced = rs.getTimestamp("time_placed");
                int version = rs.getInt("version");

                orders.add(new Order(quantity, timePlaced, new CustomerProxy(customerID), new ListingProxy(listingID), version));
            }

            statement.close();

            return orders;

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void insert(Order order) throws ConnectionException, InsertException {
        String sql = "INSERT into orders(customer_id, listing_id, quantity, time_placed, version) VALUES(?, ?, ?, ?::timestamp, ?)";

        //get attributes
        ArrayList<String> input = new ArrayList<>();
        input.add(String.valueOf(order.getCustomer().getId()));
        input.add(String.valueOf(order.getListing().getId()));
        input.add(String.valueOf(order.getQuantity()));
        input.add(String.valueOf(new Timestamp(System.currentTimeMillis()).toString()));
        input.add(String.valueOf(order.getVersion()));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new InsertException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void update(Order order) throws ConnectionException, ConcurrencyException {
        String sql = "UPDATE orders SET quantity = ?, time_placed = ?::timestamp, version = ? WHERE customer_id = ? AND listing_id = ? AND version = ?";

        //get attributes
        ArrayList<String> input = new ArrayList<>();

        input.add(String.valueOf(order.getQuantity()));
        input.add(String.valueOf(new Timestamp(System.currentTimeMillis()).toString()));

        input.add(String.valueOf(order.getVersion() + 1));
        input.add(String.valueOf(order.getCustomer().getId()));
        input.add(String.valueOf(order.getListing().getId()));
        input.add(String.valueOf(order.getVersion()));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new ConcurrencyException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void delete(Order order) throws ConnectionException, NotFoundException {
        String sql = "DELETE FROM orders WHERE customer_id = ? AND listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Arrays.asList(Long.toString(order.getCustomer().getId()), Long.toString(order.getListing().getId())));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new NotFoundException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

}
