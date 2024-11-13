package controller.user.seller;

import domain.account.Account;
import domain.listing.AuctionListing;
import org.json.JSONArray;
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
import java.util.List;

@WebServlet("/server/list/auction-list")
public class ViewAuctionListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();

            Account account = AccountService.get(id);
            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            List<AuctionListing> auctions = AuctionService.getSellerAuctions(id);
            assert auctions != null;
            for (AuctionListing auction : auctions) {
                JSONObject auctionJson = new JSONObject();

                auctionJson.put("id", auction.getId());
                auctionJson.put("name", auction.getName());
                auctionJson.put("description", auction.getDescription());
                auctionJson.put("category", auction.getCategory());
                auctionJson.put("created", auction.getTimeCreated().toString());
                auctionJson.put("type", auction.getType().toString());
                auctionJson.put("start", auction.getStartTime().toString());
                auctionJson.put("end", auction.getEndTime().toString());
                auctionJson.put("initial", auction.getInitialPrice());
                auctionJson.put("current", auction.getCurrentBid());

                array.put(auctionJson);
            }

            DBConnection.getConnection().commit();

            json.put("auctions", array);
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
