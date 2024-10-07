import React, { useEffect, useState } from "react";
import "./Navbar.css";

const Navbar = () => {
  const [tvShowName, setTvShowName] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const [selectedShow, setSelectedShow] = useState(null);

  const searchTvShows = async (name) => {
    if (name.trim() === "") {
      setSearchResults([]);
      setShowDropdown(false);
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/tvshows/search", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ showName: name }),
      });

      if (!response.ok) {
        throw new Error("Failed to fetch search results");
      }

      const data = await response.json();
      setSearchResults(data);
      setShowDropdown(data.length > 0);
    } catch (error) {
      console.error("Error fetching TV shows:", error.message);
    }
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      searchTvShows(tvShowName);
    }, 300);

    return () => clearTimeout(delayDebounceFn);
  }, [tvShowName]);

  const closeSearchResults = () => {
    setShowDropdown(false);
  };

  const handleRowClick = (show) => {
    setSelectedShow(show);
    setShowDropdown(false);
  };

  const closeModal = () => {
    setSelectedShow(null);
  };

  return (
    <nav className="bg-base-100 shadow-md p-4 relative">
      <div className="container mx-auto flex items-center justify-between">
        <div className="flex-grow">
          <input
            type="text"
            placeholder="Search TV shows..."
            className="input input-bordered w-full max-w-xl"
            value={tvShowName}
            onChange={(e) => setTvShowName(e.target.value)}
          />
        </div>
      </div>
      {showDropdown && searchResults.length > 0 && (
        <div className="dropdown bg-gray-800 text-white shadow-md mt-2 rounded w-full max-w-xl overflow-y-auto scrollbar-hide">
          <div className="flex justify-between items-center p-2">
            <span className="font-bold">Results</span>
            <button
              onClick={closeSearchResults}
              className="text-gray-400 hover:text-gray-200"
            >
              X
            </button>
          </div>
          {searchResults.map((show) => (
            <div
              key={show.id}
              className="row flex border-b border-gray-600 hover:bg-gray-700 transition-colors duration-200 cursor-pointer"
              onClick={() => handleRowClick(show)}
            >
              <img
                src={show.image}
                alt={show.showName}
                className="w-16 h-16 object-cover mr-3 rounded"
              />
              <div className="text-lg flex-grow">{show.showName}</div>
            </div>
          ))}
        </div>
      )}

      {}
      {selectedShow && (
        <div className="modal modal-open">
          <div className="modal-overlay" onClick={closeModal}></div>
          <div className="modal-content w-96 mx-auto rounded-lg bg-white shadow-lg relative mt-20">
            <button
              className="absolute top-2 right-2 text-gray-600 hover:text-gray-900"
              onClick={closeModal}
            >
              X
            </button>
            <div className="modal-header p-4">
              <h3 className="text-lg font-bold">{selectedShow.showName}</h3>
            </div>
            <div className="modal-body p-4">
              <img
                src={selectedShow.image}
                alt={selectedShow.showName}
                className="w-full h-auto rounded mb-4"
              />
              <p>Description or additional information can go here.</p>
            </div>
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navbar;
