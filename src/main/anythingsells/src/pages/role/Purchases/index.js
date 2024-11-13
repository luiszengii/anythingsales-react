import Bar from "../../../components/navigation/Bar";
import UserPurchases from "../../../components/profile/UserPurchases";

const Purchases = (props) => {
  return (
    <div>
      <Bar session={props.session}/>
      <UserPurchases id={props.session.id}/>
    </div>
  )
}

export default Purchases;
