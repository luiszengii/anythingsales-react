import { Grid } from "@mui/material";

const ViewPurchase = (props) => {
  return (
    <Grid container style={{height: '100%', paddingLeft: '30%', paddingRight: '30%', paddingTop: '5%'}} spacing={2}>
      <Grid item xs={6}>
        <div>{props.informationButtons}</div><br/>
        <div>{props.listingInformation}</div>
      </Grid>
      <Grid item xs={6}>
        <div>{props.purchaseButtons}</div><br/>
        <div>{props.purchaseInformation}</div><br/>
        <div>{props.customerInformation}</div>
      </Grid>
    </Grid>
  )
}

export default ViewPurchase;