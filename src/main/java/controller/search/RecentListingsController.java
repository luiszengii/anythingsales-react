package controller.search;

import domain.listing.Listing;
import org.json.JSONArray;
import org.json.JSONObject;
import transaction.service.ListingService;
import util.API;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/list/recent")
public class RecentListingsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            List<Listing> listings = ListingService.findRecentListings();
            JSONObject json = new JSONObject();

            JSONArray array = new JSONArray();
            for (Listing listing : listings) {
                JSONObject listingJson = new JSONObject();
                listingJson.put("id", listing.getId());
                listingJson.put("name", listing.getName());
                listingJson.put("category", listing.getCategory());
                listingJson.put("type", listing.getType().toString());

                array.put(listingJson);
            }

            json.put("listings", array);

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
