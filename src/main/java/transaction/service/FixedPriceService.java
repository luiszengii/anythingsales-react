package transaction.service;

import datasource.datamapper.ListingMapper;
import domain.listing.FixedPriceListing;
import exception.ConcurrencyException;
import exception.ConnectionException;
import exception.InsertException;
import exception.NotFoundException;
import transaction.unitofwork.FixedPriceUnitOfWork;

import java.util.List;

public class FixedPriceService {
    public static FixedPriceListing get(long listingID) throws ConnectionException {
        FixedPriceUnitOfWork.newCurrent();
        FixedPriceListing fixedPriceListing = (FixedPriceListing) ListingMapper.getInstance().find(listingID);
        FixedPriceUnitOfWork.getCurrent().registerClean(fixedPriceListing);
        return fixedPriceListing;
    }

    public static List<FixedPriceListing> getSellerListings(long sellerID) throws ConnectionException {
        FixedPriceUnitOfWork.newCurrent();
        List<FixedPriceListing> fixedPriceListings = ListingMapper.getInstance().findFixedPriceListings(sellerID);
        for (FixedPriceListing listing : fixedPriceListings) {
            FixedPriceUnitOfWork.getCurrent().registerClean(listing);
        }
        return fixedPriceListings;
    }

    public static void create(FixedPriceListing fixedPriceListing) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        FixedPriceUnitOfWork.newCurrent();
        FixedPriceUnitOfWork.getCurrent().registerNew(fixedPriceListing);
        FixedPriceUnitOfWork.getCurrent().commit();
    }

    public static void update(FixedPriceListing fixedPriceListing) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        if (fixedPriceListing.getQuantityWithOrders() < 0) throw new ConcurrencyException();
        FixedPriceUnitOfWork.newCurrent();
        FixedPriceUnitOfWork.getCurrent().registerDirty(fixedPriceListing);
        FixedPriceUnitOfWork.getCurrent().commit();
    }

    public static void delete(FixedPriceListing fixedPriceListing) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        FixedPriceUnitOfWork.newCurrent();
        FixedPriceUnitOfWork.getCurrent().registerDeleted(fixedPriceListing);
        FixedPriceUnitOfWork.getCurrent().commit();
    }

    public static void delete(List<FixedPriceListing> listings) throws ConcurrencyException, ConnectionException, InsertException, NotFoundException {
        FixedPriceUnitOfWork.newCurrent();
        for (FixedPriceListing listing : listings) FixedPriceUnitOfWork.getCurrent().registerDeleted(listing);
        FixedPriceUnitOfWork.getCurrent().commit();
    }
}
