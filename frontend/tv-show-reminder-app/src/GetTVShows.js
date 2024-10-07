import React, { useEffect, useState } from "react";

const GetTVShows = () => {
  const [tvShows, setTVShows] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getTvShows = async () => {
      console.log("Fetching TV shows...");
      setError(null);
      setLoading(true);

      try {
        const response = await fetch("http://localhost:8080/tvshows", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          console.error(
            "Failed to fetch data. Response status:",
            response.status
          );
          throw new Error("Failed to fetch data");
        }

        const data = await response.json();
        console.log("TV shows data fetched successfully:", data);
        setTVShows(data);
      } catch (error) {
        console.error("Error fetching TV shows:", error.message);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    getTvShows();
  }, []);

  return (
    <div>
      <h2>TV Shows</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {loading ? (
        <div className="spinner">
          <div className="loading-circle"></div>
        </div>
      ) : (
        <ul>
          {tvShows.map((tvShow, index) => (
            <li key={`${tvShow.id}_${index}`}>
              <strong>Show Name:</strong> {tvShow.showName}
              <br />
              <strong>Language:</strong> {tvShow.language}
              <br />
              <strong>Premiered:</strong> {tvShow.premiered}
              <br />
              <strong>Status:</strong> {tvShow.status}
              <br />
              <strong>Network:</strong> {tvShow.network}
              <br />
              <br />
              <strong>Image:</strong>
              <br />
              {tvShow.image && (
                <img
                  src={tvShow.image}
                  alt={tvShow.showName}
                  style={{ width: "200px", height: "auto" }}
                />
              )}
              <br />
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default GetTVShows;
