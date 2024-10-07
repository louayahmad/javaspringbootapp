import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import GetTVShows from "./Components/GetTVShows";
import Navbar from "./Components/Navbar";

const App = () => {
  return (
    <Router>
      <Navbar /> {}
      <Routes>
        <Route path="/" element={<GetTVShows />} />
      </Routes>
    </Router>
  );
};

export default App;
