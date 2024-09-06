import React, { useEffect, useState, useRef } from "react";

const CourseMapComponent = ({ onSave, onLoad, selectedDayIndex, selectedPlaceIndex }) => {
  const mapRef = useRef(null);
  const [map, setMap] = useState(null);
  const [selectedPlace, setSelectedPlace] = useState(null);
  const [keyword, setKeyword] = useState("");
  const [places, setPlaces] = useState([]);
  const [savedPlaces, setSavedPlaces] = useState([]);
  const [markers, setMarkers] = useState([]);
  const [infoWindows, setInfoWindows] = useState([]);

  useEffect(() => {
    const initializeMap = () => {
      if (!window.kakao || !window.kakao.maps) {
        console.error("Kakao Maps API is not loaded.");
        return;
      }

      const map = new window.kakao.maps.Map(mapRef.current, {
        center: new window.kakao.maps.LatLng(37.5665, 126.978),
        level: 3,
      });
      setMap(map);
      onLoad(map);

      window.kakao.maps.event.addListener(map, "click", () => {
        window.closeInfoWindow();
        setSelectedPlace(null);
      });
    };

    if (!window.kakao || !window.kakao.maps) {
      const script = document.createElement("script");
      script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=de8bb4ac88880a204a617a3e3f74d387&libraries=services`;
      script.onload = initializeMap;
      document.head.appendChild(script);
    } else {
      initializeMap();
    }

    const loadedPlaces = JSON.parse(localStorage.getItem("savedPlaces") || "[]");

    const placesWithLatLng = loadedPlaces.map((place) => ({
      ...place,
      position: new window.kakao.maps.LatLng(place.position.Ma, place.position.La),
    }));

    setSavedPlaces(placesWithLatLng);
  }, []);

  useEffect(() => {
    localStorage.setItem("savedPlaces", JSON.stringify(savedPlaces));
  }, [savedPlaces]);

  useEffect(() => {
    if (map && savedPlaces.length > 0) {
      clearMarkers();
      clearInfoWindows();

      const newMarkers = [];
      const newInfoWindows = [];

      savedPlaces.forEach((place) => {
        const marker = new window.kakao.maps.Marker({
          position: place.position,
          map: map,
        });

        const infoWindow = new window.kakao.maps.InfoWindow({
          content: createInfoWindowContent(place),
        });

        window.kakao.maps.event.addListener(marker, "click", () => {
          window.closeInfoWindow();
          infoWindow.open(map, marker);
        });

        newMarkers.push(marker);
        newInfoWindows.push(infoWindow);
      });

      setMarkers(newMarkers);
      setInfoWindows(newInfoWindows);
    }
  }, [map, savedPlaces]);

  const clearMarkers = () => {
    markers.forEach((marker) => marker.setMap(null));
    setMarkers([]);
  };

  const clearInfoWindows = () => {
    infoWindows.forEach((infoWindow) => infoWindow.close());
    setInfoWindows([]);
  };

  const createInfoWindowContent = (place) => {
    const isSaved = savedPlaces.some((savedPlace) => savedPlace.id === place.id);
    const buttonLabel = isSaved ? "삭제" : "저장";
    const buttonOnClick = isSaved ? `window.removePlace('${place.id}')` : `window.savePlace('${place}')`;

    return `
    <div style="padding:10px;font-size:12px;display:flex;flex-direction:column;align-items:flex-start;width:150px;">
      <div style="margin-bottom: 8px; display: flex; justify-content: space-between; width: 100%;">
        <strong>${place.place_name}</strong>
      </div>
      <div style="margin-bottom: 8px;">${place.address_name}</div>
      <button onclick="${buttonOnClick}" style="width:100%;background-color:${
      isSaved ? "red" : "green"
    };color:white;padding:5px;border:none;border-radius:5px;">
        ${buttonLabel}
      </button>
    </div>
  `;
  };

  window.closeInfoWindow = () => {
    infoWindows.forEach((infoWindow, idx) => {
      infoWindow.close();
    });
  };
  window.savePlace = (ePlace) => {
    handleSave(ePlace);
  };

  window.removePlace = (id) => {
    const newSavedPlaces = savedPlaces.filter((place) => place.id !== id);
    setSavedPlaces(newSavedPlaces);
  };

  const handleSearch = () => {
    if (map && keyword) {
      const ps = new window.kakao.maps.services.Places();
      ps.keywordSearch(keyword, (data, status) => {
        if (status === window.kakao.maps.services.Status.OK) {
          setPlaces(data);
          map.setCenter(new window.kakao.maps.LatLng(data[0].y, data[0].x));
          map.setLevel(3);

          clearMarkers();
          clearInfoWindows();

          const newMarkers = [];
          const newInfoWindows = [];

          data.forEach((place) => {
            const marker = new window.kakao.maps.Marker({
              position: new window.kakao.maps.LatLng(place.y, place.x),
              map: map,
            });

            const infoWindow = new window.kakao.maps.InfoWindow({
              content: createInfoWindowContent(place),
            });

            window.kakao.maps.event.addListener(marker, "click", () => {
              window.closeInfoWindow();
              setSelectedPlace({
                address_name: place.address_name,
                category_group_code: place.category_group_code,
                category_group_name: place.category_group_name,
                category_name: place.category_name,
                id: place.id,
                phone: place.phone,
                place_name: place.place_name,
                place_url: place.place_url,
                road_address_name: place.road_address_name,
                position: new window.kakao.maps.LatLng(place.y, place.x),
              });
              infoWindow.open(map, marker);
            });

            newMarkers.push(marker);
            newInfoWindows.push(infoWindow);
          });

          setMarkers(newMarkers);
          setInfoWindows(newInfoWindows);
        }
      });
    }
  };

  const handleSave = (ePlace) => {
    console.log("handleSave");

    if (ePlace) {
      setSelectedPlace(ePlace);
    }

    if (selectedPlace) {
      const isAlreadySaved = savedPlaces.some((place) => place.id === selectedPlace.id);
      if (isAlreadySaved) {
        alert("이미 저장된 장소입니다.");
      } else {
        const newPlace = {
          ...selectedPlace,
          dayIndex: selectedDayIndex,
          placeIndex: selectedPlaceIndex,
        };
        onSave(newPlace); // CourseForm에 장소 데이터 전달
        setSelectedPlace(null);
      }
    }
  };

  const handlePlaceClick = (place) => {
    map.setCenter(place.position);
    map.setLevel(5);
    const marker = markers.find(
      (marker) =>
        marker.getPosition().getLat().toFixed(10) === place.position.getLat().toFixed(10) &&
        marker.getPosition().getLng().toFixed(10) === place.position.getLng().toFixed(10)
    );

    const infoWindow = infoWindows[markers.indexOf(marker)];

    if (infoWindow) {
      window.closeInfoWindow();
      infoWindow.open(map, marker);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  return (
    <div className="flex flex-col w-1/2 h-full p-4">
      <div ref={mapRef} className="flex-grow mb-4" style={{ minHeight: "300px" }}></div>

      <div>
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={handleKeyPress}
          placeholder="장소를 검색하세요"
        />
        <button onClick={handleSearch}>검색</button>

        <ul>
          {places.map((place, index) => (
            <li
              className="border"
              key={index}
              onClick={() => {
                const selectedPosition = new window.kakao.maps.LatLng(place.y, place.x);

                const placeData = {
                  address_name: place.address_name,
                  category_group_code: place.category_group_code,
                  category_group_name: place.category_group_name,
                  category_name: place.category_name,
                  id: place.id,
                  phone: place.phone,
                  place_name: place.place_name,
                  place_url: place.place_url,
                  road_address_name: place.road_address_name,
                  position: selectedPosition,
                };

                map.setCenter(selectedPosition);
                map.setLevel(3);

                setSelectedPlace(placeData);

                handlePlaceClick(placeData);
              }}>
              {place.place_name}
            </li>
          ))}
        </ul>
      </div>

      <div>
        <h2>저장된 장소 목록</h2>

        <ul>
          {savedPlaces.map((place, index) => (
            <li key={index} onClick={() => handlePlaceClick(place)}>
              {place.place_name}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default CourseMapComponent;
