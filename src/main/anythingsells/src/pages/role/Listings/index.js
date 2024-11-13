import Bar from "../../../components/navigation/Bar";
import UserListings from "../../../components/profile/UserListings";

const Listings = (props) => {
  return (
    <div>
      <Bar session={props.session}/>
      <UserListings id={props.session.id}/>
    </div>
  )
}

export default Listings;
