import { Box, Button, ButtonBase, Card, CardContent, Grid, Skeleton, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchRecentListings } from "../../../api";
import Login from "../../../components/Login";

const DefaultHome = (props) => {
  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [recent, setRecent] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    fetchRecentListings().then(result => {
      setRecent(result.data.listings);
    });
   }, []);

  return (
    <div>
      <Box
        maxWidth
        textAlign='center'
      >
        <br/><br/>
        <h1>AnythingSells by AnythingWorks</h1>
        <h3>Create an Account</h3>
        <Button variant="contained" onClick={() => setIsLoginOpen(true)}>Get Started</Button>
      </Box>
      <Login
        open={isLoginOpen}
        setOpen={setIsLoginOpen}
        isLogin={false}
      />
      <br/><br/><br/>
      <Grid container spacing={4} style={{textAlign: 'center', overflowY: "auto", maxHeight: "90vh"}}>
        {recent
        .map(listing => {
          return (
            <Grid item>
              <Card key={listing.id} sx={{ minWidth: 292 }}>
                <ButtonBase
                  onClick={() => {
                    if (listing.type === "AUCTION") {
                      navigate(`/view/auction/${listing.id}`);
                    } else {
                      navigate(`/view/fixed_price/${listing.id}`);
                    }
                  }}
                >
                  <CardContent>
                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                      {listing.type}
                    </Typography><br/>
                    <Skeleton variant='rectangular' animation='false' width='100%' height={100} /><br/>
                    <Typography variant="h5" component="div">
                      {listing.name}
                    </Typography>
                    <Typography variant="body2">
                      {listing.category}
                    </Typography>
                  </CardContent>
                </ButtonBase>
              </Card>
            </Grid>
          );
        })}
      </Grid>
    </div>
  );
}

export default DefaultHome;