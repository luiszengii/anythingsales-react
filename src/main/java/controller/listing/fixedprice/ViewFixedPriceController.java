package controller.listing.fixedprice;

import domain.listing.FixedPriceListing;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.FixedPriceService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/server/view/fixed-price")
public class ViewFixedPriceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();

            FixedPriceListing listing = FixedPriceService.get(id);

            if (listing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            json.put("id", listing.getId());
            json.put("name", listing.getName());
            json.put("description", listing.getDescription());
            json.put("category", listing.getCategory());
            json.put("quantity", listing.getQuantityWithOrders());
            json.put("created", listing.getTimeCreated().toString());
            json.put("type", listing.getType().toString());
            json.put("price", listing.getPrice());
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
