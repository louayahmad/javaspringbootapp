import React, { useEffect, useState } from "react";
import "./GetTVShows.css";

const GetTVShows = () => {
  const [tvShows, setTVShows] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState({});
  const [episodes, setEpisodes] = useState({});

  useEffect(() => {
    const getTvShows = async () => {
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
          throw new Error("Failed to fetch data");
        }

        const data = await response.json();
        setTVShows(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    getTvShows();
  }, []);

  const getUpcomingEpisodes = async (tvShowId, tvShowName) => {
    setEpisodes((prev) => ({
      ...prev,
      [tvShowId]: null,
    }));

    try {
      const response = await fetch("http://localhost:8080/episodes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          tvShowId,
          tvShowName,
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to fetch upcoming episodes for the TV show");
      }
      const data = await response.json();
      setEpisodes((upcomingEpisodes) => ({
        ...upcomingEpisodes,
        [tvShowId]: data,
      }));
    } catch (error) {
      console.error(
        "Error fetching upcoming episodes for the show:",
        error.message
      );
    }
  };

  const toggleModal = (tvShowId, tvShowName) => {
    setShowModal((prev) => ({
      ...prev,
      [tvShowId]: !prev[tvShowId],
    }));

    getUpcomingEpisodes(tvShowId, tvShowName);
  };

  const setReminder = (episodeId) => {
    alert(`Reminder set for episode: ${episodeId}`);
  };

  return (
    <div className="px-12 py-8">
      {error && <p className="text-red-600">{error}</p>}
      {loading ? (
        <div className="flex justify-center items-center h-64">
          <div className="loader" />
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {tvShows.map((tvShow, index) => (
            <div key={`${tvShow.id}_${index}`} className="relative group">
              <figure className="h-[440px] w-full">
                {tvShow.image ? (
                  <img
                    src={tvShow.image}
                    alt={tvShow.showName}
                    className="w-full h-full object-cover rounded-lg transition-opacity duration-300"
                  />
                ) : (
                  <div className="h-full bg-gray-200 rounded-lg flex items-center justify-center">
                    <span>No Image Available</span>
                  </div>
                )}
              </figure>
              <div className="absolute inset-0 bg-black bg-opacity-60 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col items-center justify-center p-4">
                <h3 className="text-white text-lg font-bold mb-2">
                  {tvShow.showName}
                </h3>
                <p className="text-white">
                  <strong>Language:</strong> {tvShow.language}
                </p>
                <p className="text-white">
                  <strong>Premiered:</strong> {tvShow.premiered}
                </p>
                <p className="text-white">
                  <strong>Status:</strong> {tvShow.status}
                </p>
                <p className="text-white">
                  <strong>Network:</strong> {tvShow.network}
                </p>
                <div className="card-actions justify-center mt-4">
                  <button
                    className="btn btn-primary"
                    onClick={() => toggleModal(tvShow.id, tvShow.showName)}
                  >
                    Schedule
                  </button>
                </div>

                {}
                {showModal[tvShow.id] && (
                  <div className="absolute inset-0 bg-gray-800 bg-opacity-90 rounded-lg flex flex-col items-center justify-center p-4 z-10">
                    <h3 className="text-white text-xl font-bold mb-4">
                      Upcoming Episodes for {tvShow.showName}
                    </h3>
                    <div className="w-full h-64 overflow-y-auto bg-gray-700 p-4 rounded">
                      {episodes[tvShow.id] === null ? (
                        <div className="flex justify-center items-center h-full">
                          <div className="loader" /> {}
                        </div>
                      ) : episodes[tvShow.id] &&
                        episodes[tvShow.id].length > 0 ? (
                        episodes[tvShow.id].map((episode) => (
                          <div
                            key={episode.episodeId}
                            className="flex justify-between items-center bg-gray-800 text-white p-3 mb-2 rounded"
                          >
                            <div>
                              <p className="font-bold">{episode.episodeName}</p>
                              <p>
                                Air Date:{" "}
                                {new Date(
                                  episode.episodeAirDateTime
                                ).toLocaleString()}
                              </p>
                            </div>
                            <button
                              className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded"
                              onClick={() => setReminder(episode.episodeId)}
                            >
                              Set Reminder
                            </button>
                          </div>
                        ))
                      ) : (
                        <p className="text-white">
                          No upcoming episodes found.
                        </p>
                      )}
                    </div>
                    <button
                      className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 mt-4 rounded"
                      onClick={() => toggleModal(tvShow.id)}
                    >
                      Close
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default GetTVShows;
