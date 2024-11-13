package transaction.service;

import datasource.datamapper.ListingMapper;
import domain.listing.AuctionListing;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.unitofwork.AuctionUnitOfWork;

import java.util.List;

public class AuctionService {
    public static AuctionListing get(long auctionID) throws ConnectionException {
        AuctionUnitOfWork.newCurrent();
        AuctionListing auction = (AuctionListing) ListingMapper.getInstance().find(auctionID);
        AuctionUnitOfWork.getCurrent().registerClean(auction);
        return auction;
    }

    public static List<AuctionListing> getSellerAuctions(long sellerID) throws ConnectionException {
        AuctionUnitOfWork.newCurrent();
        List<AuctionListing> auctionListings = ListingMapper.getInstance().findAuctionListings(sellerID);
        for (AuctionListing auction : auctionListings) {
            AuctionUnitOfWork.getCurrent().registerClean(auction);
        }
        return auctionListings;
    }

    public static void create(AuctionListing auctionListing) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        AuctionUnitOfWork.newCurrent();
        AuctionUnitOfWork.getCurrent().registerNew(auctionListing);
        AuctionUnitOfWork.getCurrent().commit();
    }

    public static void update(AuctionListing auctionListing) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        AuctionUnitOfWork.newCurrent();
        AuctionUnitOfWork.getCurrent().registerDirty(auctionListing);
        AuctionUnitOfWork.getCurrent().commit();
    }

    public static void delete(AuctionListing auctionListing) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        AuctionUnitOfWork.newCurrent();
        AuctionUnitOfWork.getCurrent().registerDeleted(auctionListing);
        AuctionUnitOfWork.getCurrent().commit();
    }

    public static void delete(List<AuctionListing> listings) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        AuctionUnitOfWork.newCurrent();
        for (AuctionListing listing : listings) AuctionUnitOfWork.getCurrent().registerDeleted(listing);
        AuctionUnitOfWork.getCurrent().commit();
    }
}
