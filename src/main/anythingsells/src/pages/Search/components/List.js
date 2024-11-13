import React, { useState } from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import { DataGrid } from '@mui/x-data-grid';
import { useNavigate } from "react-router-dom";
import { Box, ButtonBase, Divider, List, ListItem, ListItemText, Typography } from "@mui/material";
import { Fragment } from "react";

import TabPanel from './TabPanel';

const listingColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'name', headerName: 'Name', flex: 3 },
  { field: 'category', headerName: 'Category', flex: 1 },
  { field: 'type', headerName: 'Type', flex: 1 }
];

const userColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'name', headerName: 'Username', flex: 3 },
  { field: 'type', headerName: 'Type', flex: 1 }
];

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function ListComponent(props) {
  const [value, setValue] = useState(0);

  const navigate = useNavigate();

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleListingClick = (params) => {
    if (params.row.type === 'AUCTION') {
      navigate(`/view/auction/${params.row.id}`);
    } else {
      navigate(`/view/fixed_price/${params.row.id}`);
    }
  }

  const handleUserClick = (params) => {
    navigate(`/view/user/${params.row.id}`);
  }

  const levenshtein = require('fast-levenshtein');

  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
          <Tab label="Relevant" {...a11yProps(0)} />
          <Tab label="Listings" {...a11yProps(1)} />
          <Tab label="Users" {...a11yProps(2)} />
        </Tabs>
      </Box>
      <TabPanel value={value} index={0}>
        <List sx={{height: '100%', paddingLeft: '15%', paddingRight: '15%'}}>
          <Divider />
          {
            props.combined
            .sort((a, b) => {
              return levenshtein.get(a.name, b.name)
            })
            .map(i => {
              return (
                <div>
                  <ButtonBase
                    onClick={() => {
                      if (i.type === "AUCTION") {
                        navigate(`/view/auction/${i.id}/`);
                      } else if (i.type === "FIXED_PRICE") {
                        navigate(`/view/fixed_price/${i.id}/`);
                      } else {
                        navigate(`/view/user/${i.id}/`);
                      }
                    }}
                  >
                    <ListItem alignItems="flex-start">
                      <ListItemText
                        primary={i.name}
                        secondary={
                          <div>
                            {(i.type === "AUCTION" || i.type === "FIXED_PRICE") &&
                              <Fragment>
                                <Typography
                                  sx={{ display: 'inline' }}
                                  component="span"
                                  variant="body2"
                                  color="text.primary"
                                >
                                  {i.category}
                                </Typography>
                                {' - '}
                                <Typography
                                  sx={{ display: 'inline' }}
                                  component="span"
                                  variant="body2"
                                  color="text.primary"
                                >
                                  {i.type}
                                </Typography>
                              </Fragment>
                            }
                            {(i.type === "SELLER" || i.type === "CUSTOMER" || i.type === "ADMIN") &&
                              <Fragment>
                                <Typography
                                  sx={{ display: 'inline' }}
                                  component="span"
                                  variant="body2"
                                  color="text.primary"
                                >
                                  {i.type}
                                </Typography>
                              </Fragment>
                            }
                          </div>
                        }
                      />
                      <Divider/>
                    </ListItem>
                    <Divider/>
                </ButtonBase>
                <Divider/>
                </div>
              );
            })
          }
        </List>
      </TabPanel>
      <TabPanel value={value} index={1}>
        <Box sx={{height: '100%', paddingLeft: '15%', paddingRight: '15%'}}>
          <DataGrid
            autoHeight={true}
            rows={props.listings}
            columns={listingColumns}
            onRowClick={handleListingClick}
            density='compact'
            disableColumnSelector
            disableColumnFilter
          />
        </Box>
      </TabPanel>
      <TabPanel value={value} index={2}>
        <Box sx={{height: '100%', paddingLeft: '15%', paddingRight: '15%'}}>
          <DataGrid
            autoHeight={true}
            rows={props.users}
            columns={userColumns}
            onRowClick={handleUserClick}
            density='compact'
            disableColumnSelector
            disableColumnFilter
          />
        </Box>
      </TabPanel>
    </Box>
  );
}