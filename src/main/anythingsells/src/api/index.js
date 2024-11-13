import axios from 'axios';

const API = axios.create({
  // baseURL: 'https://anything-sells.herokuapp.com/'
  baseURL: 'http://localhost:8080/'
});

export const fetchUserSummary = (id) => API.get(`server/view/user?id=${id}`);
export const fetchUser = (id) => API.get(`server/user?id=${id}`);
export const updateUser = (id, data) => API.patch(`server/user?id=${id}`, data);
export const deleteUser = (id) => API.delete(`server/user?id=${id}`);

export const fetchAuction = (id) => API.get(`server/view/auction?id=${id}`);
export const updateAuction = (id, data) => API.patch(`server/listing/auction?id=${id}`, data);
export const deleteAuction = (id) => API.delete(`server/listing/auction?id=${id}`);
export const createAuction = (data) => API.post(`server/listing/new/auction`, data);

export const fetchAuctionBids = (id) => API.get(`server/listing/auction/bid?id=${id}`);

export const fetchFixedPrice = (id) => API.get(`server/view/fixed-price?id=${id}`);
export const updateFixedPrice = (id, data) => API.patch(`server/listing/fixed-price?id=${id}`, data);
export const deleteFixedPrice = (id) => API.delete(`server/listing/fixed-price?id=${id}`);
export const createFixedPrice = (data) => API.post(`server/listing/new/fixed-price`, data);

export const fetchFixedPriceOrders = (id) => API.get(`server/listing/fixed-price/order?id=${id}`);

export const fetchAllSellers = (id) => API.get(`server/listing/seller?id=${id}`);

export const fetchSellers = (id) => API.get(`server/view/seller?id=${id}`);
export const addSeller = (data) => API.post(`server/listing/seller`, data);
export const deleteSeller = (sellerId, listingId) => API.delete(`server/listing/seller?sellerId=${sellerId}&listingId=${listingId}`);

export const fetchBid = (customerId, listingId) => API.get(`server/purchase/bid?customerId=${customerId}&listingId=${listingId}`);
export const deleteBid = (customerId, listingId) => API.delete(`server/purchase/bid?customerId=${customerId}&listingId=${listingId}`);
export const createBid = (data) => API.post(`server/purchase/new/bid`, data);
export const updateBid = (customerId, listingId, data) => API.patch(`server/purchase/new/bid?customerId=${customerId}&listingId=${listingId}`, data);

export const fetchOrder = (customerId, listingId) => API.get(`server/purchase/order?customerId=${customerId}&listingId=${listingId}`);
export const deleteOrder = (customerId, listingId) => API.delete(`server/purchase/order?customerId=${customerId}&listingId=${listingId}`);
export const updateOrder = (customerId, listingId, data) => API.patch(`server/purchase/order?customerId=${customerId}&listingId=${listingId}`, data);
export const createOrder = (data) => API.post(`server/purchase/new/order`, data);

export const fetchBidList = (id) => API.get(`server/user/bid-list?id=${id}`);
export const fetchOrderList = (id) => API.get(`server/user/order-list?id=${id}`);

export const fetchAuctionList = (id) => API.get(`server/list/auction-list?id=${id}`);
export const fetchFixedPriceList = (id) => API.get(`server/list/fixed-price-list?id=${id}`);

export const login = (data) => API.post(`server/login`, data);
export const logout = () => API.post(`server/logout`);
export const register = (data) => API.post(`server/register`, data);

export const fetchSession = () => API.get(`server/session`);

export const search = (term) => API.get(`server/search?term=${term}`);
export const fetchRecentListings = () => API.get(`server/list/recent`);
export const fetchAllUsers = () => API.get(`server/list/users`);