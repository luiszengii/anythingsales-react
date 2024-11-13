package controller.listing.fixedprice;

import domain.listing.FixedPriceListing;
import domain.purchase.Order;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.FixedPriceService;
import transaction.service.OrderService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/listing/fixed-price/order")
public class FixedPriceOrderController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();
            JSONArray orderArray = new JSONArray();

            FixedPriceListing listing = FixedPriceService.get(id);

            if (listing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            List<Order> orders = OrderService.getFixedPriceOrders(id);

            assert orders != null;
            for (Order order : orders) {
                JSONObject orderJson = new JSONObject();

                orderJson.put("id", order.getCustomer().getId());
                orderJson.put("username", order.getCustomer().getUsername());
                orderJson.put("customerId", order.getCustomer().getId());
                orderJson.put("listingId", order.getListing().getId());

                orderJson.put("quantity", order.getQuantity());
                orderJson.put("price", order.getTotalPrice());
                orderJson.put("placed", order.getTimeStamp().toString());
                orderJson.put("name", order.getListing().getName());
                orderJson.put("category", order.getListing().getCategory());
                orderJson.put("type", order.getListing().getType().toString());

                orderArray.put(orderJson);
            }

            DBConnection.getConnection().commit();

            json.put("orders", orderArray);
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
