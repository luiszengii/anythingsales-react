import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from "@mui/material/Box";
import { Grid, MenuItem, Select } from '@mui/material';

const EditListing = (props) => {
  return (
    <Grid container style={{height: '100%', paddingLeft: '30%', paddingRight: '30%', paddingTop: '5%'}} spacing={2}>
      <Grid item xs={6}>
        {props.listing}
      </Grid>
      <Grid item xs={6}>
        <Box
          sx={{
            '& .MuiTextField-root': { m: 1, width: '25ch' }
          }}
          noValidate
          autoComplete="off"
        >
          <form onSubmit={props.handleSave} id="form">
            <TextField
              required
              style={{ width: '100%' }}
              InputLabelProps={{ shrink: true }}
              variant="standard"
              id="name"
              label="Name"
              value={props.data.name}
              onChange={e => props.setData({ ...props.data, name: e.target.value })}
            /><br/>
            <TextField
              style={{ width: '100%' }}
              multiline
              InputLabelProps={{ shrink: true }}
              rows={10}
              variant="standard"
              id="description"
              label="Description"
              value={props.data.description}
              onChange={e => props.setData({ ...props.data, description: e.target.value })}
            /><br/><br/>
            <Select
              required
              style={{ width: '100%' }}
              variant="standard"
              id="category"
              label="Category"
              value={String(props.data.category)}
              onChange={e => props.setData({ ...props.data, category: e.target.value })}
            >
              <MenuItem value={"APP_GAMES"}>APP_GAMES</MenuItem>
              <MenuItem value={"HOME"}>HOME</MenuItem>
              <MenuItem value={"ELECTRONIC"}>ELECTRONIC</MenuItem>
              <MenuItem value={"KITCHEN_DINING"}>KITCHEN_DINING</MenuItem>
              <MenuItem value={"MOVIES_TV"}>MOVIES_TV</MenuItem>
              <MenuItem value={"MUSIC"}>MUSIC</MenuItem>
              <MenuItem value={"PET_SUPPLIES"}>PET_SUPPLIES</MenuItem>
            </Select><br/><br/>
            
            {props.listingFields}<br/>

          </form>
          <Button type="submit" fullWidth variant="contained" form="form">Update</Button>
        </Box>
      </Grid>
    </Grid>
  );
}

export default EditListing;