import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { useParams, useNavigate } from "react-router-dom";
import {useEffect, useState} from "react";
import {fetchUser, updateUser} from "../../../api";
import Bar from '../../../components/navigation/Bar';
import { Snackbar } from '@mui/material';

const EditProfile = (props) => {
  const { id } = useParams();

  const [data, setData] = useState({});
  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchUser(id).then(result => {
      setData(result.data);
    });
  }, [id]);

  const handleSave = (e) => {
    e.preventDefault();
    updateUser(id, data).then(() => {
      navigate(`/view/user/${id}`, { replace: true });
    }).catch(() => {
      setError(true);
    });
  }

  return (
    <div>
      <Bar session={props.session}/>
      
      <div style={{height: '100%', paddingLeft: '30%', paddingRight: '30%', paddingTop: '5%'}}>
        <form onSubmit={handleSave}>
          <TextField
            autoFocus
            required
            margin="dense"
            id="username"
            label="Username"
            style={{ width: '100%' }}
            InputLabelProps={{ shrink: true }}
            variant="standard"
            value={data.username}
            onChange={e => setData({ ...data, username: e.target.value })}
          />
          {
            (data.type !== "ADMIN") &&
            <div>
              <TextField
                autoFocus
                margin="dense"
                id="firstname"
                label="First Name"
                style={{ width: '100%' }}
                InputLabelProps={{ shrink: true }}
                variant="standard"
                value={data.firstname}
                onChange={e => setData({ ...data, firstname: e.target.value })}
              />
              <TextField
                autoFocus
                margin="dense"
                id="lastname"
                label="Last Name"
                style={{ width: '100%' }}
                InputLabelProps={{ shrink: true }}
                variant="standard"
                defaultValue={data.lastname}
                onChange={e => setData({ ...data, lastname: e.target.value })}
              />
              <TextField
                autoFocus
                margin="dense"
                id="email"
                label="Email"
                style={{ width: '100%' }}
                InputLabelProps={{ shrink: true }}
                variant="standard"
                defaultValue={data.email}
                onChange={e => setData({ ...data, email: e.target.value })}
              />
              <TextField
                autoFocus
                margin="dense"
                id="address"
                label="Address"
                style={{ width: '100%' }}
                InputLabelProps={{ shrink: true }}
                variant="standard"
                defaultValue={data.address}
                onChange={e => setData({ ...data, address: e.target.value })}
              />
              <TextField
                autoFocus
                margin="dense"
                id="phone"
                label="Phone Number"
                style={{ width: '100%' }}
                InputLabelProps={{ shrink: true }}
                variant="standard"
                defaultValue={data.phone}
                onChange={e => setData({ ...data, phone: e.target.value })}
              />
            </div>
          }
          <br/>
          <Button variant='contained' fullWidth type='submit'>Save</Button>
        </form>
      </div>

      <Snackbar
        open={error}
        autoHideDuration={6000}
        onClose={() => setError(false)}
        message="Please refresh page."
      />
    </div>
  )
}

export default EditProfile;