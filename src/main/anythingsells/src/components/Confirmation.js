import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from "@mui/material";

const Confirmation = (props) => {
  return (
    <Dialog
      open={props.open}
      onClose={props.onClose}
    >
      <DialogTitle>{props.title}</DialogTitle>
      <DialogContent>{props.content}</DialogContent>
      <DialogActions style={{ justifyContent: 'center' }}>
        <Button onClick={props.onClick} variant={props.variant} color={props.color}>{props.buttonText}</Button>
      </DialogActions>
    </Dialog>
  );
}

export default Confirmation;