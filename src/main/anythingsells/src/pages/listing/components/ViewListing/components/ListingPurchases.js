import { Box } from "@mui/system";
import { DataGrid } from "@mui/x-data-grid";

const ListingPurchases = (props) => {
  return (
    <Box sx={{ textAlign: 'center' }}>
      <DataGrid
        autoHeight={true}
        rows={props.purchases}
        columns={props.columns}
        onRowClick={props.handlePurchaseClick}
        disableColumnSelector
      />
    </Box>
  );
}

export default ListingPurchases;