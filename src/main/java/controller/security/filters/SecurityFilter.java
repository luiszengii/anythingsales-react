package controller.security.filters;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter("/*")
public class SecurityFilter implements Filter {
    private static final String SECRET = "asdfqwerqe!@#$%#@$%_)((*&*Sdfsdfdfsdsfsdfsdfsdfsdfsdf";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        /* If there is no authorization header or if it doesn't have login credentials or a token
         * then there is no point of continuing */
        String authorization = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    authorization = cookie.getValue();
                }
            }
        }

        if (authorization != null) {
            /* exam if the signature is valid*/
            try {
                Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                        .requireIssuer("AnythingSells")
                        .build()
                        .parseClaimsJws(authorization);

                /* We can trust this token, let's proceed */
//                filterChain.doFilter(req, res);
//                return;

            }catch (JwtException e) {
                req.getSession().invalidate();
            }
        }

        filterChain.doFilter(req, res);
//
//        /* Extract the token or base64encoded login credentials */
//        String token = authorization.replaceAll("(Basic)|(Bearer)", "").trim();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
}