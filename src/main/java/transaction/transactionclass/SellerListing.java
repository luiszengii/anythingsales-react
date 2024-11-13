package transaction.transactionclass;

public class SellerListing {
    private final long sellerId;
    private final long listingId;

    public SellerListing(long sellerId, long listingId) {
        this.sellerId = sellerId;
        this.listingId = listingId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public long getListingId() {
        return listingId;
    }
}
