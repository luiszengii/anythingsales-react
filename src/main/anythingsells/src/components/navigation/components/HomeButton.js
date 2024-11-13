import { useNavigate } from "react-router-dom";

import Button from "@mui/material/Button";

const HomeButton = () => {
  // Navigation
  const navigate = useNavigate();

  const handleHomeClick = () => {
    navigate('/');
  }

  return (
    <Button
      variant="contained"
      onClick={handleHomeClick}
      disableElevation
    >
      AnythingSells
    </Button>
  )
}

export default HomeButton;