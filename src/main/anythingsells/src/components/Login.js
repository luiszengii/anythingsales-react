import { useState } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import { login, register } from '../api';

export default function Login(props) {
  const [isLogin, setIsLogin] = useState(props.isLogin);
  const [loginData, setLoginData] = useState({});
  const [registerData, setRegisterData] = useState({});
  const [errorText, setErrorText] = useState(null);

  const handleLoginSelect = () => {
    clear();
    setIsLogin(true);
  };

  const handleRegisterSelect = () => {
    clear();
    setIsLogin(false);
  };

  const handleLogin = (e) => {
    e.preventDefault();
    login(loginData)
    .then(() => {
      window.location.reload();
    })
    .catch(() => {
      setErrorText("Incorrect username/password");
    });
  };

  const handleRegister = (e) => {
    e.preventDefault();
    if (!errorText) {
      register(registerData)
      .then(() => {
        handleLoginSelect();
      })
      .catch(err => {
        console.log(err.message);
        setErrorText("Username taken");
      });
    }
  };

  const handleClose = () => {
    clear();
    setIsLogin(props.isLogin);
    props.setOpen(false);
  }

  const clear = () => {
    setLoginData({});
    setRegisterData({});
    setErrorText(null);
  }

  const checkPassword = (text) => {
    if (registerData.password !== text) {
      setErrorText("Passwords don't match");
    } else {
      setErrorText(null);
    }
  }

  return (
    <div>
      {isLogin &&
        <Dialog 
        open={props.open}
        onClose={handleClose}
        >
          <DialogTitle>Login</DialogTitle>
          <DialogContent>
            <form onSubmit={handleLogin} id="loginform">
              <TextField
                required
                margin="dense"
                id="username"
                label="Username"
                fullWidth
                variant="standard"
                onInput={e => setLoginData({ ...loginData, username: e.target.value })}
              />
              <TextField
                required
                margin="dense"
                id="password"
                label="Password"
                fullWidth
                variant="standard"
                type="password"
                helperText={errorText}
                onInput={e => setLoginData({ ...loginData, password: e.target.value })}
              />
            </form>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleRegisterSelect}>Register</Button>
            <Button type="submit" variant="contained" form="loginform">Login</Button>
          </DialogActions>
        </Dialog>
      }
      {!isLogin &&
        <Dialog 
        open={props.open}
        onClose={handleClose}
        >
          <DialogTitle>Register</DialogTitle>
          <DialogContent>
            <form onSubmit={handleRegister} id="registerform">
              <TextField
                required
                margin="dense"
                id="username"
                label="Username"
                fullWidth
                variant="standard"
                onInput={e => setRegisterData({ ...registerData, username: e.target.value })}
              />
              <TextField
                required
                margin="dense"
                id="password"
                label="Password"
                fullWidth
                variant="standard"
                type="password"
                onInput={e => setRegisterData({ ...registerData, password: e.target.value })}
              />
              <TextField
                required
                margin="dense"
                id="confirm"
                label="Confirm Password"
                fullWidth
                variant="standard"
                type="password"
                helperText={errorText}
                onInput={e => checkPassword(e.target.value)}
              />
              <FormControl>
                <FormLabel id="role"/>
                <RadioGroup
                  row
                  aria-labelledby="role"
                  name="role-group"
                  onChange={e => setRegisterData({ ...registerData, type: e.target.value })}
                >
                  <FormControlLabel value="CUSTOMER" control={<Radio required/>} label="Customer"/>
                  <FormControlLabel value="SELLER" control={<Radio required/>} label="Seller" />
                </RadioGroup>
              </FormControl>
            </form>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleLoginSelect}>Login</Button>
            <Button type="submit" variant="contained" form="registerform">Register</Button>
          </DialogActions>
        </Dialog>
      }
    </div>
  );
}
