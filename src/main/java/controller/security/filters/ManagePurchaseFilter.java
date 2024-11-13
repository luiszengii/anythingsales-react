package controller.security.filters;

import domain.account.AccountType;
import domain.account.user.Seller;
import util.DBConnection;
import transaction.service.ListingService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/server/purchase/*")
public class ManagePurchaseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        try {
            DBConnection.newConnection();

            AccountType type = (AccountType) req.getSession().getAttribute("type");

            if (type.equals(AccountType.CUSTOMER) && req.getParameter("customerId") != null) {
                long customerId = Long.parseLong(req.getParameter("customerId"));
                if ((long) req.getSession().getAttribute("id") != customerId) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    DBConnection.getConnection().close();
                    return;
                }
            }

            if (type.equals(AccountType.SELLER)) {
                long listingId = Long.parseLong(req.getParameter("listingId"));

                List<Seller> sellers = ListingService.findSellers(listingId);

                List<Long> sellerIds = new ArrayList<>();
                assert sellers != null;
                for (Seller seller : sellers) {
                    sellerIds.add(seller.getId());
                }

                if (!sellerIds.contains((long) req.getSession().getAttribute("id"))) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    DBConnection.getConnection().close();
                    return;
                }
            }

            DBConnection.getConnection().close();
            filterChain.doFilter(req, res);

        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
