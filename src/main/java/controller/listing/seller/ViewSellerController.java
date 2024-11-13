package controller.listing.seller;

import domain.account.user.Seller;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.ListingService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/view/seller")
public class ViewSellerController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();
            JSONArray sellerArray = new JSONArray();

            List<Seller> sellers = ListingService.findSellers(id);

            assert sellers != null;
            if (sellers.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            for (Seller seller : sellers) {
                JSONObject sellerJson = new JSONObject();

                sellerJson.put("id", seller.getId());
                sellerJson.put("username", seller.getUsername());

                sellerArray.put(sellerJson);
            }

            DBConnection.getConnection().commit();

            json.put("sellers", sellerArray);
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
