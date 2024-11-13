import TextField from '@mui/material/TextField';
import { useState, useEffect } from "react";
import { updateFixedPrice, fetchFixedPrice } from "../../../../api";
import Bar from '../../../../components/navigation/Bar';
import { useNavigate, useParams } from "react-router-dom";
import EditListing from '../../components/EditListing';
import { Chip, Snackbar, styled } from '@mui/material';

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const EditFixedPrice = (props) => {
  const { id } = useParams();

  const [data, setData] = useState({});
  const [quantity, setQuantity] = useState(0);
  const [listing, setListing] = useState({});
  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchFixedPrice(id).then(result => {
      setData(result.data);
      setQuantity(result.data.quantity);
      setListing(result.data);
    });
  }, [id]);

  const handleSave = (e) => {
    e.preventDefault();
    updateFixedPrice(id, { ...data, quantity: quantity - data.quantity }).then(() => {
      navigate(`/view/fixed_price/${id}`, { replace: true });
    }).catch(() => {
      setError(true);
    });
  }

  const listingInfo = (
    <div>
      <Div>{listing.name}</Div>
      <Chip label={listing.category} variant="outlined" /><br/>
      <Div>{`Quantity Remaining: ${listing.quantity}`}</Div>
      <Div>{`Price: ${listing.price}`}</Div>
    </div>
  )

  const listingFields = (
    <div>
      <TextField
        style={{ width: '100%' }}
        required
        InputLabelProps={{ shrink: true }}
        inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
        variant="standard"
        id="quantity"
        label="Quantity Remaining (format: integer)"
        value={quantity}
        onChange={e => setQuantity(Number(e.target.value))}
      /><br/>
      <TextField
        required
        InputLabelProps={{ shrink: true }}
        style={{ width: '100%' }}
        InputProps={{ inputProps: { step: "0.01" }}}
        inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
        variant="standard"
        id="price"
        label="Price (format: 2 decimal places)"
        value={data.price}
        onChange={e => setData({ ...data, price: e.target.value })}
      />
    </div>
  );

  return (
    <div>
      <Bar session={props.session}/>

      <EditListing
        handleSave={handleSave}
        data={data}
        setData={setData}
        listingFields={listingFields}
        listing={listingInfo}
      />

      <Snackbar
        open={error}
        autoHideDuration={6000}
        onClose={() => setError(false)}
        message="Please refresh page."
      />
    </div>
  );
}

export default EditFixedPrice;