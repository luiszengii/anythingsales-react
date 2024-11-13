import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';

import HomeButton from './components/HomeButton';
import SearchBar from './components/SearchBar';
import RoleButton from './components/RoleButton';
import AccountButton from './components/AccountButton';

const Bar = (props) => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <HomeButton/>
          <Box sx={{ flexGrow: 1 }} />
          <SearchBar/>
          <Box sx={{ flexGrow: 1 }} />
          <RoleButton session={props.session} />
          <AccountButton session={props.session} />
        </Toolbar>
      </AppBar>
    </Box>
  );
}

export default Bar;