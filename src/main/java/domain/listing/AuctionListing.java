package domain.listing;

import data.lists.BidList;
import domain.account.user.Seller;
import data.lists.SellerList;
import domain.purchase.Bid;
import exception.ConnectionException;

import java.sql.Timestamp;
import java.util.List;

public class AuctionListing extends Listing implements BidList {
    private double initialPrice;
    private Timestamp startTime;
    private Timestamp endTime;

    private final BidList bidList;

    public AuctionListing(String name, String description, String category, double initialPrice, Timestamp startTime, Timestamp endTime, SellerList sellerList) {
        super(name, description, category, ListingType.AUCTION, sellerList);
        this.initialPrice = initialPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bidList = null;
    }

    public AuctionListing(long id, String name, String description, String category, double initialPrice, Timestamp startTime, Timestamp endTime, Timestamp timeCreated, SellerList sellerList, BidList bidList, int version) {
        super(id, name, description, category, ListingType.AUCTION, timeCreated, sellerList, version);
        this.initialPrice = initialPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bidList = bidList;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }

    public double getCurrentBid() throws ConnectionException {
        double current = initialPrice;

        if (bidList != null) {
            for (Bid bid : bidList.getBids()) {
                if (bid.getBidPrice() > current) current = bid.getBidPrice();
            }
        }

        return current;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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
