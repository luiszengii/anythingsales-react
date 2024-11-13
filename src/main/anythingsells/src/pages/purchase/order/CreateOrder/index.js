import { useEffect, useState } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { createOrder, fetchFixedPrice } from '../../../../api';

import TextField from '@mui/material/TextField';

import Bar from "../../../../components/navigation/Bar";
import CreatePurchase from '../../components/CreatePurchase';
import { Snackbar } from '@mui/material';

const CreateOrder = (props) => {
  const { listingId } = useParams();
  const [order, setOrder] = useState({ quantity: 1 });
  const [listing, setListing] = useState({});

  const [error, setError] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchFixedPrice(listingId).then(result => {
      setListing(result.data);
    });
  }, [listingId]);

  const handleSave = (e) => {
    e.preventDefault();
    createOrder({ ...order, listingId: Number(listingId) }).then(() => {
      navigate(`/view/fixed_price/${listingId}`);
    }).catch(() => {
      setError(true);
    });
  }

  const input = (
    <TextField
      autoFocus
      required
      margin="dense"
      id="quantity"
      label="Quantity"
      variant="standard"
      value={order.quantity}
      type="number"
      InputProps={{ inputProps: { max: listing.quantity, min: 1 }}}
      inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
      onChange={e => setOrder({ ...order, quantity: e.target.value })}
    />
  );

  return (
    <div>
      <Bar session={props.session}/>
      <CreatePurchase
        handleSave={handleSave}
        input={input}
        saveText={'Create Order'}
      />

      <Snackbar
        open={error}
        autoHideDuration={6000}
        onClose={() => setError(false)}
        message="Please refresh page."
      />
    </div>
  )
}

export default CreateOrder;