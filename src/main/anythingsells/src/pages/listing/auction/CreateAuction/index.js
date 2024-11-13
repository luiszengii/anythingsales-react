import TextField from '@mui/material/TextField';
import { useState } from "react";
import { createAuction } from "../../../../api";
import Bar from '../../../../components/navigation/Bar';
import { useNavigate } from "react-router-dom";
import { DateTimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import CreateListing from '../../components/CreateListing';

const CreateAuction = (props) => {
  const [data, setData] = useState(null);
  const [start, setStart] = useState(dayjs());
  const [end, setEnd] = useState(dayjs());
  const navigate = useNavigate();

  const handleSave = (e) => {
    e.preventDefault();
    createAuction({ ...data, start: start.format('YYYY-MM-DD HH:mm:ss.SSS[000]'), end: end.format('YYYY-MM-DD HH:mm:ss.SSS[000]') }).then(() => {
      navigate('/listings', { replace: true });
    });
  }

  const listingFields = (
    <div>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateTimePicker
          renderInput={(props) => <TextField {...props} style={{ width: '100%' }} />}
          required
          id="start"
          label="Start Time"
          value={start}
          minDateTime={dayjs()}
          onChange={e => setStart(e)}
        />
      </LocalizationProvider>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateTimePicker
          renderInput={(props) => <TextField {...props} style={{ width: '100%' }} />}
          required
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
        InputProps={{ inputProps: { step: "0.01" }}}
        inputProps={{ inputMode: 'decimal', pattern: '^[0-9]*.[0-9]{2}$' }}
        variant="standard"
        id="initial"
        label="Initial Bid (format: 2 decimal places)"
        onChange={e => setData({ ...data, initial: e.target.value })}
      />
    </div>
  );

  return (
    <div>
      <Bar session={props.session}/>
      <CreateListing
        handleSave={handleSave}
        data={data}
        setData={setData}
        listingFields={listingFields}
      />
    </div>
  );
}

export default CreateAuction;