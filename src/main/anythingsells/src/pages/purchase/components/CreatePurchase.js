import Button from '@mui/material/Button';

const CreatePurchase = (props) => {
  return (
    <div>
      <form onSubmit={props.handleSave}>
        <div>{props.input}</div><br/>
        <Button type='submit'>{props.saveText}</Button>
      </form>
    </div>
  )
}

export default CreatePurchase;