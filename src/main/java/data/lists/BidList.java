package data.lists;

import domain.purchase.Bid;
import exception.ConnectionException;

import java.util.List;

public interface BidList {
    List<Bid> getBids() throws ConnectionException;

    void addBid(Bid bid) throws ConnectionException;

    void removeBid(Bid bid) throws ConnectionException;
}
