import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";

import Bar from "../../components/navigation/Bar";
import List from "./components/List";
import { search } from '../../api';

// use cards for search
const Search = (props) => {
  const { term } = useParams();

  const [listings, setListings] = useState([]);
  const [users, setUsers] = useState([]);
  const [combined, setCombined] = useState([]);

  useEffect(() => {
    search(term).then(result => {
      setListings(result.data.listings);
      setUsers(result.data.users);
      setCombined([ ...result.data.listings, ...result.data.users ]);
    });
  }, [term]);

  return (
    <div>
      <Bar session={props.session}/>
      <List
        listings={listings}
        users={users}
        combined={combined}
      />
    </div>
  )
}

export default Search;
