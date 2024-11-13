import { useState } from "react";
import IconButton from '@mui/material/IconButton';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import AccountCircle from '@mui/icons-material/AccountCircle';
import Login from "../../Login";
import Box from '@mui/material/Box';
import { logout } from "../../../api";
import { useNavigate } from "react-router-dom";

const AccountButton = (props) => {
  const [accountAnchorEl, setAccountAnchorEl] = useState(null);
  const [isLoginOpen, setIsLoginOpen] = useState(false);

  const navigate = useNavigate();

  const isAccountMenuOpen = Boolean(accountAnchorEl);

  const handleAccountMenuOpen = (event) => {
    setAccountAnchorEl(event.currentTarget);
  }

  const handleMenuClose = () => {
    setAccountAnchorEl(null);
  };

  const handleAccountClick = () => {
    navigate(`/view/user/${props.session.id}`);
  }

  const handleLoginClick = () => {
    setIsLoginOpen(true);
  }

  const handleLogoutClick = () => {
    logout().then(() => {
      window.location.reload();
    });
  }

  const accountMenu = 'account-menu';
  const renderAccountMenu = (
    <Menu
      anchorEl={accountAnchorEl}
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      id={accountMenu}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={isAccountMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={handleAccountClick}>Account</MenuItem>
      <MenuItem onClick={handleLogoutClick}>Logout</MenuItem>
    </Menu>
  );

  return (
    <div>
      {props.session.id &&
      <div>
        <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
        <IconButton
          size="large"
          edge="end"
          aria-label="account of current user"
          aria-controls={accountMenu}
          aria-haspopup="true"
          onClick={handleAccountMenuOpen}
          color="inherit"
        >
          <AccountCircle />
        </IconButton>
      </Box>
      {renderAccountMenu}
      </div>
      }

    {!props.session.id &&
      <div>
        <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
        <IconButton
          size="large"
          edge="end"
          onClick={handleLoginClick}
          color="inherit"
        >
          <AccountCircle />
        </IconButton>
      </Box>
      <Login
        open={isLoginOpen}
        setOpen={setIsLoginOpen}
        isLogin={true}
      />
      </div>
      }
    </div>
  )
}

export default AccountButton;