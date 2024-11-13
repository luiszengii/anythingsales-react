import { useState } from "react";
import List from "@mui/icons-material/List";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useNavigate } from "react-router-dom";
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';

const RoleButton = (props) => {
  const [listingAnchorEl, setListingAnchorEl] = useState(null);
  const isListingMenuOpen = Boolean(listingAnchorEl);

  const navigate = useNavigate();

  const handleListingMenuOpen = (event) => {
    setListingAnchorEl(event.currentTarget);
  }

  const handleMenuClose = () => {
    setListingAnchorEl(null);
  };

  const handleListingsClick = () => {
    navigate('/listings');
  }

  // Customers
  const handlePurchasesClick = () => {
    navigate('/purchases');
  }

  const handleNewAuctionClick = () => {
    navigate('/new/auction');
  }

  const handleNewFixedPriceClick = () => {
    navigate('/new/fixed_price');
  }

  // Listing menu
  const listingMenu = 'listing-menu';
  const renderListingMenu = (
    <Menu
      anchorEl={listingAnchorEl}
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      id={listingMenu}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={isListingMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={handleListingsClick}>Listings</MenuItem>
      <MenuItem onClick={handleNewAuctionClick}>New Auction</MenuItem>
      <MenuItem onClick={handleNewFixedPriceClick}>New Fixed Price</MenuItem>
    </Menu>
  );

  return (
    <div>
      {props.session.type === "CUSTOMER" &&
        <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
          <IconButton
            size="large"
            edge="end"
            aria-label="current user purchases"
            aria-haspopup="true"
            onClick={handlePurchasesClick}
            color="inherit"
          >
            <ShoppingCartIcon />
          </IconButton>
        </Box>
      }
      {props.session.type === "SELLER" &&
        <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
          <IconButton
            size="large"
            edge="end"
            aria-label="current user listings"
            aria-controls={listingMenu}
            aria-haspopup="true"
            onClick={handleListingMenuOpen}
            color="inherit"
          >
            <List />
          </IconButton>
        </Box>
      }
      {renderListingMenu}
    </div>
  )
}

export default RoleButton;