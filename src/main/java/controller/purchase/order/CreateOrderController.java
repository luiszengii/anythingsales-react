package controller.purchase.order;

import datasource.proxy.CustomerProxy;
import datasource.proxy.ListingProxy;
import domain.account.Account;
import domain.account.AccountType;
import domain.purchase.Order;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import transaction.service.OrderService;
import util.API;
import util.LockManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/server/purchase/new/order")
public class CreateOrderController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject body = API.readRequestBody(req);
        assert body != null;

        if (!(body.has("listingId") && body.has("quantity"))) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            DBConnection.getConnection().close();
            return;
        }

        long listingId = body.getLong("listingId");
        int quantity = body.getInt("quantity");
        Timestamp placed = new Timestamp(System.currentTimeMillis());

        try {
            LockManager.getInstance().acquireLock(listingId, req.getSession().getId());
            DBConnection.newConnection();

            if (req.getSession().getAttribute("id") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                DBConnection.getConnection().close();
                return;
            }

            long customerId = (Long) req.getSession().getAttribute("id");

            Account account = AccountService.get(customerId);
            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }
            if (!account.getType().equals(AccountType.CUSTOMER)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                DBConnection.getConnection().close();
                return;
            }

            Order order = new Order(quantity, placed, new CustomerProxy(customerId), new ListingProxy(listingId), 1);

            OrderService.create(order);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }

        LockManager.getInstance().releaseLock(listingId, req.getSession().getId());
    }
}
