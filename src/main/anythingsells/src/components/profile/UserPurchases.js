import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { DataGrid } from '@mui/x-data-grid';
import Box from "@mui/material/Box";
import { fetchBidList, fetchOrderList } from '../../api';

const purchaseColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'name', headerName: 'Name', flex: 3 },
  { field: 'price', headerName: 'Price', flex: 1 },
  { field: 'category', headerName: 'Category', flex: 1 }
];

const UserPurchases = (props) => {
  const [bids, setBids] = useState([]);
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (props.id != null) {
      fetchBidList(props.id).then(result => {
        setBids(result.data.bids);
      });
      fetchOrderList(props.id).then(result => {
        setOrders(result.data.orders);
      });
    }
  }, [props.id]);

  const handleBidClick = (params) => {
    navigate(`/view/bid/${props.id}/${params.row.id}`);
  }

  const handleOrderClick = (params) => {
    navigate(`/view/order/${props.id}/${params.row.id}`);
  }

  return (
    <Box sx={{height: '100%', paddingLeft: '15%', paddingRight: '15%'}}>
      <h3>Bids</h3>
      <DataGrid
        autoHeight={true}
        rows={bids}
        columns={purchaseColumns}
        onRowClick={handleBidClick}
        density='compact'
        disableColumnSelector
        disableColumnFilter
      />
      <h3>Orders</h3>
      <DataGrid
        autoHeight={true}
        rows={orders}
        columns={purchaseColumns}
        onRowClick={handleOrderClick}
        density='compact'
        disableColumnSelector
        disableColumnFilter
      />
    </Box>
  )
}

export default UserPurchases;