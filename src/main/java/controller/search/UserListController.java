package controller.search;

import domain.account.Account;
import org.json.JSONArray;
import org.json.JSONObject;
import transaction.service.AccountService;
import util.API;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/server/list/users")
public class UserListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            List<Account> accounts = AccountService.getAll();
            JSONObject json = new JSONObject();

            JSONArray array = new JSONArray();
            for (Account account : accounts) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", account.getId());
                userJson.put("username", account.getUsername());
                userJson.put("type", account.getType());

                array.put(userJson);
            }

            json.put("users", array);

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
