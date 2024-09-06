import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";

const CourseDetail = () => {
  const { id } = useParams(); // Assuming you pass the ID of the course as a URL parameter
  const [course, setCourse] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch the course data based on the ID
    axios.get(`/api/v1/posts/course/${id}`)
      .then((response) => {
        setCourse(response.data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err);
        setLoading(false);
      });
  }, [id]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error loading course details</div>;
  if (!course) return <div>No course found</div>;

  return (
    <div className="container p-6">
      <h1 className="text-3xl font-bold mb-4">{course.title}</h1>
      <div className="mb-4">
        <h2 className="text-xl font-semibold">여행할 나라:</h2>
        <p>{course.country}</p>
      </div>
      <div className="mb-4">
        <h2 className="text-xl font-semibold">여행할 도시:</h2>
        <p>{course.city}</p>
      </div>
      <div className="mb-4">
        <h2 className="text-xl font-semibold">태그:</h2>
        <p>{course.tags}</p>
      </div>
      <div className="mb-4">
        <h2 className="text-xl font-semibold">일정:</h2>
        {course.postData.map((day, dayIndex) => (
          <div key={dayIndex} className="border p-4 rounded-lg bg-white shadow mb-4">
            <h3 className="text-lg font-semibold">Day {dayIndex + 1}</h3>
            {day.dayMemo && (
              <div className="mb-2">
                <h4 className="font-semibold">Day Memo:</h4>
                <p>{day.dayMemo}</p>
              </div>
            )}
            {day.places.map((place, placeIndex) => (
              <div key={placeIndex} className="mb-2">
                <h4 className="font-semibold">Place {placeIndex + 1}</h4>
                <p><strong>Name:</strong> {place.place_name}</p>
                <p><strong>Memo:</strong> {place.placeMemo}</p>
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default CourseDetail;
