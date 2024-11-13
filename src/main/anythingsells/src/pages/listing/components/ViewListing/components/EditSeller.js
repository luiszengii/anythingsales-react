import { Autocomplete, Button, Grid, TextField } from "@mui/material";

const EditSeller = (props) => {
  return (
    <Grid item xs={6} style={{height: '100%', overflow: 'hidden', textAlign: 'center'}}>
      <form onSubmit={props.handleSeller}>
        <Autocomplete
          required
          disablePortal
          id="seller-search"
          isOptionEqualToValue={(option, value) => option.username === value.username}
          getOptionLabel={(option) => option.username}
          options={props.sellerList}
          sx={{ width: '85%' }}
          renderInput={(params) => <TextField {...params} label={props.selectTitle} />}
          onChange={(event, newValue) => {props.setSeller(newValue)}}
          size='small'
        />
        <Button type='submit'>{props.buttonText}</Button>
      </form>
    </Grid>
  );
}

export default EditSeller;