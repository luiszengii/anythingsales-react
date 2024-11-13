package data.lists;

import domain.account.user.Seller;
import exception.ConnectionException;

import java.util.List;

public interface SellerList {
    List<Seller> getSellers() throws ConnectionException;

    void addSeller(Seller seller) throws ConnectionException;

    void removeSeller(Seller seller) throws ConnectionException;
}
