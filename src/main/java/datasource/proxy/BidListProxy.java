package datasource.proxy;

import data.lists.BidList;
import domain.purchase.Bid;
import exception.ConnectionException;
import transaction.service.BidService;

import java.util.List;

public class BidListProxy implements BidList {
    private List<Bid> bids = null;
    private final long customerID;

    public BidListProxy(long customerID) {
        this.customerID = customerID;
    }

    @Override
    public List<Bid> getBids() throws ConnectionException {
        load();
        return bids;
    }

    @Override
    public void addBid(Bid bid) throws ConnectionException {
        load();
        bids.add(bid);
    }

    @Override
    public void removeBid(Bid bid) throws ConnectionException {
        load();
        bids.remove(bid);
    }

    private void load() throws ConnectionException {
        if (bids == null) {
            bids = BidService.getCustomerBids(customerID);
        }
    }
}
