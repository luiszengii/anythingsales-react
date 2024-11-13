import { Box } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchAllUsers } from "../../../api";

const userColumns = [
  { field: 'id', headerName: 'ID', hide: true },
  { field: 'username', headerName: 'Username', flex: 3 },
  { field: 'type', headerName: 'Type', flex: 1 }
];

const AdminHome = (props) => {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchAllUsers().then(result => {
      setUsers(result.data.users);
    });
  }, []);

  const handleUserClick = (params) => {
    navigate(`/view/user/${params.row.id}`);
  }

 return (
  <Box sx={{height: '100%', paddingLeft: '15%', paddingRight: '15%'}}>
      <h3>Users</h3>
      <DataGrid
        autoHeight={true}
        rows={users}
        columns={userColumns}
        onRowClick={handleUserClick}
        density='compact'
        disableColumnSelector
        disableColumnFilter
      />
    </Box>
 );
}

export default AdminHome;