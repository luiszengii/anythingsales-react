package data.single;

import domain.account.user.Customer;
import exception.ConnectionException;

public interface CustomerData {
    Customer getCustomer() throws ConnectionException;
}
