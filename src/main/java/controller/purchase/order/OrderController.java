package controller.purchase.order;

import domain.purchase.Order;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.OrderService;
import util.API;
import util.LockManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/server/purchase/order")
public class OrderController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        } else {
            this.doPatch(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long customerId = Long.parseLong(req.getParameter("customerId"));
        long listingId = Long.parseLong(req.getParameter("listingId"));

        try {
            LockManager.getInstance().acquireLock(listingId, req.getSession().getId());
            DBConnection.newConnection();

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            Order order = OrderService.get(customerId, listingId);
            if (order == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            if (body.has("quantity")) order.setQuantity(body.getInt("quantity"));
            if (body.has("version")) order.setVersion(body.getInt("version"));

            OrderService.update(order);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }

        LockManager.getInstance().releaseLock(listingId, req.getSession().getId());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long customerId = Long.parseLong(req.getParameter("customerId"));
            long listingId = Long.parseLong(req.getParameter("listingId"));
            JSONObject json = new JSONObject();

            Order order = OrderService.get(customerId, listingId);

            if (order == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            json.put("quantity", order.getQuantity());
            json.put("price", order.getTotalPrice());
            json.put("placed", order.getTimeStamp().toString());

            json.put("username", order.getCustomer().getUsername());
            json.put("firstname", order.getCustomer().getFirstName());
            json.put("lastname", order.getCustomer().getLastName());
            json.put("email", order.getCustomer().getEmail());
            json.put("address", order.getCustomer().getAddress());
            json.put("phone", order.getCustomer().getPhoneNumber());
            json.put("version", order.getVersion());

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

            Order order = OrderService.get(customerId, listingId);

            if (order == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            OrderService.delete(order);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
