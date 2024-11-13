package controller.listing.auction;

import domain.listing.AuctionListing;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AuctionService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/server/view/auction")
public class ViewAuctionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();

            AuctionListing listing = AuctionService.get(id);

            if (listing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            json.put("id", listing.getId());
            json.put("name", listing.getName());
            json.put("description", listing.getDescription());
            json.put("category", listing.getCategory());
            json.put("created", listing.getTimeCreated().toString());
            json.put("type", listing.getType().toString());
            json.put("start", listing.getStartTime().toString());
            json.put("end", listing.getEndTime().toString());
            json.put("initial", listing.getInitialPrice());
            json.put("current", listing.getCurrentBid());
            json.put("version", listing.getVersion());

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
