import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { useParams, useNavigate } from "react-router-dom";
import {useEffect, useState} from "react";
import {fetchUser, updateUser} from "../../../api";
import Bar from '../../../components/navigation/Bar';
import { Snackbar } from '@mui/material';

const EditPassword = (props) => {
  const { id } = useParams();

  const [data, setData] = useState({});
  const [newPassword, setNewPassword] = useState(null);

  const [errorText, setErrorText] = useState(null);

  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchUser(id).then(result => {
      setData(result.data);
    });
  }, [id]);

  const handleSave = (e) => {
    e.preventDefault();

    if (newPassword !== data.password) {
      setErrorText("Passwords do not match");
      return;
    }
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
            id="new"
            label="New Password"
            style={{ width: '100%' }}
            variant="standard"
            type="password"
            onChange={e => setNewPassword(e.target.value )}
          /><br/>
          <TextField
            autoFocus
            required
            margin="dense"
            id="confirm"
            label="Confirm New Password"
            style={{ width: '100%' }}
            variant="standard"
            type="password"
            helperText={errorText}
            onChange={e => setData({ ...data, password: e.target.value })}
          /><br/><br/>
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

export default EditPassword;