import { Box } from "@mui/system";
import { DataGrid } from "@mui/x-data-grid";

const sellerColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'username', headerName: 'Sellers', flex: 1, headerAlign: 'center' }
];

const Sellers = (props) => {
  return (
    <Box
      display='flex'
    >
      <DataGrid
        autoHeight={true}
        rows={props.sellers}
        columns={sellerColumns}
        onRowClick={props.handleUserClick}
        density='compact'
        disableColumnSelector
        disableColumnFilter
        hideFooter
      />
    </Box>
  );
}

export default Sellers;