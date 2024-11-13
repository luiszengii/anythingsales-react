import TextField from '@mui/material/TextField';
import { useState } from "react";
import { createFixedPrice } from "../../../../api";
import Bar from '../../../../components/navigation/Bar';
import { useNavigate } from "react-router-dom";
import CreateListing from '../../components/CreateListing';
import { Snackbar } from '@mui/material';

const CreateFixedPrice = (props) => {
  const [data, setData] = useState(null);
  const navigate = useNavigate();

  const [error, setError] = useState(false);

  const handleSave = (e) => {
    e.preventDefault();
    createFixedPrice(data).then(() => {
      navigate('/listings', { replace: true });
    }).catch(() => {
      setError(true);
    });
  }

  const listingFields = (
    <div>
      <TextField
        style={{ width: '100%' }}
        required
        inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
        variant="standard"
        id="quantity"
        label="Quantity (format: integer)"
        onChange={e => setData({ ...data, quantity: Number(e.target.value) })}
      /><br/>
      <TextField
        required
        style={{ width: '100%' }}
        InputProps={{ inputProps: { step: "0.01" }}}
        inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
        variant="standard"
        id="price"
        label="Price (format: 2 decimal places)"
        onChange={e => setData({ ...data, price: e.target.value })}
      />
    </div>
  );

  return (
    <div>
      <Bar session={props.session}/>
      <CreateListing
        handleSave={handleSave}
        data={data}
        setData={setData}
        listingFields={listingFields}
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

export default CreateFixedPrice;