import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Sellers from "./components/Sellers";
import EditSeller from "./components/EditSeller";
import ListingPurchases from "./components/ListingPurchases";
import { ButtonBase, Card, CardContent, Skeleton, Typography } from '@mui/material';
import { useEffect } from 'react';
import { useState } from 'react';
import { fetchAuctionList, fetchFixedPriceList } from '../../../../api';
import { useNavigate } from 'react-router-dom';

const ViewListing = (props) => {
  const [listings, setListings] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    for (let i = 0; i < props.sellers.length; i++) {
      fetchAuctionList(props.sellers[i].id).then(result => {
        setListings(l => [ ...l, ...result.data.auctions ]);
      });
      fetchFixedPriceList(props.sellers[i].id).then(result => {
        setListings(l => [ ...l, ...result.data.listings ]);
      });
    }
  }, [props.sellers]);

  return (
    <div>
      <Grid container style={{height: '100%', padding: '1%'}} spacing={2}>
        <Grid item xs={8} style={{textAlign: 'center', overflowX: "auto", maxHeight: "90vh"}}>
          <h1>{props.listing.name}</h1>
          <Chip label={props.listing.category} variant="outlined" /><br/><br/>
          <Skeleton variant='rectangular' animation='false' width='100%' height={500} /><br/>
          <Typography variant="body1" align='left' gutterBottom>
            {props.listing.description}
          </Typography><br/>

          <Typography variant="h6" align='left' gutterBottom>
            Other listings from the sellers:
          </Typography>
          <Grid container spacing={1}>
            {listings
            .filter(listing => {
              return listing.id !== props.listing.id;
            })
            .sort((a, b) => {
              return b - a;
            })
            .map(listing => {
              return (
                <Grid item>
                  <Card key={listing.id} sx={{ minWidth: 275 }}>
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
        
        <Grid item xs={4}>
          <br/>
          <Paper
            sx={{
              width: '90%',
              maxHeight: "90vh",
              padding: '2%',
              backgroundColor: (theme) =>
                theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
            }}
          >
            <div>{props.listingDetails}</div>
            
            {(props.isSeller || props.session.type === "ADMIN") &&
              <div>{props.listingButtons}</div>
            }
            {(!props.isCustomer && props.session.type === "CUSTOMER") &&
              <div>{props.purchaseButton}</div>
            }
            {(props.isCustomer && props.session.type === "CUSTOMER") &&
              <div>{props.viewPurchaseButton}</div>
            }

          <br/>
          <Sellers
            sellers={props.sellers}
            handleUserClick={props.handleUserClick}
          />
          <br/>

          {(props.isSeller || props.session.type === "ADMIN") &&
            <div>
              <Grid container spacing={2} margin='0'>
                <EditSeller
                  title='Add Seller'
                  handleSeller={props.handleAddSeller}
                  sellerList={props.allSellers}
                  selectTitle='All Sellers'
                  setSeller={props.setNewSeller}
                  buttonText='Add'
                />
                <EditSeller
                  title='Delete Seller'
                  handleSeller={props.handleDeleteSeller}
                  sellerList={props.sellers}
                  selectTitle='Current Sellers'
                  setSeller={props.setOldSeller}
                  buttonText='Delete'
                />
              </Grid>

              <br/>

              <ListingPurchases
                title={props.purchaseTitle}
                purchases={props.purchases}
                columns={props.purchaseColumns}
                handlePurchaseClick={props.handlePurchaseClick}
              />
            </div>
          }
          </Paper>
        </Grid>
      </Grid>
    </div>
  )
}

export default ViewListing;