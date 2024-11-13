import React from "react";

import Bar from "../../components/navigation/Bar";
import AdminHome from "./components/AdminHome";
import CustomerHome from "./components/CustomerHome";
import DefaultHome from "./components/DefaultHome";
import SellerHome from "./components/SellerHome";

const Home = (props) => {
  return (
    <div>
      <Bar session={props.session}/>

      {(props.session.type !== "CUSTOMER" && props.session.type !== "SELLER" && props.session.type !== "ADMIN") &&
        <DefaultHome session={props.session}/>
      }
      {(props.session.type === "CUSTOMER") &&
        <CustomerHome session={props.session}/>
      }
      {(props.session.type === "SELLER") &&
        <SellerHome session={props.session}/>
      }
      {(props.session.type === "ADMIN") &&
        <AdminHome session={props.session}/>
      }
    </div>
  )
}

export default Home;
