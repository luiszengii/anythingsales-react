package domain.listing;

import domain.account.user.Seller;
import data.lists.SellerList;
import exception.ConnectionException;

import java.sql.Timestamp;
import java.util.List;

public abstract class Listing implements SellerList {
    private long id;
    private String name;
    private String description;
    private String category;
    private final ListingType type;
    private Timestamp timeCreated;
    private int version;

    private final SellerList sellerList;

    public Listing(String name, String description, String category, ListingType type, SellerList sellerList) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.sellerList = sellerList;
    }

    public Listing(long id, String name, String description, String category, ListingType type, Timestamp timeCreated, SellerList sellerList, int version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.timeCreated = timeCreated;
        this.sellerList = sellerList;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ListingType getType() {
        return type;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public List<Seller> getSellers() throws ConnectionException {
        return sellerList.getSellers();
    }

    public void addSeller(Seller seller) throws ConnectionException {
        sellerList.addSeller(seller);
    }

    public void removeSeller(Seller seller) throws ConnectionException {
        sellerList.removeSeller(seller);
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
