package controller.purchase.bid;

import domain.purchase.Bid;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.BidService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/server/purchase/bid")
public class BidController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long customerId = Long.parseLong(req.getParameter("customerId"));
            long listingId = Long.parseLong(req.getParameter("listingId"));
            JSONObject json = new JSONObject();

            Bid bid = BidService.get(customerId, listingId);

            if (bid == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            json.put("price", bid.getBidPrice());
            json.put("placed", bid.getTimeStamp().toString());

            json.put("username", bid.getCustomer().getUsername());
            json.put("firstname", bid.getCustomer().getFirstName());
            json.put("lastname", bid.getCustomer().getLastName());
            json.put("email", bid.getCustomer().getEmail());
            json.put("address", bid.getCustomer().getAddress());
            json.put("phone", bid.getCustomer().getPhoneNumber());

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long customerId = Long.parseLong(req.getParameter("customerId"));
            long listingId = Long.parseLong(req.getParameter("listingId"));

            Bid bid = BidService.get(customerId, listingId);

            if (bid == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            BidService.delete(bid);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
