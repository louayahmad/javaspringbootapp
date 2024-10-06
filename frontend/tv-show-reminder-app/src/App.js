import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import GetTVShows from "./GetTVShows";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<GetTVShows />} />
      </Routes>
    </Router>
  );
};

export default App;
