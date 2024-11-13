package controller.listing.auction;

import datasource.proxy.NewSellerListProxy;
import domain.account.Account;
import domain.account.AccountType;
import domain.account.user.Seller;
import domain.listing.AuctionListing;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import transaction.service.AuctionService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/server/listing/new/auction")
public class CreateAuctionController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            if (req.getSession().getAttribute("id") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                DBConnection.getConnection().close();
                return;
            }

            long id = (Long) req.getSession().getAttribute("id");

            Account account = AccountService.get(id);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            if (!account.getType().equals(AccountType.SELLER)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                DBConnection.getConnection().close();
                return;
            }

            Seller seller = (Seller) account;

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            String name = "";
            String description = "";
            String category = "";
            Timestamp start = new Timestamp(0);
            Timestamp end = new Timestamp(0);
            double initial = 0;

            if (body.has("name")) name = body.getString("name");
            if (body.has("description")) description = body.getString("description");
            if (body.has("category")) category = body.getString("category");
            if (body.has("start")) start = Timestamp.valueOf(body.getString("start"));
            if (body.has("end")) end = Timestamp.valueOf(body.getString("end"));
            if (body.has("initial")) initial = body.getDouble("initial");

            AuctionListing auction = new AuctionListing(name, description, category, initial, start, end, new NewSellerListProxy(seller));

            AuctionService.create(auction);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
