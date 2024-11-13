import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

import Bar from "../../../../components/navigation/Bar";
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import { addSeller, deleteFixedPrice, deleteSeller, fetchAllSellers, fetchFixedPrice, fetchFixedPriceOrders, fetchOrderList, fetchSellers, createOrder } from '../../../../api';
import ViewListing from '../../components/ViewListing';
import { Snackbar, TextField } from '@mui/material';

const orderColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'username', headerName: 'Username', flex: 2 },
  { field: 'quantity', headerName: 'Quantity', flex: 1 },
  { field: 'price', headerName: 'Price', flex: 1 },
  { field: 'placed', headerName: 'Time Placed', flex: 1 }
];

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const ViewFixedPrice = (props) => {
  const { id } = useParams();

  const [listing, setListing] = useState({});
  const [sellers, setSellers] = useState([]);
  const [orders, setOrders] = useState([]);

  const [allSellers, setAllSellers] = useState([]);
  const [newSeller, setNewSeller] = useState(null);

  const [oldSeller, setOldSeller] = useState(null);

  const [isSeller, setIsSeller] = useState(false);
  const [isCustomer, setIsCustomer] = useState(false);

  const [order, setOrder] = useState({ quantity: 1 });

  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchFixedPrice(id).then(result => {
      setListing(result.data);
    });
    fetchSellers(id).then(result => {
      setSellers(result.data.sellers);
    });
  }, [id]);

  useEffect(() => {
    if (props.session.type === "SELLER" || props.session.type === "ADMIN") {
      let s = false;

      for (let i = 0; i < sellers.length; i++) {
        /* eslint eqeqeq: 0 */
        if (sellers[i].id == props.session.id) {
          s = true;
          break;
        }
      }

      if (s || props.session.type === "ADMIN") {
        fetchAllSellers(id).then(result => {
          setAllSellers(result.data.sellers);
        });
        fetchFixedPriceOrders(id).then(result => {
          setOrders(result.data.orders);
        });
      }

      setIsSeller(s);

    } else if (props.session.type === "CUSTOMER") {
      fetchOrderList(props.session.id).then(result => {
        for (let i = 0; i < result.data.orders.length; i++) {
          if (result.data.orders[i].id == id) {
            setIsCustomer(true);
            return;
          }
        }
      });
    }
  }, [id, props.session.id, props.session.type, sellers]);

  const handleUserClick = (params) => {
    navigate(`/view/user/${params.row.id}`);
  }

  const handleOrderClick = (params) => {
    navigate(`/view/order/${params.row.id}/${id}`);
  }

  const handleAddSeller = (e) => {
    e.preventDefault();
    addSeller({ sellerId: newSeller.id, listingId: id }).then(() => {
      setAllSellers(allSellers.filter(seller => seller.id !== newSeller.id));
      setSellers([ ...sellers, newSeller ]);
    }).catch(() => {
      setError(true);
    });
  }

  const handleDeleteSeller = (e) => {
    e.preventDefault();
    deleteSeller(oldSeller.id, id).then(() => {
      if (oldSeller.id === props.session.id) {
        navigate('/listings');
      } else {
        setAllSellers([ ...allSellers, oldSeller ]);
        setSellers(sellers.filter(seller => seller.id !== oldSeller.id));
      }
    }).catch(() => {
      setError(true);
    });
  }

  const handleDeleteListing = () => {
    deleteFixedPrice(id).then(() => {
      navigate('/listings');
    }).catch(() => {
      setError(true);
    });
  }

  const handleEditClick = () => {
    navigate(`/edit/fixed_price/${id}`);
  }

  const handleViewOrder = () => {
    navigate(`/view/order/${props.session.id}/${id}`);
  }

  const handleSave = (e) => {
    e.preventDefault();
    createOrder({ ...order, listingId: Number(id) }).then(() => {
      navigate(`/view/order/${props.session.id}/${id}`);
    }).catch(() => {
      setError(true);
    });
  }

  const listingDetails = (
    <div>
      <Div>{`Quantity Remaining: ${listing.quantity}`}</Div>
      <Div>{`Price: ${listing.price}`}</Div>
    </div>
  );

  const listingButtons = (
    <div>
      <Button variant='contained' fullWidth onClick={handleEditClick}>Edit Listing</Button><br/><br/>
      <Button variant='contained' fullWidth onClick={handleDeleteListing}>Delete Listing</Button>
    </div>
  );

  const puchaseButton = (
    <form onSubmit={handleSave}>
      <TextField
        autoFocus
        margin="dense"
        id="quantity"
        label="Quantity"
        variant="standard"
        value={order.quantity}
        type="number"
        InputProps={{ inputProps: { max: listing.quantity, min: 1 }}}
        inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
        onChange={e => setOrder({ ...order, quantity: e.target.value })}
        fullWidth
      /><br/>
      <Button variant='contained' fullWidth type='submit'>Order</Button>
    </form>
  );

  const viewPurchaseButton = (
    <Button variant='contained' fullWidth onClick={handleViewOrder}>View order</Button>
  );

  return (
    <div>
      <Bar session={props.session}/>

      <ViewListing
        session={props.session}
        listing={listing}
        sellers={sellers}
        handleUserClick={handleUserClick}
        isSeller={isSeller}
        handleAddSeller={handleAddSeller}
        handleDeleteSeller={handleDeleteSeller}
        allSellers={allSellers}
        setNewSeller={setNewSeller}
        setOldSeller={setOldSeller}
        purchaseTitle='Orders'
        purchases={orders}
        purchaseColumns={orderColumns}
        handlePurchaseClick={handleOrderClick}
        listingDetails={listingDetails}
        listingButtons={listingButtons}
        purchaseButton={puchaseButton}
        viewPurchaseButton={viewPurchaseButton}
        isCustomer={isCustomer}
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

export default ViewFixedPrice;