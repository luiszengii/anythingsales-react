package util;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class API {
    public static JSONObject readRequestBody(HttpServletRequest request) {
        try {
            Reader reader = request.getReader();
            JSONTokener token = new JSONTokener(reader);
            return new JSONObject(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendResponse(HttpServletResponse response, JSONObject json) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(json.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
