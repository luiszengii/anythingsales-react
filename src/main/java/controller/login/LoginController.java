package controller.login;

import domain.account.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@WebServlet("/server/login")
public class LoginController extends HttpServlet {

    private static final String SECRET = "asdfqwerqe!@#$%#@$%_)((*&*Sdfsdfdfsdsfsdfsdfsdfsdfsdf";

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();
            HttpSession session = req.getSession();

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            if (!(body.has("username") && body.has("password"))) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                DBConnection.getConnection().close();
                return;
            }

            String username = body.getString("username");
            String password = body.getString("password");

            Account account = AccountService.login(username, password);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                DBConnection.getConnection().close();
                return;
            }

            session.setAttribute("username", username);
            session.setAttribute("id", account.getId());
            session.setAttribute("type", account.getType());

            //generate jwt token and sends back to client as in header "Authorization"
            String jwt = Jwts.builder()
                    .setIssuer("AnythingSells")
                    .setSubject(account.getUsername())
                    .setExpiration(new Date(new Date().getTime() + (1000 * 60 * 500)))//2 mins
                    .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .compact();

            Cookie token = new Cookie("jwt", jwt);
            token.setSecure(true);
            token.setHttpOnly(true);

            DBConnection.getConnection().commit();
            resp.addCookie(token);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }

}
