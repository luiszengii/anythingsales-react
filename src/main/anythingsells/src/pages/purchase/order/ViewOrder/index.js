import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { deleteOrder, fetchFixedPrice, fetchOrder } from '../../../../api';
import { styled } from '@mui/material/styles';

import Bar from "../../../../components/navigation/Bar";
import { Button, ButtonGroup, Chip, Snackbar } from '@mui/material';
import ViewPurchase from '../../components/ViewPurchase';

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const ViewOrder = (props) => {
  const { customerId, listingId } = useParams();
  const [order, setOrder] = useState({});
  const [listing, setListing] = useState({});

  const [error, setError] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchOrder(customerId, listingId).then(result => {
      setOrder(result.data);
    });
    fetchFixedPrice(listingId).then(result => {
      setListing(result.data);
    });
  }, [customerId, listingId]);

  const handleListingClick = () => {
    navigate(`/view/fixed_price/${listingId}`);
  }

  const handleCustomerClick = () => {
    navigate(`/view/user/${customerId}`);
  }

  const handleEditClick = () => {
    navigate(`/edit/order/${customerId}/${listingId}`);
  }

  const handleDeleteClick = (e) => {
    e.preventDefault();
    deleteOrder(customerId, listingId).then(() => {
      navigate(`/view/fixed_price/${listingId}`);
    }).catch(() => {
      setError(true);
    });
  }

  const listingInformation = (
    <div>
      <Div>{listing.name}</Div>
      <Chip label={listing.category} variant="outlined" /><br/>
      <Div>{`Quantity Remaining: ${listing.quantity}`}</Div>
      <Div>{`Price: ${listing.price}`}</Div>
    </div>
  )

  const purchaseInformation = (
    <div>
      <Div>{`Total Price: ${order.price}`}</Div>
      <Div>{`Quantity: ${order.quantity}`}</Div>
      <Div>{`Time placed: ${order.placed}`}</Div>
    </div>
  )

  const customerInformation = (
    <div>
      <Div>{`Customer Username: ${order.username}`}</Div>
      <Div>{`Customer Name: ${order.firstname} ${order.lastname}`}</Div>
      <Div>{`Customer Email: ${order.email}`}</Div>
      <Div>{`Customer Address: ${order.address}`}</Div>
      <Div>{`Contact Number: ${order.phone}`}</Div>
    </div>
  )

  const informationButtons = (
    <div>
      <ButtonGroup variant='outlined' aria-label='outlined button group'>
        <Button onClick={handleListingClick}>Listing</Button>
        <Button onClick={handleCustomerClick}>Customer</Button>
      </ButtonGroup>
    </div>
  )

  const purchaseButtons = (
    <div>
      <ButtonGroup variant='contained' aria-label='contained button group'>
        <Button onClick={handleEditClick}>Edit Order</Button>
        <Button onClick={handleDeleteClick}>Delete Order</Button>
      </ButtonGroup>
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

export default ViewOrder;