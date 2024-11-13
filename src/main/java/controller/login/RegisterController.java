package controller.login;

import domain.account.user.Customer;
import domain.account.user.Seller;
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

@WebServlet("/server/register")
public class RegisterController extends HttpServlet{
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            DBConnection.newConnection();

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            if (!(body.has("username") && body.has("password") && body.has("type"))) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                DBConnection.getConnection().close();
                return;
            }

            String username = body.getString("username");
            String password = body.getString("password");
            String type = body.getString("type");

            switch (type) {
                case "CUSTOMER":
                    AccountService.create(new Customer(username, password));
                    break;
                case "SELLER":
                    AccountService.create(new Seller(username, password));
                    break;
            }

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
