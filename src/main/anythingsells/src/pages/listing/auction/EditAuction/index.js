import TextField from '@mui/material/TextField';
import { useState, useEffect } from "react";
import { fetchAuction, updateAuction } from "../../../../api";
import Bar from '../../../../components/navigation/Bar';
import { useNavigate, useParams } from "react-router-dom";
import { DateTimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import EditListing from '../../components/EditListing';
import { Chip, Snackbar, styled } from '@mui/material';

const Div = styled('div')(({ theme }) => ({
  ...theme.typography.button,
  backgroundColor: theme.palette.background.paper,
  padding: theme.spacing(1),
}));

const EditAuction = (props) => {
  const { id } = useParams();

  const [data, setData] = useState({});
  const [auction, setAuction] = useState({});
  const [start, setStart] = useState(dayjs());
  const [end, setEnd] = useState(dayjs());
  const [auctionStart, setAuctionStart] = useState(dayjs());
  const navigate = useNavigate();

  const [error, setError] = useState(false);

  useEffect(() => {
    fetchAuction(id).then(result => {
      setData(result.data);
      setAuction(result.data);
      setAuctionStart(dayjs(`${result.data.start}00`, 'YYYY-MM-DD HH:mm:ss.SSS'));
      setStart(dayjs(`${result.data.start}00`, 'YYYY-MM-DD HH:mm:ss.SSS'));
      setEnd(dayjs(`${result.data.end}00`, 'YYYY-MM-DD HH:mm:ss.SSS'));
    });
  }, [id]);

  const handleSave = (e) => {
    e.preventDefault();
    updateAuction(id, { ...data, start: start.format('YYYY-MM-DD HH:mm:ss.SSS[000]'), end: end.format('YYYY-MM-DD HH:mm:ss.SSS[000]') }).then(() => {
      navigate(`/view/auction/${id}`, { replace: true });
    }).catch(() => {
      setError(true);
    });
  }

  const listing = (
    <div>
      <Div>{auction.name}</Div>
      <Chip label={auction.category} variant="outlined" /><br/>
      <Div>{`Start Time: ${auction.start}`}</Div>
      <Div>{`End Time: ${auction.end}`}</Div>
      <Div>{`Initial Bid: ${auction.initial}`}</Div>
      <Div>{`Current Bid: ${auction.current}`}</Div>
    </div>
  );

  const listingFields = (
    <div>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateTimePicker
          renderInput={(props) => <TextField {...props} style={{ width: '100%' }} />}
          id="start"
          label="Start Time"
          value={start}
          minDateTime={auctionStart}
          onChange={e => setStart(e)}
        />
      </LocalizationProvider>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateTimePicker
          renderInput={(props) => <TextField {...props} style={{ width: '100%' }} />}
          id="end"
          label="End Time"
          value={end}
          minDateTime={start}
          onChange={e => setEnd(e)}
        />
      </LocalizationProvider><br/><br/>
      <TextField
        required
        style={{ width: '100%' }}
        InputLabelProps={{ shrink: true }}
        InputProps={{ inputProps: { step: "0.01" }}}
        inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
        variant="standard"
        id="initial"
        label="Initial Bid (format: 2 decimal places)"
        value={data.initial}
        onChange={e => setData({ ...data, initial: e.target.value })}
      />
    </div>
  );

  return (
    <div>
      <Bar session={props.session}/>

      <EditListing
        handleSave={handleSave}
        data={data}
        setData={setData}
        listingFields={listingFields}
        listing={listing}
      />

      <Snackbar
        open={error}
        autoHideDuration={6000}
        onClose={() => setError(false)}
        message="Please refresh page."
      />
    </div>
  );
}

export default EditAuction;