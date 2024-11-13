package controller.security;

import org.json.JSONObject;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/server/session")
public class SessionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject json = new JSONObject();

        if (req.getSession().getAttribute("id") != null) {
            json.put("id", req.getSession().getAttribute("id"));
            json.put("type", req.getSession().getAttribute("type").toString());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        API.sendResponse(resp, json);
    }
}
