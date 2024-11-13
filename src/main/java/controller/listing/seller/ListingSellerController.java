package controller.listing.seller;

import domain.account.Account;
import domain.account.AccountType;
import domain.account.user.Seller;
import domain.listing.AuctionListing;
import domain.listing.FixedPriceListing;
import domain.listing.Listing;
import domain.listing.ListingType;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DBConnection;
import transaction.service.AccountService;
import transaction.service.AuctionService;
import transaction.service.FixedPriceService;
import transaction.service.ListingService;
import util.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/server/listing/seller")
public class ListingSellerController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long id = Long.parseLong(req.getParameter("id"));
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();

            List<Seller> sellers = ListingService.findSellers(id);

            assert sellers != null;
            if (sellers.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            List<Long> sellerIds = new ArrayList<>();
            for (Seller seller : sellers) {
                sellerIds.add(seller.getId());
            }

            List<Account> accounts = AccountService.getAll();
            assert accounts != null;
            for (Account account : accounts) {
                if (account.getType().equals(AccountType.SELLER) && !sellerIds.contains(account.getId())) {
                    JSONObject accountJson = new JSONObject();
                    accountJson.put("id", account.getId());
                    accountJson.put("username", account.getUsername());
                    array.put(accountJson);
                }
            }

            DBConnection.getConnection().commit();

            json.put("sellers", array);
            resp.setStatus(HttpServletResponse.SC_OK);
            API.sendResponse(resp, json);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            JSONObject body = API.readRequestBody(req);
            assert body != null;

            if (!(body.has("sellerId") && body.has("listingId"))) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                DBConnection.getConnection().close();
                return;
            }

            long sellerId = body.getLong("sellerId");
            long listingId = body.getLong("listingId");

            Account account = AccountService.get(sellerId);
            List<Seller> sellers = ListingService.findSellers(listingId);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }
            if (!account.getType().equals(AccountType.SELLER)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                DBConnection.getConnection().close();
                return;
            }
            assert sellers != null;
            if (sellers.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            ListingService.addSeller(sellerId, listingId);
            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DBConnection.newConnection();

            long sellerId = Long.parseLong(req.getParameter("sellerId"));
            long listingId = Long.parseLong(req.getParameter("listingId"));

            Account account = AccountService.get(sellerId);
            List<Seller> sellers = ListingService.findSellers(listingId);

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }
            if (!account.getType().equals(AccountType.SELLER)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                DBConnection.getConnection().close();
                return;
            }
            assert sellers != null;
            if (sellers.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                DBConnection.getConnection().close();
                return;
            }

            if (sellers.size() == 1) {
                Listing listing = ListingService.find(listingId);
                if (listing.getType().equals(ListingType.AUCTION)) {
                    AuctionService.delete((AuctionListing) listing);
                } else {
                    FixedPriceService.delete((FixedPriceListing) listing);
                }
            } else {
                ListingService.removeSeller(sellerId, listingId);
            }

            DBConnection.getConnection().commit();
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            DBConnection.getConnection().close();
        }
    }
}
