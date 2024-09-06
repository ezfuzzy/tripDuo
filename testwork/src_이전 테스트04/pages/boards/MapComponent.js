import React, { useEffect, useState, useRef } from "react";

const MapComponent = ({ onSave, onLoad }) => {
  const mapRef = useRef(null); // 지도 DOM 요소를 참조하기 위한 ref
  const [map, setMap] = useState(null); // Kakao 지도 객체를 관리
  const [selectedPlace, setSelectedPlace] = useState(null); // 선택된 장소를 관리
  const [keyword, setKeyword] = useState(""); // 검색 키워드를 관리
  const [places, setPlaces] = useState([]); // 검색된 장소들을 관리
  const [savedPlaces, setSavedPlaces] = useState([]); // 저장된 장소들을 관리
  const [selectedSavedPlace, setSelectedSavedPlace] = useState(null); // 선택된 저장된 장소를 관리
  const [showOnlySavedPlaces, setShowOnlySavedPlaces] = useState(false); // 저장된 장소만 표시할지 여부를 관리
  const [markers, setMarkers] = useState([]); // 지도에 표시된 마커들을 관리
  const [infoWindows, setInfoWindows] = useState([]); // 지도에 표시된 정보 창들을 관리

  // 컴포넌트가 처음 렌더링될 때 실행되는 useEffect
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

      // Add click event listener to the map
      window.kakao.maps.event.addListener(map, "click", () => {
        window.closeInfoWindow();// infoWindow 전부 닫기
        setSelectedPlace(null); // 선택된 장소 초기화
        setSelectedSavedPlace(null); // 선택된 저장된 장소 초기화
      });
    };

    // Kakao Maps API가 로드되지 않은 경우 스크립트 추가
    if (!window.kakao || !window.kakao.maps) {
      const script = document.createElement("script");
      script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=de8bb4ac88880a204a617a3e3f74d387&libraries=services`;
      script.onload = initializeMap;
      document.head.appendChild(script);
    } else {
      initializeMap();
    }

    // 로컬 저장소에서 저장된 장소 불러오기
    const loadedPlaces = JSON.parse(localStorage.getItem("savedPlaces") || "[]");

    // 불러온 장소들에 LatLng 객체 추가
    const placesWithLatLng = loadedPlaces.map((place) => ({
      ...place,
      position: new window.kakao.maps.LatLng(place.position.Ma, place.position.La), // 객체로 생성해야만 함
    }));

    setSavedPlaces(placesWithLatLng);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // 여기에 의존성 추가 금지

  // 저장된 장소가 변경될 때마다 로컬 저장소에 저장
  useEffect(() => {
    localStorage.setItem("savedPlaces", JSON.stringify(savedPlaces));
  }, [savedPlaces]);

  // 지도 객체가 변경될때와 저장된 장소변경시 마커갱신 + 정보창 없애기
  useEffect(() => {
    if (map && savedPlaces.length > 0) {
      clearMarkers();
      clearInfoWindows();

      const newMarkers = [];
      const newInfoWindows = [];

      savedPlaces.forEach((place) => {
        const marker = new window.kakao.maps.Marker({ // marker 생성
          position: place.position,
          map: map,
        });

        const infoWindow = new window.kakao.maps.InfoWindow({ // infoWindow 생성
          content: createInfoWindowContent(place),
        });

        window.kakao.maps.event.addListener(marker, "click", () => {
          // 마커 클릭 이벤트
          window.closeInfoWindow();
          setSelectedSavedPlace(place);
          infoWindow.open(map, marker);
        });

        newMarkers.push(marker);
        newInfoWindows.push(infoWindow);
      });

      setMarkers(newMarkers);
      setInfoWindows(newInfoWindows);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map, savedPlaces]);

  const clearMarkers = () => {
    markers.forEach((marker) => marker.setMap(null));
    setMarkers([]);
  };

  const clearInfoWindows = () => {
    infoWindows.forEach((infoWindow) => infoWindow.close());
    setInfoWindows([]);
  };

  // const closeAllInfoWindows = () => {
  //   window.closeInfoWindow();
  // };

  const createInfoWindowContent = (place) => {
    // infoWindow 코드 window.을 써야만 하는 것 같음 / 잘 모름
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
    // infoWindow 닫기 (실제 동작)
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
    if (ePlace) {
      setSelectedPlace(ePlace);
    }

    if (selectedPlace) {
      const isAlreadySaved = savedPlaces.some((place) => place.id === selectedPlace.id);
      if (isAlreadySaved) {
        alert("이미 저장된 장소입니다.");
      } else {
        setSavedPlaces([...savedPlaces, selectedPlace]);
        onSave(selectedPlace);
        setSelectedPlace(null);
      }
    }
  };

  const handlePlaceClick = (place) => {
    setSelectedSavedPlace(place);

    map.setCenter(place.position); // 지도 중심을 place.location 좌표로 
    map.setLevel(5); // 지도의 zoom level 올라갈수록 작아지고  작아질수록 커짐
    const marker = markers.find( // 소수점 10자리까지 비교 - 6~7자리만 해도 1.1cm의 정확도 
      (marker) =>
        marker.getPosition().getLat().toFixed(10) === place.position.getLat().toFixed(10) &&
        marker.getPosition().getLng().toFixed(10) === place.position.getLng().toFixed(10)
    );

    const infoWindow = infoWindows[markers.indexOf(marker)]; // 마커에 해당하는 infoWindow 찾아서

    if (infoWindow) { // infoWindow 랜더링
      window.closeInfoWindow();
      infoWindow.open(map, marker);
    }
  };

  const handleShowSavedPlaces = () => {
    setShowOnlySavedPlaces(!showOnlySavedPlaces); // 이거 코드 약간 수정해야되는데 어차피 이 부분은 안들어갈거니까 괜춘

    if (!showOnlySavedPlaces) {
      // 마커 클리어 
      markers.forEach((marker) => marker.setMap(null));

      // 저장된 장소에 마커 세팅 
      const savedMarkers = savedPlaces.map((place) => {
        return new window.kakao.maps.Marker({
          position: place.position,
          map: map,
        });
      });
      setMarkers(savedMarkers);
  
    } else {
      // Revert to showing all places
      handleSearch();
    }
  };

  // 검색폼 편의성 
  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  // 저장한 장소목록에서 장소 삭제 
  const handleDeleteSavedPlace = () => {
    if (selectedSavedPlace) {
      const newSavedPlaces = savedPlaces.filter((place) => place.id !== selectedSavedPlace.id);
      setSavedPlaces(newSavedPlaces);
      setSelectedSavedPlace(null);
      alert("장소가 삭제되었습니다.");
    }
  };

  const urlPattern = new RegExp(/^(https?:\/\/)?([a-z\d-]+\.)+[a-z]{2,6}(:\d{1,5})?(\/.*)?$/i);

  return (
    <div className="flex h-screen bg-gray-100">
      <div ref={mapRef} className="w-1/2 h-full"></div>

      <div className="w-1/6 p-4 bg-white shadow-md">
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={handleKeyPress}
          placeholder="장소를 검색하세요"
          className="w-full p-2 mb-2 border rounded"
        />
        <button onClick={handleSearch} className="w-full p-2 text-white bg-blue-500 rounded hover:bg-blue-600">
          검색
        </button>

        <ul className="mt-4 space-y-2">
          {places.map((place, index) => (
            <li
              key={index}
              onClick={() => {
                const selectedPosition = new window.kakao.maps.LatLng(place.y, place.x); // 객체 생성이 포인트
                // 위도, 경도 순서 

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
                // setSelectedSavedPlace 하는 이유는 어차피 useState라 일시적으로 사용할 거고
                // 화면 오른쪽에 정보 띄어주려면 이거 써야함 안그러면 상태관리 약간 복잡해져서 이렇게 구현 
                setSelectedSavedPlace(placeData);
                handlePlaceClick(placeData);
              }}
              className={`p-2 cursor-pointer hover:bg-gray-100 rounded ${
                selectedPlace && selectedPlace.id === place.id ? "bg-blue-100 border border-blue-500" : ""
              }`}>
              {place.place_name}
            </li>
          ))}
        </ul>
      </div>

      <div className="w-1/6 p-4 bg-white shadow-md">
        <h2 className="mb-2 text-xl font-bold">저장된 장소 목록</h2>
        <button
          onClick={handleShowSavedPlaces}
          className="w-full p-2 my-4 text-white bg-purple-500 rounded hover:bg-purple-600">
          {showOnlySavedPlaces ? "모든 장소 보기" : "저장한 장소만 보기"}
        </button>
        <ul className="space-y-2">
          {savedPlaces.map((place, index) => (
            <li
              key={index}
              onClick={() => handlePlaceClick(place)}
              className={`p-2 cursor-pointer hover:bg-gray-100 rounded ${
                selectedSavedPlace && selectedSavedPlace.id === place.id ? "bg-blue-100 border border-blue-500" : ""
              }`}>
              {place.place_name}
            </li>
          ))}
        </ul>
      </div>

      {selectedSavedPlace && (
        <div className="w-1/6 p-4 bg-white shadow-md">
          <h3 className="mb-4 text-lg font-semibold">{selectedSavedPlace.name} 정보</h3>
          <ul className="space-y-2">
            {Object.entries(selectedSavedPlace).map(([key, value]) => {
              const valueString = value.toString();
              const isUrl = urlPattern.test(valueString);

              return (
                <li key={key} className="p-2 bg-gray-50 rounded">
                  <div>
                    <strong className="font-medium">{key}:</strong>
                  </div>
                  {isUrl ? (
                    <a href={valueString} target="_blank" rel="noopener noreferrer" style={{ color: "blue" }}>
                      {valueString}
                    </a>
                  ) : (
                    valueString
                  )}
                </li>
              );
            })}
          </ul>
          <button
            onClick={handleDeleteSavedPlace}
            className="w-full p-2 mt-4 text-white bg-red-500 rounded hover:bg-red-600">
            삭제
          </button>
        </div>
      )}
    </div>
  );
};

export default MapComponent;
