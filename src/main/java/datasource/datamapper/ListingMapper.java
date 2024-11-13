package datasource.datamapper;

import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import util.DBConnection;
import datasource.proxy.*;
import domain.listing.AuctionListing;
import domain.listing.FixedPriceListing;
import domain.listing.Listing;
import domain.account.user.Seller;
import domain.listing.ListingType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListingMapper {
    private static ListingMapper instance;

    public static ListingMapper getInstance() {
        if (instance == null) instance = new ListingMapper();
        return instance;
    }

    public Listing find(long id) throws ConnectionException {
        String sql = "SELECT * FROM listings WHERE listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(id)));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                int listingID = rs.getInt(1);
                String listingName = rs.getString(2);
                String description = rs.getString(3);
                String category = rs.getString(4);
                Timestamp timeCreated = rs.getTimestamp(5);
                String listingType = rs.getString(6);
                int version = rs.getInt("version");

                statement.close();

                if (listingType.equals("AUCTION")) {
                    return findAuction(listingID, listingName, description, category, timeCreated, version);
                }
                return findFixedPrice(listingID, listingName, description, category, timeCreated, version);
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return null;
    }
    public ArrayList<Listing> findAll(String searchTerm) throws ConnectionException {
        String sql = "SELECT * FROM listings WHERE listing_name LIKE ? OR description LIKE ?;";
        ArrayList<Listing> toReturn = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        input.add("%"+searchTerm+"%");
        input.add("%"+searchTerm+"%");

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                int listingID = rs.getInt(1);
                String listingName = rs.getString(2);
                String description = rs.getString(3);
                String category = rs.getString(4);
                Timestamp timeCreated = rs.getTimestamp(5);
                String listingType = rs.getString(6);
                int version = rs.getInt("version");

                if (listingType.equals("AUCTION")) {
                    toReturn.add(findAuction(listingID, listingName, description, category, timeCreated, version));
                } else {
                    toReturn.add(findFixedPrice(listingID, listingName, description, category, timeCreated, version));
                }
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return toReturn;
    }

    private AuctionListing findAuction(long id, String name, String description, String category, Timestamp timeCreated, int version) throws ConnectionException {
        String sql = "SELECT * FROM auctions WHERE listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(id)));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                int listingID = rs.getInt(1);
                Timestamp start = rs.getTimestamp(2);
                Timestamp end = rs.getTimestamp(3);
                double basePrice = rs.getDouble(4);

                statement.close();

                return new AuctionListing(listingID, name, description, category, basePrice, start, end, timeCreated, new SellerListProxy(listingID), new AuctionBidListProxy(listingID), version);
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return null;
    }

    private FixedPriceListing findFixedPrice(long id, String name, String description, String category, Timestamp timeCreated, int version) throws ConnectionException {
        String sql = "SELECT * FROM fixed_listings WHERE listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(id)));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                int listingID = rs.getInt(1);
                double price = rs.getDouble(2);
                int quantity = rs.getInt(3);

                statement.close();

                return new FixedPriceListing(listingID, name, description, category, quantity, price, timeCreated, new SellerListProxy(listingID), new FixedPriceOrderProxy(listingID), version);
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return null;
    }

    public List<Seller> findSellers(long id) throws ConnectionException {
        String sql = "SELECT * FROM seller_listing WHERE listing_id = ?;";
        ArrayList<String> input = new ArrayList<>(Collections.singletonList(Long.toString(id)));

        List<Seller> sellers = new ArrayList<>();

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                long sellerID = rs.getInt(1);

                sellers.add((Seller) UserMapper.getInstance().find(sellerID));
            }

            statement.close();

            return sellers;

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void addSeller(long sellerID, long listingID) throws ConnectionException, InsertException {
        String listing_id = String.valueOf(listingID);
        String sql = "INSERT into seller_listing(seller_id, listing_id) VALUES(?, ?);";
        ArrayList<String> input = new ArrayList<>();
        input.add(String.valueOf(sellerID));
        input.add(listing_id);

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

    public void deleteSeller(long sellerID, long listingID) throws ConnectionException, NotFoundException {
        String listing_id = String.valueOf(listingID);
        String sql = "DELETE FROM seller_listing WHERE seller_id = ? AND listing_id = ?;";
        ArrayList<String> input = new ArrayList<>();
        input.add(String.valueOf(sellerID));
        input.add(listing_id);

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

    public void insert(Listing listing, long sellerId) throws ConnectionException, InsertException {
        String auctionSql = "WITH first AS (INSERT INTO listings (listing_name, description, listing_category, listing_type, time_created, version) VALUES (?, ?, ?::category, ?::listing_type, ?::timestamp, 1) RETURNING listing_id), second AS ( INSERT INTO seller_listing (seller_id, listing_id) VALUES (?, (SELECT listing_id FROM first)) RETURNING listing_id ) INSERT INTO auctions (listing_id, start_time, end_time, base_price) VALUES ((SELECT listing_id FROM second), ?::timestamp, ?::timestamp, ?);";
        String fixedPriceSql = "WITH first AS (INSERT INTO listings (listing_name, description, listing_category, listing_type, time_created, version) VALUES (?, ?, ?::category, ?::listing_type, ?::timestamp, 1) RETURNING listing_id), second AS ( INSERT INTO seller_listing (seller_id, listing_id) VALUES (?, (SELECT listing_id FROM first)) RETURNING listing_id ) INSERT INTO fixed_listings (listing_id, price, quantity) VALUES ((SELECT listing_id FROM second), ?, ?);";
        String sql = null;

        ArrayList<String> input = new ArrayList<>();
        input.add(listing.getName());
        input.add(listing.getDescription());
        input.add(listing.getCategory());
        input.add(String.valueOf(listing.getType()));
        input.add(new Timestamp(System.currentTimeMillis()).toString());

        input.add(String.valueOf(sellerId));

        switch (listing.getType().toString()) {
            case "AUCTION":
                AuctionListing auction = (AuctionListing) listing;
                input.add(String.valueOf(auction.getStartTime()));
                input.add(String.valueOf(auction.getEndTime()));
                input.add(String.valueOf(auction.getInitialPrice()));
                sql = auctionSql;
                break;
            case "FIXED_PRICE":
                FixedPriceListing fixedPriceListing = (FixedPriceListing) listing;
                input.add(String.valueOf(fixedPriceListing.getPrice()));
                input.add(String.valueOf(fixedPriceListing.getQuantity()));
                sql = fixedPriceSql;
                break;
        }

        assert sql != null;
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

    public void update(Listing listing) throws ConcurrencyException, ConnectionException {
        String sql = "UPDATE listings SET listing_name = ?, description = ?, listing_category = ?::category, time_created = ?::timestamp, listing_type = ?::listing_type, version = ? WHERE listing_id = ? AND version = ?;";
        ArrayList<String> input = new ArrayList<>();
        input.add(listing.getName());
        input.add(listing.getDescription());
        input.add(listing.getCategory());
        input.add(String.valueOf(listing.getTimeCreated()));
        input.add(listing.getType().toString());

        input.add(String.valueOf(listing.getVersion() + 1));
        input.add(String.valueOf(listing.getId()));
        input.add(String.valueOf(listing.getVersion()));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new ConcurrencyException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }

        input = new ArrayList<>();
        switch (listing.getType().toString()) {
            case "AUCTION":
                AuctionListing auction = (AuctionListing) listing;
                input.add(String.valueOf(auction.getStartTime()));
                input.add(String.valueOf(auction.getEndTime()));
                input.add(String.valueOf(auction.getInitialPrice()));
                input.add(String.valueOf(listing.getId()));
                sql = "UPDATE auctions SET start_time = ?::timestamp, end_time = ?::timestamp, base_price = ? WHERE listing_id = ?;";
                break;
            case "FIXED_PRICE":
                FixedPriceListing fixedPriceListing = (FixedPriceListing) listing;
                input.add(String.valueOf(fixedPriceListing.getPrice()));
                input.add(String.valueOf(fixedPriceListing.getQuantity()));
                input.add(String.valueOf(listing.getId()));
                sql = "UPDATE fixed_listings SET price = ?, quantity = ? WHERE listing_id = ?;";
                break;
        }

        try {
            statement = DBConnection.getConnection().prepare(sql, input);
            assert statement != null;
            int row = statement.executeUpdate();
            statement.close();

            if (row == 0) throw new ConcurrencyException();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public void delete(Listing listing) throws ConcurrencyException, ConnectionException, NotFoundException {
        String sql = "DELETE FROM fixed_listings WHERE listing_id = ?; DELETE FROM auctions WHERE listing_id = ?; DELETE FROM listings WHERE listing_id = ?";
        ArrayList<String> input = new ArrayList<>();
        input.add(String.valueOf(listing.getId()));
        input.add(String.valueOf(listing.getId()));
        input.add(String.valueOf(listing.getId()));

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

    public List<FixedPriceListing> findFixedPriceListings(long sellerID) throws ConnectionException {
        String sql = "SELECT * from seller_listing WHERE seller_id = ?";
        ArrayList<String> input = new ArrayList<>();
        ArrayList<Long> resultIds = new ArrayList<>();
        ArrayList<FixedPriceListing> results = new ArrayList<>();
        input.add(String.valueOf(sellerID));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                resultIds.add(rs.getLong(2));
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Long id : resultIds) {
            Listing listing = find(id);
            if (listing != null && listing.getType().equals(ListingType.FIXED_PRICE)) results.add((FixedPriceListing) listing);
        }

        return results;
    }

    public List<AuctionListing> findAuctionListings(long sellerID) throws ConnectionException {
        String sql = "SELECT * from seller_listing WHERE seller_id = ?";
        ArrayList<String> input = new ArrayList<>();
        ArrayList<Long> resultIds = new ArrayList<>();
        ArrayList<AuctionListing> results = new ArrayList<>();
        input.add(String.valueOf(sellerID));

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);
        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                resultIds.add(rs.getLong(2));
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Long id : resultIds) {
            Listing listing = find(id);
            if (listing != null && listing.getType().equals(ListingType.AUCTION)) results.add((AuctionListing) listing);
        }

        return results;
    }

    public List<Listing> getRecentListings() throws ConnectionException {
        String sql = "SELECT * FROM listings ORDER BY time_created DESC LIMIT 20;";
        ArrayList<String> input = new ArrayList<>();

        PreparedStatement statement = DBConnection.getConnection().prepare(sql, input);

        ArrayList<Listing> listings = new ArrayList<>();

        try {
            assert statement != null;
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                int listingID = rs.getInt(1);
                String listingName = rs.getString(2);
                String description = rs.getString(3);
                String category = rs.getString(4);
                Timestamp timeCreated = rs.getTimestamp(5);
                String listingType = rs.getString(6);
                int version = rs.getInt("version");

                if (listingType.equals("AUCTION")) {
                    listings.add(findAuction(listingID, listingName, description, category, timeCreated, version));
                } else {
                    listings.add(findFixedPrice(listingID, listingName, description, category, timeCreated, version));
                }
            }

            statement.close();

        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return listings;
    }
}
