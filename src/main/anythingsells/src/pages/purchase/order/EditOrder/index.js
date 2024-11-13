import { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { fetchFixedPrice, fetchOrder, updateOrder } from '../../../../api';

import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';

import Bar from "../../../../components/navigation/Bar";
import { Grid, Snackbar, styled } from '@mui/material';

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const EditOrder = (props) => {
  const { customerId, listingId } = useParams();
  const [order, setOrder] = useState({});
  const [listing, setListing] = useState({});
  const [quantity, setQuantity] = useState(0);
  const [orderInfo, setOrderInfo] = useState({});

  const [error, setError] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchFixedPrice(listingId).then(result => {
      setListing(result.data);
    });
  }, [listingId]);

  useEffect(() => {
    fetchOrder(customerId, listingId).then(result => {
      setOrder(result.data);
      setOrderInfo(result.data);
      setQuantity(result.data.quantity);
    });
  }, [customerId, listingId]);

  const handleSave = (e) => {
    e.preventDefault();
    updateOrder(customerId, listingId, order).then(() => {
      navigate(`/view/order/${customerId}/${listingId}`, { replace: true });
    }).catch(() => {
      setError(true);
    });
  }

  return (
    <div>
      <Bar session={props.session}/>

      <Grid container style={{height: '100%', paddingLeft: '30%', paddingRight: '30%', paddingTop: '5%'}} spacing={2}>
        <Grid item xs={6}>
          <Div>{listing.name}</Div>
          <Div>{`Quantity Remaining: ${listing.quantity}`}</Div>
          <Div>{`Price: ${listing.price}`}</Div><br/>
          <Div>{`Total Price: ${orderInfo.price}`}</Div>
          <Div>{`Quantity: ${orderInfo.quantity}`}</Div>
          <Div>{`Time placed: ${orderInfo.placed}`}</Div><br/>
          <Div>{`Customer Username: ${orderInfo.username}`}</Div>
          <Div>{`Customer Name: ${orderInfo.firstname} ${orderInfo.lastname}`}</Div>
          <Div>{`Customer Email: ${orderInfo.email}`}</Div>
          <Div>{`Customer Address: ${orderInfo.address}`}</Div>
          <Div>{`Contact Number: ${orderInfo.phone}`}</Div>
        </Grid>
        <Grid item xs={6}>
          <form onSubmit={handleSave}>
            <TextField
              autoFocus
              margin="dense"
              id="quantity"
              label="Quantity"
              variant="standard"
              value={order.quantity}
              type="number"
              InputLabelProps={{ shrink: true }}
              style={{ width: '100%' }}
              InputProps={{ inputProps: { max: listing.quantity + quantity, min: 1 }}}
              inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
              onChange={e => setOrder({ ...order, quantity: e.target.value })}
            />
            <Button type='submit' fullWidth variant="contained">Save</Button>
          </form>
        </Grid>
      </Grid>

      <Snackbar
      open={error}
      autoHideDuration={6000}
      onClose={() => setError(false)}
      message="Please refresh page."
      />
    </div>
  )
}

export default EditOrder;