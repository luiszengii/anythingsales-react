import { useEffect, useState } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { createBid, fetchAuction } from '../../../../api';

import TextField from '@mui/material/TextField';

import Bar from "../../../../components/navigation/Bar";
import CreatePurchase from '../../components/CreatePurchase';
import { Snackbar } from '@mui/material';

const CreateBid = (props) => {
  const { listingId } = useParams();
  const [bid, setBid] = useState({});
  const [auction, setAuction] = useState({});

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchAuction(listingId).then(result => {
      setAuction(result.data);
      setBid({ price: result.data.current });
    });
  }, [listingId]);

  const navigate = useNavigate();

  const handleSave = (e) => {
    e.preventDefault();
    createBid({ ...bid, listingId: Number(listingId) }).then(() => {
      navigate(`/view/auction/${listingId}`);
    }).catch(() => {
      setError(true);
    });
  }

  const input = (
    <TextField
      autoFocus
      required
      margin="dense"
      id="price"
      label="Bid"
      variant="standard"
      value={bid.price}
      type="number"
      InputProps={{ inputProps: { min: auction.current + 0.01, step: "0.01" }}}
      inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
      onChange={e => setBid({ price: Number(e.target.value) })}
    />
  );

  return (
    <div>
      <Bar session={props.session}/>
      <CreatePurchase
        handleSave={handleSave}
        input={input}
        saveText={'Create Bid'}
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

export default CreateBid;