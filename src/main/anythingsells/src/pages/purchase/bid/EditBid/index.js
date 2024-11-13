import { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { fetchAuction, updateBid } from '../../../../api';

import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';

import Bar from "../../../../components/navigation/Bar";
import { Snackbar } from '@mui/material';

const EditBid = (props) => {
  const { customerId, listingId } = useParams();
  const [bid, setBid] = useState({});
  const [listing, setListing] = useState({});

  const [error, setError] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchAuction(listingId).then(result => {
      setListing(result.data);
      setBid({ price: result.data.current });
    });
  }, [listingId]);

  const handleSave = (e) => {
    e.preventDefault();
    updateBid(customerId, listingId, bid).then(() => {
      navigate(`/view/auction/${listingId}`);
    }).catch(() => {
      setError(true);
    });
  }

  return (
    <div>
      <Bar session={props.session}/>
      <form onSubmit={handleSave}>
        <TextField
          autoFocus
          required
          margin="dense"
          id="price"
          label="Bid"
          variant="standard"
          value={bid.price}
          type="number"
          InputProps={{ inputProps: { min: listing.current + 0.01, step: "0.01" }}}
          inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
          onChange={e => setBid({ price: Number(e.target.value) })}
        />
        <Button type='submit'>Create Bid</Button>
      </form>

      <Snackbar
        open={error}
        autoHideDuration={6000}
        onClose={() => setError(false)}
        message="Please refresh page."
      />
    </div>
  )
}

export default EditBid;