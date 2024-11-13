package datasource.proxy;

import data.lists.SellerList;
import domain.account.user.Seller;

import java.util.ArrayList;
import java.util.List;

public class NewSellerListProxy implements SellerList {
    private final List<Seller> sellers = new ArrayList<>();

    public NewSellerListProxy(Seller seller) {
        this.sellers.add(seller);
    }

    @Override
    public List<Seller> getSellers() {
        return sellers;
    }

    @Override
    public void addSeller(Seller seller) {
        sellers.add(seller);
    }

    @Override
    public void removeSeller(Seller seller) {
        sellers.remove(seller);
    }
}
