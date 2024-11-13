package datasource.proxy;

import data.single.CustomerData;
import domain.account.user.Customer;
import exception.ConnectionException;
import transaction.service.AccountService;

public class CustomerProxy implements CustomerData {
    private Customer customer = null;
    private final long id;

    public CustomerProxy(long id) {
        this.id = id;
    }

    @Override
    public Customer getCustomer() throws ConnectionException {
        if (customer == null) {
            customer = (Customer) AccountService.get(id);
        }
        return customer;
    }
}
