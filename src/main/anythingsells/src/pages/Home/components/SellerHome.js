import { ButtonBase, Card, CardContent, Grid, Skeleton, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchAuctionBids, fetchAuctionList, fetchFixedPriceList, fetchFixedPriceOrders, fetchRecentListings } from "../../../api";
import RecentListings from "./components/RecentListings";
import RecentPurchases from "./components/RecentPurchases";

const SellerHome = (props) => {
  const [listings, setListings] = useState([]);
  const [purchases, setPurchases] = useState([]);

  const [recent, setRecent] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    fetchRecentListings().then(result => {
      setRecent(result.data.listings);
    });
   }, []);

  useEffect(() => {
    fetchAuctionList(props.session.id).then(result => {
      setListings(l => [ ...l, ...result.data.auctions ]);

      for (let i = 0; i < result.data.auctions.length; i++) {
        fetchAuctionBids(result.data.auctions[i].id).then(r => {
          setPurchases(p => [ ...p, ...r.data.bids ]);
        });
      }
    });
    fetchFixedPriceList(props.session.id).then(result => {
      setListings(l => [ ...l, ...result.data.listings ]);

      for (let i = 0; i < result.data.listings.length; i++) {
        fetchFixedPriceOrders(result.data.listings[i].id).then(r => {
          setPurchases(p => [ ...p, ...r.data.orders ]);
        });
      }
    });
  }, [props.session.id]);

  return (
    <Grid container style={{height: '100%', paddingLeft: '10%', paddingRight: '10%', paddingTop: '2%'}} spacing={2}>
      <Grid item xs={6}>
        <Typography variant="h6" align='left' gutterBottom>
          Recent Listings from All Sellers:
        </Typography>
        <Grid container spacing={1} style={{textAlign: 'center', overflowY: "auto", maxHeight: "90vh"}}>
          {recent
          .map(listing => {
            return (
              <Grid item>
                <Card key={listing.id} sx={{ minWidth: 292 }}>
                  <ButtonBase
                    onClick={() => {
                      if (listing.type === "AUCTION") {
                        navigate(`/view/auction/${listing.id}`);
                      } else {
                        navigate(`/view/fixed_price/${listing.id}`);
                      }
                    }}
                  >
                    <CardContent>
                      <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                        {listing.type}
                      </Typography>
                      <Skeleton variant='rectangular' animation='false' width='100%' height={100} /><br/>
                      <Typography variant="h5" component="div">
                        {listing.name}
                      </Typography>
                      <Typography variant="body2">
                        {listing.category}
                      </Typography>
                    </CardContent>
                  </ButtonBase>
                </Card>
              </Grid>
            );
          })}
        </Grid>
      </Grid>
      <Grid item xs={6}>
        <RecentListings
          listings={listings}
          session={props.session}
        /><br/>
        <RecentPurchases
          purchases={purchases}
          session={props.session}
        />
      </Grid>
    </Grid>
  );
}

export default SellerHome;