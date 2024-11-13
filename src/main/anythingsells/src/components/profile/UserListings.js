import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { DataGrid } from '@mui/x-data-grid';
import Box from "@mui/material/Box";
import { fetchAuctionList, fetchFixedPriceList } from '../../api';

const listingColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'name', headerName: 'Name', flex: 3 },
  { field: 'category', headerName: 'Category', flex: 1 }
];

const UserListings = (props) => {
  const [auctions, setAuctions] = useState([]);
  const [fixedPriceListings, setFixedPriceListings] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (props.id != null) {
      fetchAuctionList(props.id).then(result => {
        setAuctions(result.data.auctions);
      });
      fetchFixedPriceList(props.id).then(result => {
        setFixedPriceListings(result.data.listings);
      });
    }
  }, [props.id]);

  const handleAuctionClick = (params) => {
    navigate(`/view/auction/${params.row.id}`);
  }

  const handleListingClick = (params) => {
    navigate(`/view/fixed_price/${params.row.id}`);
  }

  return (
    <Box sx={{height: '100%', paddingLeft: '15%', paddingRight: '15%'}}>
      <h3>Auction Listings</h3>
      <DataGrid
        autoHeight={true}
        rows={auctions}
        columns={listingColumns}
        onRowClick={handleAuctionClick}
        density='compact'
        disableColumnSelector
        disableColumnFilter
      />
      <h3>Fixed Price Listings</h3>
      <DataGrid
        autoHeight={true}
        rows={fixedPriceListings}
        columns={listingColumns}
        onRowClick={handleListingClick}
        density='compact'
        disableColumnSelector
        disableColumnFilter
      />
    </Box>
  )
}

export default UserListings;