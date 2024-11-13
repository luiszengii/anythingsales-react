package controller.search;

import domain.account.Account;
import domain.listing.Listing;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.ListingService;
import transaction.service.AccountService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/search")
public class SearchController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            DBConnection.newConnection();

            List<Account> accounts = AccountService.find(request.getParameter("term"));
            List<Listing> listings = ListingService.find(request.getParameter("term"));
            JSONObject json = new JSONObject();

            JSONArray userArray = new JSONArray();
            assert accounts != null;
            for (Account account : accounts) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", account.getId());
                userJson.put("name", account.getUsername());
                userJson.put("type", account.getType().toString());

                userArray.put(userJson);
            }

            json.put("users", userArray);

            JSONArray listingArray = new JSONArray();
            assert listings != null;
            for (Listing listing : listings) {
                JSONObject listingJson = new JSONObject();
                listingJson.put("id", listing.getId());
                listingJson.put("name", listing.getName());
                listingJson.put("category", listing.getCategory());
                listingJson.put("type", listing.getType().toString());

                listingArray.put(listingJson);
            }

            json.put("listings", listingArray);

            DBConnection.getConnection().commit();
            response.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(response, json);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}