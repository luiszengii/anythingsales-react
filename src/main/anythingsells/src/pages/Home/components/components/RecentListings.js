import { Box, Button, ButtonBase, Divider, List, ListItem, ListItemText, Typography } from "@mui/material";
import { Fragment } from "react";
import { useNavigate } from "react-router-dom";

const RecentListings = (props) => {
  const navigate = useNavigate();

  return (
    <List sx={{ width: '100%', maxWidth: 500, bgcolor: 'background.paper' }}>
      <Typography variant="h6" align='left' gutterBottom>
        Recent Listings
        <Box sx={{ flexGrow: 1 }} />
        {(props.session.type === 'SELLER') && 
          <Button variant='outlined' onClick={() => {navigate('/listings')}}>View All</Button>
        }
      </Typography>
      <Divider />
      {
        props.listings
        .sort((a, b) => {
          return b.id - b.id;
        })
        .slice(0, 5)
        .map(listing => {
          return (
            <div>
              <ButtonBase
                onClick={() => {
                  if (listing.type === "AUCTION") {
                    navigate(`/view/auction/${listing.id}/`);
                  } else {
                    navigate(`/view/fixed_price/${listing.id}/`);
                  }
                }}
              >
                <ListItem alignItems="flex-start">
                  <ListItemText
                    primary={listing.name}
                    secondary={
                      <Fragment>
                        <Typography
                          sx={{ display: 'inline' }}
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          {listing.category}
                        </Typography>
                        {' - '}
                        <Typography
                          sx={{ display: 'inline' }}
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          {listing.type}
                        </Typography>
                      </Fragment>
                    }
                  />
                  <Divider/>
                </ListItem>
                <Divider/>
            </ButtonBase>
            <Divider/>
            </div>
          );
        })
      }
    </List>
  );
}

export default RecentListings;