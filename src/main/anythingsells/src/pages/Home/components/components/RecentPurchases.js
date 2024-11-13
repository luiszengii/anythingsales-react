import { Button, ButtonBase, Divider, List, ListItem, ListItemText, Typography } from "@mui/material";
import { Box } from "@mui/system";
import dayjs from "dayjs";
import { Fragment } from "react";
import { useNavigate } from "react-router-dom";

const RecentPurchases = (props) => {
  const navigate = useNavigate();

  return (
    <List sx={{ width: '100%', maxWidth: 500, bgcolor: 'background.paper' }}>
      <Typography variant="h6" align='left' gutterBottom>
        Recent Purchases
        <Box sx={{ flexGrow: 1 }} />
        {(props.session.type === 'CUSTOMER') && 
          <Button variant='outlined' onClick={() => {navigate('/purchases')}}>View All</Button>
        }
      </Typography>
      <Divider />
      {
        props.purchases
        .sort((a, b) => {
          return dayjs(`${b.placed}00`, 'YYYY-MM-DD HH:mm:ss.SSS').diff(dayjs(`${a.placed}00`, 'YYYY-MM-DD HH:mm:ss.SSS'));
        })
        .slice(0, 5)
        .map(purchase => {
          return (
            <div>
              <ButtonBase
                onClick={() => {
                  if (purchase.type === "AUCTION") {
                    navigate(`/view/bid/${purchase.customerId}/${purchase.listingId}/`);
                  } else {
                    navigate(`/view/order/${purchase.customerId}/${purchase.listingId}/`);
                  }
                }}
              >
                <ListItem alignItems="flex-start">
                  <ListItemText
                    primary={purchase.name}
                    secondary={
                      <Fragment>
                        <Typography
                          sx={{ display: 'inline' }}
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          {purchase.category}
                        </Typography>
                        {' - '}
                        <Typography
                          sx={{ display: 'inline' }}
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          {purchase.type}
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

export default RecentPurchases;