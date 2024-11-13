import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

import Bar from "../../../../components/navigation/Bar";
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import { fetchAuction, deleteSeller, fetchAuctionBids, fetchSellers, addSeller, fetchAllSellers, deleteAuction, fetchBidList, createBid, fetchBid, updateBid } from '../../../../api';
import ViewListing from '../../components/ViewListing';
import dayjs from 'dayjs';
import { Alert, Snackbar, TextField } from '@mui/material';

const bidColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'username', headerName: 'Username', flex: 2 },
  { field: 'price', headerName: 'Bid', flex: 1 },
  { field: 'placed', headerName: 'Time Placed', flex: 1 }
];

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const ViewAuction = (props) => {
  const { id } = useParams();

  const [auction, setAuction] = useState({});
  const [sellers, setSellers] = useState([]);
  const [bids, setBids] = useState([]);

  const [allSellers, setAllSellers] = useState([]);
  const [newSeller, setNewSeller] = useState(null);

  const [oldSeller, setOldSeller] = useState(null);

  const [isSeller, setIsSeller] = useState(false);
  const [isCustomer, setIsCustomer] = useState(false);

  const [bid, setBid] = useState({});

  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchAuction(id).then(result => {
      setAuction(result.data);
      setBid({ price: result.data.current });
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
        fetchAuctionBids(id).then(result => {
          setBids(result.data.bids);
        });
      }

      setIsSeller(s);

    } else if (props.session.type === "CUSTOMER") {
      fetchBidList(props.session.id).then(result => {
        for (let i = 0; i < result.data.bids.length; i++) {
          if (result.data.bids[i].id == id) {
            setIsCustomer(true);
            return;
          }
        }
      });
    }
  }, [id, props.session.id, props.session.type, sellers]);

  useEffect(() => {
    fetchBid(props.session.id, id).then(result => {
      setBid(result.data);
    });
  }, [props.session.id, id, isCustomer]);

  const handleUserClick = (params) => {
    navigate(`/view/user/${params.row.id}`);
  }

  const handleBidClick = (params) => {
    navigate(`/view/bid/${params.row.id}/${id}`);
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

  const handleDeleteAuction = (e) => {
    e.preventDefault();
    deleteAuction(id).then(() => {
      navigate(`/listings`);
    }).catch(() => {
      setError(true);
    });
  }

  const handleEditClick = () => {
    navigate(`/edit/auction/${id}`);
  }

  const handleViewBid = () => {
    navigate(`/view/bid/${props.session.id}/${id}`);
  }

  const handleSave = (e) => {
    e.preventDefault();
    createBid({ ...bid, listingId: Number(id) }).then(() => {
      navigate(`/view/bid/${props.session.id}/${id}`);
    }).catch(() => {
      setError(true);
    });
  }

  const handleUpdate = (e) => {
    e.preventDefault();
    updateBid(props.session.id, id, bid).then(() => {
      navigate(`/view/bid/${props.session.id}/${id}`);
    }).catch(() => {
      setError(true);
    });
  }

  const listingDetails = (
    <div>
      <Div>{`Start Time: ${auction.start}`}</Div>
      <Div>{`End Time: ${auction.end}`}</Div>
      <Div>{`Initial Bid: ${auction.initial}`}</Div>
      <Div>{`Current Bid: ${auction.current}`}</Div>
    </div>
  );

  const listingButtons = (
    <div>
      <Button variant='contained' fullWidth onClick={handleEditClick}>Edit Auction</Button><br/><br/>
      <Button variant='contained' fullWidth onClick={handleDeleteAuction}>Delete Auction</Button>
    </div>
  );

  const puchaseButton = (
    <div>
    { (dayjs().isAfter(dayjs(`${auction.start}00`, 'YYYY-MM-DD HH:mm:ss.SSS')) && dayjs().isBefore(dayjs(`${auction.end}00`, 'YYYY-MM-DD HH:mm:ss.SSS'))) &&
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
          InputProps={{ inputProps: { min: auction.current + 0.01, step: "0.01" }}}
          inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
          onChange={e => setBid({ price: Number(e.target.value) })}
          fullWidth
        /><br/>
        <Button variant='contained' fullWidth type='submit'>Bid</Button>
      </form>
    }
    </div>
  );

  const viewPurchaseButton = (
    <div>
      {(bid.price === auction.current) &&
        <Alert severity="success">Currently winning auction.</Alert>
      }
      {(bid.price < auction.current) &&
        <Alert severity="error">Currently losing auction.</Alert>
      }
      <br/>
      <Button variant='contained' fullWidth onClick={handleViewBid}>View Bid</Button><br/>
      { (dayjs().isAfter(dayjs(`${auction.start}00`, 'YYYY-MM-DD HH:mm:ss.SSS')) && dayjs().isBefore(dayjs(`${auction.end}00`, 'YYYY-MM-DD HH:mm:ss.SSS'))) &&
      <form onSubmit={handleUpdate}>
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
          fullWidth
        /><br/>
        <Button variant='contained' fullWidth type='submit'>Bid</Button>
      </form>
      }
    </div>
  );

  return (
    <div>
      <Bar session={props.session}/>

      <ViewListing
        session={props.session}
        listing={auction}
        sellers={sellers}
        handleUserClick={handleUserClick}
        isSeller={isSeller}
        handleAddSeller={handleAddSeller}
        handleDeleteSeller={handleDeleteSeller}
        allSellers={allSellers}
        setNewSeller={setNewSeller}
        setOldSeller={setOldSeller}
        purchaseTitle='Bids'
        purchases={bids}
        purchaseColumns={bidColumns}
        handlePurchaseClick={handleBidClick}
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

export default ViewAuction;