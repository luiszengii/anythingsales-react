import './App.css';

import { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Home from './pages/Home';            // entry - shows search results
import Listings from './pages/role/Listings';    // seller listings list
import Purchases from './pages/role/Purchases';  // customer purchases list

import ViewBid from './pages/purchase/bid/ViewBid';
import EditBid from './pages/purchase/bid/EditBid';
import CreateBid from './pages/purchase/bid/CreateBid';

import ViewOrder from './pages/purchase/order/ViewOrder';
import EditOrder from './pages/purchase/order/EditOrder';
import CreateOrder from './pages/purchase/order/CreateOrder';

import ViewAuction from './pages/listing/auction/ViewAuction';
import EditAuction from './pages/listing/auction/EditAuction';
import CreateAuction from './pages/listing/auction/CreateAuction';

import EditFixedPrice from './pages/listing/fixedprice/EditFixedPriceListing';
import ViewFixedPrice from './pages/listing/fixedprice/ViewFixedPriceListing';
import CreateFixedPrice from './pages/listing/fixedprice/CreateFixedPriceListing';

import Search from './pages/Search';

import ViewProfile from './pages/profile/ViewProfile';
import EditProfile from './pages/profile/EditProfile';
import { fetchSession } from './api';
import EditPassword from './pages/profile/EditPassword';

function App() {
  const [session, setSession] = useState({});

  useEffect(() => {
    fetchSession().then(result => {
      setSession(result.data);
    });
  }, []);

  return (
    <div>
      <Router>
        <Routes>
          <Route path='/' element={<Home session={session}/>} />
          <Route path='/search/:term' element={<Search session={session}/>} />
          <Route path='/view/user/:id' element={<ViewProfile session={session}/>} />
          <Route path='/edit/user/:id' element={<EditProfile session={session}/>} />
          <Route path='/edit/password/:id' element={<EditPassword session={session}/>} />

          <Route path='/listings' element={<Listings session={session}/>} />
          <Route path='/purchases' element={<Purchases session={session}/>} />

          <Route path='/view/bid/:customerId/:listingId' element={<ViewBid session={session}/>} />
          <Route path='/new/bid/:customerId/:listingId' element={<CreateBid session={session}/>} />
          <Route path='/new/rebid/:customerId/:listingId' element={<EditBid session={session}/>} />

          <Route path='/view/order/:customerId/:listingId' element={<ViewOrder session={session}/>} />
          <Route path='/edit/order/:customerId/:listingId' element={<EditOrder session={session}/>} />
          <Route path='/new/order/:customerId/:listingId' element={<CreateOrder session={session}/>} />

          <Route path='/view/auction/:id' element={<ViewAuction session={session}/>} />
          <Route path='/edit/auction/:id' element={<EditAuction session={session}/>} />
          <Route path='/new/auction' element={<CreateAuction session={session}/>} />

          <Route path='/view/fixed_price/:id' element={<ViewFixedPrice session={session}/>} />
          <Route path='/edit/fixed_price/:id' element={<EditFixedPrice session={session}/>} />
          <Route path='/new/fixed_price' element={<CreateFixedPrice session={session}/>} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
