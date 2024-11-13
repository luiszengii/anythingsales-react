package transaction.service;

import datasource.datamapper.ListingMapper;
import domain.account.user.Seller;
import domain.listing.AuctionListing;
import domain.listing.FixedPriceListing;
import domain.listing.Listing;
import domain.listing.ListingType;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.transactionclass.SellerListing;
import transaction.unitofwork.AccountUnitOfWork;
import transaction.unitofwork.AuctionUnitOfWork;
import transaction.unitofwork.FixedPriceUnitOfWork;
import transaction.unitofwork.ListingUnitOfWork;

import java.util.List;

public class ListingService {
    public static void addSeller(long sellerId, long listingId) throws ConnectionException, InsertException, NotFoundException {
        List<Seller> sellers = findSellers(listingId);

        for (Seller seller : sellers) {
            if (seller.getId() == sellerId) return;
        }

        ListingUnitOfWork.newCurrent();
        ListingUnitOfWork.getCurrent().registerNew(new SellerListing(sellerId, listingId));
        ListingUnitOfWork.getCurrent().commit();
    }

    public static void removeSeller(long sellerId, long listingId) throws ConnectionException, InsertException, NotFoundException {
        ListingUnitOfWork.newCurrent();
        ListingUnitOfWork.getCurrent().registerDeleted(new SellerListing(sellerId, listingId));
        ListingUnitOfWork.getCurrent().commit();
    }

    public static List<Listing> find(String search) throws ConnectionException {
        FixedPriceUnitOfWork.newCurrent();
        AuctionUnitOfWork.newCurrent();

        List<Listing> listings = ListingMapper.getInstance().findAll(search);
        for (Listing listing : listings) {
            if (listing.getType().equals(ListingType.AUCTION)) {
                AuctionUnitOfWork.getCurrent().registerClean((AuctionListing) listing);
            } else {
                FixedPriceUnitOfWork.getCurrent().registerClean((FixedPriceListing) listing);
            }
        }
        return listings;
    }

    public static Listing find(long listingId) throws ConnectionException {
        FixedPriceUnitOfWork.newCurrent();
        AuctionUnitOfWork.newCurrent();

        Listing listing = ListingMapper.getInstance().find(listingId);
        assert listing != null;

        if (listing.getType().equals(ListingType.AUCTION)) {
            AuctionUnitOfWork.getCurrent().registerClean((AuctionListing) listing);
        } else {
            FixedPriceUnitOfWork.getCurrent().registerClean((FixedPriceListing) listing);
        }
        return listing;
    }

    public static List<Seller> findSellers(long listingId) throws ConnectionException {
        AccountUnitOfWork.newCurrent();
        List<Seller> sellers = ListingMapper.getInstance().findSellers(listingId);

        assert sellers != null;
        for (Seller seller : sellers) {
            AccountUnitOfWork.getCurrent().registerClean(seller);
        }
        return sellers;
    }

    public static void removeSeller(List<Seller> sellers, long listingId) throws ConnectionException, InsertException, NotFoundException {
        ListingUnitOfWork.newCurrent();
        for (Seller seller : sellers) ListingUnitOfWork.getCurrent().registerDeleted(new SellerListing(seller.getId(), listingId));
        ListingUnitOfWork.getCurrent().commit();
    }

    public static List<Listing> findRecentListings() throws ConnectionException {
        FixedPriceUnitOfWork.newCurrent();
        AuctionUnitOfWork.newCurrent();

        List<Listing> listings = ListingMapper.getInstance().getRecentListings();

        for (Listing listing: listings) {
            if (listing.getType().equals(ListingType.AUCTION)) {
                AuctionUnitOfWork.getCurrent().registerClean((AuctionListing) listing);
            } else {
                FixedPriceUnitOfWork.getCurrent().registerClean((FixedPriceListing) listing);
            }
        }

        return listings;
    }
}
