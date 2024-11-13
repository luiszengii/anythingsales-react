package controller.user;

import domain.account.Account;
import domain.account.AccountType;
import domain.account.user.User;
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

@WebServlet("/server/user")
public class UserController extends HttpServlet {
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
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject body = API.readRequestBody(req);
            assert body != null;

            Account account = AccountService.get(id);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            if (body.has("username")) account.setUsername(body.getString("username"));
            if (body.has("password")) account.setPassword(body.getString("password"));
            if (body.has("version")) account.setVersion(body.getInt("version"));

            if (!account.getType().equals(AccountType.ADMIN)) {
                User user = (User) account;
                if (body.has("firstname")) user.setFirstName(body.getString("firstname"));
                if (body.has("lastname")) user.setLastName(body.getString("lastname"));
                if (body.has("email")) user.setEmail(body.getString("email"));
                if (body.has("address")) user.setAddress(body.getString("address"));
                if (body.has("phone")) user.setPhoneNumber(body.getString("phone"));
            }

            AccountService.update(account);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }

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
            json.put("version", account.getVersion());

            if (!account.getType().equals(AccountType.ADMIN)) {
                User user = (User) account;
                json.put("firstname", user.getFirstName());
                json.put("lastname", user.getLastName());
                json.put("email", user.getEmail());
                json.put("address", user.getAddress());
                json.put("phone", user.getPhoneNumber());
            }

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

            long id = Long.parseLong(req.getParameter("id"));

            Account account = AccountService.get(id);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            AccountService.delete(account);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
