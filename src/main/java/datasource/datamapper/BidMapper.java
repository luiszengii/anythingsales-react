package datasource.datamapper;

import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import util.DBConnection;
import datasource.proxy.CustomerProxy;
import datasource.proxy.ListingProxy;
import domain.purchase.Bid;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BidMapper {
    private static BidMapper instance;

    public static BidMapper getInstance() {
        if (instance == null) instance = new BidMapper();
        return instance;
    }

    public Bid find(long customerID, long listingID) throws ConnectionException {
        String sql = "SELECT * FROM bids WHERE customer_id = ? AND listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Arrays.asList(Long.toString(customerID),Long.toString(listingID)));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                double price = rs.getDouble(3);
                Timestamp time = rs.getTimestamp(4);

                statement.close();

                return new Bid(price, time, new CustomerProxy(customerID), new ListingProxy(listingID));
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }

        return null;
    }

    public List<Bid> findAllCustomerBids(long customerID) throws ConnectionException {
        String sql = "SELECT * FROM bids WHERE customer_id = ?";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(customerID)));

        ArrayList<Bid> bids = new ArrayList<>();

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                long listingID = rs.getInt(2);
                double price = rs.getDouble(3);
                Timestamp time = rs.getTimestamp(4);

                bids.add(new Bid(price, time, new CustomerProxy(customerID), new ListingProxy(listingID)));
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }

        return bids;
    }

    public List<Bid> findAllAuctionBids(long listingID) throws ConnectionException {
        String sql = "SELECT * FROM bids WHERE listing_id = ?";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(listingID)));

        ArrayList<Bid> bids = new ArrayList<>();

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                long customerID = rs.getInt(1);
                double price = rs.getDouble(3);
                Timestamp time = rs.getTimestamp(4);

                bids.add(new Bid(price, time, new CustomerProxy(customerID), new ListingProxy(listingID)));
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }

        return bids;
    }

    public void insert(Bid bid) throws ConnectionException, InsertException {
        PreparedStatement statement;
        String sql;

        //get attributes
        String customer_id = String.valueOf(bid.getCustomer().getId());
        String listing_id = String.valueOf(bid.getListing().getId());
        String bid_price = String.valueOf(bid.getBidPrice());
        String bid_time = new Timestamp(System.currentTimeMillis()).toString();
        ArrayList<String> input = new ArrayList<>();
        input.add(customer_id);
        input.add(listing_id);
        input.add(bid_price);
        input.add(bid_time);

        sql = "INSERT into bids(customer_id, listing_id, bid_price, bid_time) VALUES(?, ?, ?, ?::timestamp)";
        statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new InsertException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void update(Bid bid) throws ConnectionException, ConcurrencyException {
        PreparedStatement statement;
        String sql;

        //get attributes
        String customer_id = String.valueOf(bid.getCustomer().getId());
        String listing_id = String.valueOf(bid.getListing().getId());
        String time_placed = String.valueOf(bid.getTimeStamp());
        String bid_price = String.valueOf(bid.getBidPrice());
        ArrayList<String> input = new ArrayList<>();
        input.add(bid_price);
        input.add(time_placed);
        input.add(customer_id);
        input.add(listing_id);

        sql = "UPDATE bids SET bid_price = ?, bid_time = ?::timestamp WHERE customer_id = ? AND listing_id = ?";
        statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new ConcurrencyException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void delete(Bid bid) throws ConnectionException, NotFoundException {
        String sql = "DELETE FROM bids WHERE customer_id = ? AND listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Arrays.asList(Long.toString(bid.getCustomer().getId()), Long.toString(bid.getListing().getId())));

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
