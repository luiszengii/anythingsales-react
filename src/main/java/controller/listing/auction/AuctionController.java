package controller.listing.auction;

import domain.listing.AuctionListing;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AuctionService;
import util.API;
import util.LockManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/server/listing/auction")
public class AuctionController extends HttpServlet {
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
        long id = Long.parseLong(req.getParameter("id"));

        try {
            LockManager.getInstance().acquireLock(id, req.getSession().getId());
            DBConnection.newConnection();

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            AuctionListing listing = AuctionService.get(id);

            if (listing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            if (body.has("name")) listing.setName(body.getString("name"));
            if (body.has("description")) listing.setDescription(body.getString("description"));
            if (body.has("category")) listing.setCategory(body.getString("category"));
            if (body.has("start")) listing.setStartTime(Timestamp.valueOf(body.getString("start")));
            if (body.has("end")) listing.setEndTime(Timestamp.valueOf(body.getString("end")));
            if (body.has("initial")) listing.setInitialPrice(body.getDouble("initial"));
            if (body.has("version")) listing.setVersion(body.getInt("version"));

            AuctionService.update(listing);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }

        LockManager.getInstance().releaseLock(id, req.getSession().getId());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));

            AuctionListing listing = AuctionService.get(id);

            if (listing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            AuctionService.delete(listing);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
