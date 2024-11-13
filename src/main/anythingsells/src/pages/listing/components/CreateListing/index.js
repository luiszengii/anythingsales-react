import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from "@mui/material/Box";
import { MenuItem, Select } from '@mui/material';

const CreateListing= (props) => {
  return (
    <div style={{height: '100%', paddingLeft: '30%', paddingRight: '30%', paddingTop: '5%'}}>
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
            variant="standard"
            id="name"
            label="Name"
            onChange={e => props.setData({ ...props.data, name: e.target.value })}
          /><br/>
          <TextField
            style={{ width: '100%' }}
            required
            multiline
            rows={10}
            variant="standard"
            id="description"
            label="Description"
            onChange={e => props.setData({ ...props.data, description: e.target.value })}
          /><br/><br/>
          <Select
            required
            style={{ width: '100%' }}
            variant="standard"
            id="category"
            label="Category"
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
        <Button fullWidth type="submit" variant="contained" form="form">Create</Button>
      </Box>
    </div>
  );
}

export default CreateListing;