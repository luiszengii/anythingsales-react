package domain.purchase;

import data.single.CustomerData;
import data.single.ListingData;
import domain.account.user.Customer;
import domain.listing.Listing;
import exception.ConnectionException;

import java.sql.Timestamp;

public class Bid implements CustomerData, ListingData {
    private double bidPrice;
    private Timestamp timeStamp;
    private final CustomerData customerData;
    private final ListingData listingData;

    public Bid(double bidPrice, Timestamp timeStamp, CustomerData customerData, ListingData listingData) {
        this.bidPrice = bidPrice;
        this.timeStamp = timeStamp;
        this.customerData = customerData;
        this.listingData = listingData;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public Customer getCustomer() throws ConnectionException {
        return customerData.getCustomer();
    }

    @Override
    public Listing getListing() throws ConnectionException {
        return listingData.getListing();
    }
}
