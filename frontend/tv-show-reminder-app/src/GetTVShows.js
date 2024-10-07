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
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">TV Shows</h2>
      {error && <p className="text-red-600">{error}</p>}
      {loading ? (
        <div className="flex justify-center">
          <div className="spinner">
            <div className="loading-circle"></div>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {tvShows.map((tvShow, index) => (
            <div
              key={`${tvShow.id}_${index}`}
              className="card bg-base-100 shadow-xl"
            >
              <figure>
                {tvShow.image ? (
                  <img
                    src={tvShow.image}
                    alt={tvShow.showName}
                    className="rounded-t-lg"
                  />
                ) : (
                  <div className="h-48 bg-gray-200 rounded-t-lg flex items-center justify-center">
                    <span>No Image Available</span>
                  </div>
                )}
              </figure>
              <div className="card-body">
                <h3 className="card-title">{tvShow.showName}</h3>
                <p>
                  <strong>Language:</strong> {tvShow.language}
                </p>
                <p>
                  <strong>Premiered:</strong> {tvShow.premiered}
                </p>
                <p>
                  <strong>Status:</strong> {tvShow.status}
                </p>
                <p>
                  <strong>Network:</strong> {tvShow.network}
                </p>
                <div className="card-actions justify-end">
                  <button className="btn btn-primary">View Details</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default GetTVShows;
