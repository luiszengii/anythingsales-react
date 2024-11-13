package controller.user.seller;

import domain.account.Account;
import domain.listing.FixedPriceListing;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import transaction.service.FixedPriceService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/list/fixed-price-list")
public class ViewFixedPriceListController extends HttpServlet {
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
                return;
            }

            List<FixedPriceListing> fixedPriceListings = FixedPriceService.getSellerListings(id);
            assert fixedPriceListings != null;
            for (FixedPriceListing fixedPrice : fixedPriceListings) {
                JSONObject fixedPriceJson = new JSONObject();

                fixedPriceJson.put("id", fixedPrice.getId());
                fixedPriceJson.put("name", fixedPrice.getName());
                fixedPriceJson.put("description", fixedPrice.getDescription());
                fixedPriceJson.put("category", fixedPrice.getCategory());
                fixedPriceJson.put("quantity", fixedPrice.getQuantityWithOrders());
                fixedPriceJson.put("created", fixedPrice.getTimeCreated().toString());
                fixedPriceJson.put("type", fixedPrice.getType().toString());
                fixedPriceJson.put("price", fixedPrice.getPrice());

                array.put(fixedPriceJson);
            }

            DBConnection.getConnection().commit();

            json.put("listings", array);
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
