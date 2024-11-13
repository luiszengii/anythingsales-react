import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { deleteBid, fetchAuction, fetchBid } from '../../../../api';
import { styled } from '@mui/material/styles';

import Bar from "../../../../components/navigation/Bar";
import { Alert, Button, ButtonGroup, Chip, Snackbar } from '@mui/material';
import ViewPurchase from '../../components/ViewPurchase';

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const ViewBid = (props) => {
  const { customerId, listingId } = useParams();
  const [bid, setBid] = useState({});
  const [auction, setAuction] = useState({});

  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchBid(customerId, listingId).then(result => {
      setBid(result.data);
    });
    fetchAuction(listingId).then(result => {
      setAuction(result.data);
    });
  }, [customerId, listingId]);

  const handleListingClick = () => {
    navigate(`/view/auction/${listingId}`);
  }

  const handleCustomerClick = () => {
    navigate(`/view/user/${customerId}`);
  }

  const handleDeleteClick = (e) => {
    e.preventDefault();
    deleteBid(customerId, listingId).then(() => {
      navigate(`/view/auction/${listingId}`);
    }).catch(() => {
      setError(true);
    });
  }

  const listingInformation = (
    <div>
      <Div>{auction.name}</Div>
      <Chip label={auction.category} variant="outlined" /><br/>
      <Div>{`Start Time: ${auction.start}`}</Div>
      <Div>{`End Time: ${auction.end}`}</Div>
      <Div>{`Initial Bid: ${auction.initial}`}</Div>
      <Div>{`Current Bid: ${auction.current}`}</Div>
    </div>
  )

  const purchaseInformation = (
    <div>
      {(bid.price === auction.current) &&
        <Alert severity="success">Currently winning auction.</Alert>
      }
      {(bid.price !== auction.current) &&
        <Alert severity="error">Currently losing auction.</Alert>
      }
      <Div>{`Bid: ${bid.price}`}</Div>
      <Div>{`Time placed: ${bid.placed}`}</Div>
    </div>
  )

  const customerInformation = (
    <div>
      <Div>{`Customer Username: ${bid.username}`}</Div>
      <Div>{`Customer Name: ${bid.firstname} ${bid.lastname}`}</Div>
      <Div>{`Customer Email: ${bid.email}`}</Div>
      <Div>{`Customer Address: ${bid.address}`}</Div>
      <Div>{`Contact Number: ${bid.phone}`}</Div>
    </div>
  )

  const informationButtons = (
    <div>
      <ButtonGroup variant='outlined' aria-label='outlined button group'>
        <Button onClick={handleListingClick}>Auction</Button>
        <Button onClick={handleCustomerClick}>Customer</Button>
      </ButtonGroup>
    </div>
  )

  const purchaseButtons = (
    <div>
      <Button variant='contained' onClick={handleDeleteClick}>Delete Bid</Button>
    </div>
  )

  return (
    <div>
      <Bar session={props.session}/>
      <ViewPurchase 
        listingInformation={listingInformation}
        purchaseInformation={purchaseInformation}
        customerInformation={customerInformation}
        informationButtons={informationButtons}
        purchaseButtons={purchaseButtons}
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

export default ViewBid;