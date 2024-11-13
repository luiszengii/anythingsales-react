import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from "react-router-dom";
import { fetchUserSummary } from '../../../api';
import { styled } from '@mui/material/styles';

import Bar from "../../../components/navigation/Bar";
import UserListings from '../../../components/profile/UserListings';
import UserPurchases from '../../../components/profile/UserPurchases';
import { Button, Grid } from '@mui/material';

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const ViewProfile = (props) => {
  const { id } = useParams();

  const navigate = useNavigate();

  const [user, setUser] = useState({});

  useEffect(() => {
    fetchUserSummary(id).then(result => {
      setUser(result.data);
    });
  }, [id]);

  const handleEditClick = () => {
    navigate(`/edit/user/${id}`);
  }

  const handleEditPassword = () => {
    navigate(`/edit/password/${id}`);
  }

  return (
    <div>
      <Bar session={props.session}/>

      <Grid container style={{height: '100%', paddingLeft: '10%', paddingRight: '10%', paddingTop: '5%'}} spacing={10}>
        <Grid item xs={6}>
          <Div>Username: {user.username}</Div>
          <Div>Account type: {user.type}</Div><br/>

          {(props.session.type === "ADMIN" || props.session.id === Number(id)) &&
            <div>
              <Button variant='contained' style={{ width: '70%' }} onClick={handleEditClick}>Edit Profile</Button><br/><br/>
              <Button variant='contained' style={{ width: '70%' }} onClick={handleEditPassword}>Edit Password</Button>
            </div>
          }
        </Grid>
        <Grid item xs={6}>
          {user.type === "SELLER" &&
            <UserListings id={id} />
          }
          {(props.session.type === "ADMIN" && user.type === "CUSTOMER") &&
            <UserPurchases id={id} />
          }
        </Grid>
      </Grid>
    </div>
  )
}

export default ViewProfile;