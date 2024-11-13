
package domain.purchase;

import data.single.CustomerData;
import data.single.ListingData;
import domain.account.user.Customer;
import domain.listing.FixedPriceListing;
import domain.listing.Listing;
import exception.ConnectionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

public class Order implements CustomerData, ListingData {
    private int quantity;
    private final Timestamp timeStamp;
    private final CustomerData customerData;
    private final ListingData listingData;
    private int version;

    public Order(int quantity, Timestamp timeStamp, CustomerData customerData, ListingData listingData, int version) {
        this.quantity = quantity;
        this.timeStamp = timeStamp;
        this.customerData = customerData;
        this.listingData = listingData;
        this.version = version;
    }

    public double getTotalPrice() throws ConnectionException {
        double price = ((FixedPriceListing) listingData.getListing()).getPrice();
        BigDecimal bd = BigDecimal.valueOf(price * quantity);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    @Override
    public Customer getCustomer() throws ConnectionException {
        return customerData.getCustomer();
    }

    @Override
    public Listing getListing() throws ConnectionException {
        return listingData.getListing();
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
