package transaction.service;

import datasource.datamapper.BidMapper;
import domain.listing.AuctionListing;
import domain.purchase.Bid;
import exception.*;
import transaction.unitofwork.BidUnitOfWork;

import java.util.List;

public class BidService {
    public static Bid get(long customerID, long listingID) throws ConnectionException {
        BidUnitOfWork.newCurrent();
        Bid bid = BidMapper.getInstance().find(customerID, listingID);
        BidUnitOfWork.getCurrent().registerClean(bid);
        return bid;
    }

    public static List<Bid> getCustomerBids(long customerID) throws ConnectionException {
        BidUnitOfWork.newCurrent();
        List<Bid> bids = BidMapper.getInstance().findAllCustomerBids(customerID);
        assert bids != null;
        for (Bid bid : bids) {
            BidUnitOfWork.getCurrent().registerClean(bid);
        }
        return bids;
    }

    public static List<Bid> getAuctionBids(long listingID) throws ConnectionException {
        BidUnitOfWork.newCurrent();
        List<Bid> bids = BidMapper.getInstance().findAllAuctionBids(listingID);
        assert bids != null;
        for (Bid bid : bids) {
            BidUnitOfWork.getCurrent().registerClean(bid);
        }
        return bids;
    }

    public static void create(Bid bid) throws ConnectionException, InvalidBidException, ConcurrencyException, InsertException, NotFoundException {
        if (((AuctionListing) bid.getListing()).getCurrentBid() > bid.getBidPrice()) throw new InvalidBidException();
        BidUnitOfWork.newCurrent();
        BidUnitOfWork.getCurrent().registerNew(bid);
        BidUnitOfWork.getCurrent().commit();
    }

    public static void update(Bid bid) throws ConnectionException, InvalidBidException, ConcurrencyException, InsertException, NotFoundException {
        if (((AuctionListing) bid.getListing()).getCurrentBid() > bid.getBidPrice()) throw new InvalidBidException();
        BidUnitOfWork.newCurrent();
        BidUnitOfWork.getCurrent().registerDirty(bid);
        BidUnitOfWork.getCurrent().commit();
    }

    public static void delete(Bid bid) throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        BidUnitOfWork.newCurrent();
        BidUnitOfWork.getCurrent().registerDeleted(bid);
        BidUnitOfWork.getCurrent().commit();
    }

    public static void delete(List<Bid> bids) throws ConnectionException, ConcurrencyException, InsertException, NotFoundException {
        BidUnitOfWork.newCurrent();
        for (Bid bid : bids) BidUnitOfWork.getCurrent().registerDeleted(bid);
        BidUnitOfWork.getCurrent().commit();
    }
}
