package controller.purchase.bid;

import datasource.proxy.CustomerProxy;
import datasource.proxy.ListingProxy;
import domain.account.Account;
import domain.account.AccountType;
import domain.purchase.Bid;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import transaction.service.BidService;
import util.API;
import util.LockManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/server/purchase/new/bid")
public class CreateBidController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        } else {
            this.doPatch(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject body = API.readRequestBody(req);
        assert body != null;

        if (!(body.has("listingId") && body.has("price"))) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            DBConnection.getConnection().close();
            return;
        }

        long listingId = body.getLong("listingId");
        double price = body.getDouble("price");
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

            Bid bid = new Bid(price, placed, new CustomerProxy(customerId), new ListingProxy(listingId));

            BidService.create(bid);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }

        LockManager.getInstance().releaseLock(listingId, req.getSession().getId());
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long customerId = Long.parseLong(req.getParameter("customerId"));
        long listingId = Long.parseLong(req.getParameter("listingId"));

        try {
            LockManager.getInstance().acquireLock(listingId, req.getSession().getId());
            DBConnection.newConnection();

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            Bid bid = BidService.get(customerId, listingId);
            if (bid == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            if (body.has("price")) bid.setBidPrice(body.getDouble("price"));
            bid.setTimeStamp(new Timestamp(System.currentTimeMillis()));

            BidService.update(bid);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }

        LockManager.getInstance().releaseLock(listingId, req.getSession().getId());
    }
}
