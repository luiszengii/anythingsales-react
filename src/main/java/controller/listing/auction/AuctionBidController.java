package controller.listing.auction;

import domain.listing.AuctionListing;
import domain.purchase.Bid;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AuctionService;
import transaction.service.BidService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/listing/auction/bid")
public class AuctionBidController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();
            JSONArray bidArray = new JSONArray();

            AuctionListing auctionListing = AuctionService.get(id);

            if (auctionListing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            List<Bid> bids = BidService.getAuctionBids(id);

            assert bids != null;
            for (Bid bid : bids) {
                JSONObject bidJson = new JSONObject();

                bidJson.put("id", bid.getCustomer().getId());
                bidJson.put("username", bid.getCustomer().getUsername());
                bidJson.put("customerId", bid.getCustomer().getId());
                bidJson.put("listingId", bid.getListing().getId());

                bidJson.put("price", bid.getBidPrice());
                bidJson.put("placed", bid.getTimeStamp().toString());
                bidJson.put("name", bid.getListing().getName());
                bidJson.put("category", bid.getListing().getCategory());
                bidJson.put("type", bid.getListing().getType().toString());

                bidArray.put(bidJson);
            }

            DBConnection.getConnection().commit();

            json.put("bids", bidArray);
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
