package domain.account.user;

import data.lists.AuctionList;
import domain.account.AccountType;
import domain.listing.AuctionListing;
import domain.listing.FixedPriceListing;
import data.lists.FixedListingList;
import exception.ConnectionException;

import java.util.List;

public class Seller extends User implements FixedListingList, AuctionList {
    private final FixedListingList fixedListingList;
    private final AuctionList auctionList;

    public Seller(String username, String password) {
        super(username, password, AccountType.SELLER);
        this.fixedListingList = null;
        this.auctionList = null;
    }

    public Seller(long id, String username, String password, String firstName,
                  String lastName, String email, String address, String phoneNumber,
                  FixedListingList fixedListingList, AuctionList auctionList, int version) {
        super(id, username, password, firstName, lastName, email, address, phoneNumber, AccountType.SELLER, version);
        this.fixedListingList = fixedListingList;
        this.auctionList = auctionList;
    }

    @Override
    public List<FixedPriceListing> getFixedListings() throws ConnectionException {
        assert fixedListingList != null;
        return fixedListingList.getFixedListings();
    }

    @Override
    public void addFixedListing(FixedPriceListing listing) throws ConnectionException {
        assert fixedListingList != null;
        fixedListingList.addFixedListing(listing);
    }

    @Override
    public void removeFixedListing(FixedPriceListing listing) throws ConnectionException {
        assert fixedListingList != null;
        fixedListingList.removeFixedListing(listing);
    }

    @Override
    public List<AuctionListing> getAuctionListings() throws ConnectionException {
        assert auctionList != null;
        return auctionList.getAuctionListings();
    }

    @Override
    public void addAuctionListing(AuctionListing listing) throws ConnectionException {
        assert auctionList != null;
        auctionList.addAuctionListing(listing);
    }

    @Override
    public void removeAuctionListing(AuctionListing listing) throws ConnectionException {
        assert auctionList != null;
        auctionList.removeAuctionListing(listing);
    }
}
