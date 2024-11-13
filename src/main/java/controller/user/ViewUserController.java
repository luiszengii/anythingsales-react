package controller.user;

import domain.account.Account;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/server/view/user")
public class ViewUserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();

            Account account = AccountService.get(id);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            json.put("id", account.getId());
            json.put("username", account.getUsername());
            json.put("type", account.getType().toString());

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
