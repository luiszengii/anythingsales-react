package controller.security.filters;

import domain.account.AccountType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/server/user/*")
public class ManageUserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        long id = Long.parseLong(req.getParameter("id"));
        AccountType type = (AccountType) req.getSession().getAttribute("type");

        if (!(id == (long) req.getSession().getAttribute("id") || type.equals(AccountType.ADMIN))) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(req, res);
    }
}
