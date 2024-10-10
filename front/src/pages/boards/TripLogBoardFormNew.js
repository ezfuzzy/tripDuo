import axios from "axios"
import React, { useEffect, useState } from "react"
import { useNavigate, useSearchParams } from "react-router-dom"
import CourseKakaoMapComponent from "../../components/CourseKakaoMapComponent"
import { shallowEqual, useSelector } from "react-redux"
import CourseGoogleMapComponent from "../../components/CourseGoogleMapComponent"
import Calendar from "react-calendar"

const TripBoardFormNew = () => {
  const loggedInUserId = useSelector((state) => state.userData.id, shallowEqual)
  const loggedInNickname = useSelector((state) => state.userData.nickname, shallowEqual)
  const loggedInUsername = useSelector((state) => state.userData.username, shallowEqual)

  // 달력에서 선택된 날짜 범위 저장
  const [selectedDateRange, setSelectedDateRange] = useState([null, null])
  const [isCalendarOpen, setIsCalendarOpen] = useState(false) // 캘린더 표시 여부 상태

  const [title, setTitle] = useState("")
  const [country, setCountry] = useState("")
  const [city, setCity] = useState("")
  const [tagInput, setTagInput] = useState("")
  const [tags, setTags] = useState([])
  const [days, setDays] = useState([{ places: [""], dayMemo: "" }])
  
  //나라별 도시 목록
  const citiesByCountry = {
    대한민국: ["서울", "부산", "제주", "인천"],
    일본: ["도쿄", "오사카", "교토", "삿포로"],
    중국: ["베이징", "상하이", "광저우", "시안"],
    인도: ["델리", "뭄바이", "콜카타", "벵갈루루"],
    스페인: ["바르셀로나", "그라나다", "마드리드", "세비야"],
    영국: ["런던", "맨체스터", "버밍엄", "리버풀"],
    독일: ["베를린", "뮌헨", "프랑크푸르트", "함부르크"],
    프랑스: ["파리", "마르세유", "리옹", "니스"],
    이탈리아: ["로마", "밀라노", "베네치아", "피렌체"],
    미국: ["뉴욕", "로스앤젤레스", "시카고", "마이애미"],
    캐나다: ["토론토", "밴쿠버", "몬트리올", "오타와"],
    브라질: ["상파울루", "리우데자네이루", "브라질리아", "살바도르"],
    호주: ["시드니", "멜버른", "브리즈번", "퍼스"],
    러시아: ["모스크바", "상트페테르부르크", "노보시비르스크", "예카테린부르크"],
    "남아프리카 공화국": ["케이프타운", "요하네스버그", "더반", "프리토리아"],
    // Add more countries and cities as needed
  };

  // 선택한 나라에 맞는 도시 목록을 얻음
  const cities = citiesByCountry[country] || [] //citiesByCountry[country]가 undefined 또는 null일 경우 빈 배열 반환

  const [selectedDayIndex, setSelectedDayIndex] = useState(null)
  const [selectedPlaceIndex, setSelectedPlaceIndex] = useState(null)
  const [savedPlaces, setSavedPlaces] = useState([])
  const [isSelectPlace, setIsSelectPlace] = useState(false)

  //검색 키워드, 국내외 관련 처리
  const [searchParams] = useSearchParams()
  const domesticInternational = searchParams.get("di") || "Domestic"
  const status = searchParams.get("status") || "PUBLIC" //"PUBLIC"이거나 "PRIVATE"인 경우 처리
  const navigate = useNavigate()

  //국내 글 작성시 대한민국 자동 선택처리
  useEffect(()=>{
    if (domesticInternational === "Domestic") {
      setCountry("대한민국")
    } else {
      setCountry("")
    }
  }, [domesticInternational])

  // 날짜 초기화
  const handleDateReset = () => {
    setSelectedDateRange([null, null]) // 날짜 범위를 현재 날짜로 초기화
  }

  // 달력에서 날짜를 선택할 때 호출되는 함수
  const handleDateChange = (dateRange) => {
    setSelectedDateRange(dateRange)
    // 날짜 선택 후 캘린더 닫기
    setIsCalendarOpen(false)
  }

  // 날짜 계산 함수
  const calculateDate = (startDate, dayIndex) => {
    const date = new Date(startDate)
    date.setDate(date.getDate() + dayIndex) // 시작 날짜에 dayIndex 만큼 더함
    return date.toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    })
  }

  const handleTagInput = (e) => {
    const value = e.target.value
    setTagInput(value)

    if (value.endsWith(" ") && value.trim() !== "") {
      const newTag = value.trim()
      if (newTag !== "#" && newTag.startsWith("#") && !tags.includes(newTag)) {
        setTags([...tags, newTag])
        setTagInput("")
      }
    }
  }

  //tag 삭제
  const removeTag = (tagToRemove) => setTags(tags.filter((tag) => tag !== tagToRemove))

  //Day 추가
  const addDay = () => setDays([...days, { places: [""], dayMemo: "" }])

  //Day 삭제
  const removeDay = (dayIndex) => {
    if (days.length > 1) {
      setDays(days.filter((_, index) => index !== dayIndex))
    }
  }

  //장소 추가
  const addPlace = (dayIndex) => {
    const newDays = [...days]
    newDays[dayIndex].places.push("")
    setDays(newDays)
  }

  //장소 제거
  const removePlace = (dayIndex, placeIndex) => {
    const newDays = [...days]

    if (newDays[dayIndex].places.length > 1) {
      // 장소 데이터가 2개 이상일 때 UI와 장소 데이터를 모두 삭제
      newDays[dayIndex].places.splice(placeIndex, 1)
    } else {
      // 장소 데이터가 1개일 때 UI는 남기고 장소 데이터만 삭제
      newDays[dayIndex].places[placeIndex] = "" // 빈 값으로 장소 데이터 삭제
    }

    setDays(newDays)
  }

  //장소 선택
  const handlePlaceSelection = (dayIndex, placeIndex) => {
    setSelectedDayIndex(dayIndex)
    setSelectedPlaceIndex(placeIndex)
    setIsSelectPlace(true)
  }

  //선택한 장소 저장
  const handleSavePlace = (place) => {
    if (place && isSelectPlace) {
      const newDays = [...days]

      const currentPlace = newDays[place.dayIndex].places[place.placeIndex]
      const updatedPlace = {
        ...place,
        placeMemo: currentPlace.placeMemo || "", // 기존 메모를 유지
      }

      newDays[place.dayIndex].places[place.placeIndex] = updatedPlace
      setDays(newDays)
      setSavedPlaces([...savedPlaces, updatedPlace])
      setIsSelectPlace(false)
    }
  }

  //Day 메모
  const handleDayMemoChange = (dayIndex, memo) => {
    const newDays = [...days]
    newDays[dayIndex].dayMemo = memo
    setDays(newDays)
  }

  //게시글 작성 완료
  const handleSubmit = () => {
    const post = {
      userId: loggedInUserId,
      writer: loggedInNickname,
      type: "TRIP_LOG",
      title,
      country,
      city,
      startDate: selectedDateRange[0],
      endDate: selectedDateRange[1],
      tags,
      postData: days,
      status: status,
    }
    axios
      .post("/api/v1/posts/trip_log", post)
      .then((res) => {
        navigate(`/posts/trip_log?di=${domesticInternational}`)
      })
      .catch((error) => console.log(error))
  }

  return (
    <div className="container mx-auto p-6 max-w-[900px]">
      <div className="flex flex-col h-full bg-white p-6 shadow-lg rounded-lg">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-3xl font-semibold text-gray-800">
            {status === "PRIVATE" && "나만의 "}
            {domesticInternational === "Domestic" ? "국내 " : "해외 "}여행기록 작성
          </h1>
          <button
            onClick={() => navigate(`/posts/trip_log?di=${domesticInternational}`)}
            className="text-gray-700 bg-gray-200 hover:bg-gray-300 rounded-full text-sm px-5 py-2">
            목록으로 돌아가기
          </button>
          <button
            className="text-white bg-indigo-600 hover:bg-indigo-500 rounded-full text-sm px-5 py-2"
            onClick={handleSubmit}>
            작성 완료
          </button>
        </div>

        <div className="space-y-4">
          <div className="flex mb-4">
            {/* 제목 요소 */}
            <div className="flex-grow-[4]">
              <label htmlFor="title" className="block text-lg font-medium text-gray-700">
                제목
              </label>
              <input
                className="border-gray-300 rounded-md p-2 w-full"
                type="text"
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
            </div>

            {/* 날짜 선택 및 날짜 초기화 버튼 */}
            <div className="flex flex-grow-[1] items-end justify-end ml-4">
              <button
                onClick={() => setIsCalendarOpen(!isCalendarOpen)}
                className="text-gray-700 bg-gray-200 hover:bg-gray-300 rounded-full text-sm px-5 py-2">
                날짜 선택
              </button>
              <button
                onClick={handleDateReset}
                className="text-gray-700 bg-gray-200 hover:bg-gray-300 rounded-full text-sm px-5 py-2 ml-2">
                날짜 초기화
              </button>
            </div>
          </div>
          {/* 선택한 날짜 */}
          <div className="sm:col-span-6">
            <p style={{ marginTop: "-10px", marginBottom: "-20px" }} className="text-sm text-gray-600 text-right">
              {selectedDateRange[0] && selectedDateRange[1]
                ? `${selectedDateRange[0].toLocaleDateString()} ~ ${selectedDateRange[1].toLocaleDateString()}`
                : "0000. 00. 00. ~ 0000. 00. 00."}
            </p>
          </div>

          {/* 캘린더 표시 여부에 따라 렌더링 */}
          {isCalendarOpen && (
            <div className="absolute z-50 bg-white shadow-lg p-2">
              <Calendar
                selectRange={true}
                onChange={handleDateChange}
                value={selectedDateRange || [new Date(), new Date()]} // 초기값 또는 선택된 날짜 범위
              />
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="country" className="block text-lg font-medium text-gray-700">
                나라
              </label>
              <select
                className="border-gray-300 rounded-md p-2 w-full"
                id="country"
                value={country}
                onChange={(e) => {
                  setCountry(e.target.value)
                  setCity("")
                }}>
                {domesticInternational === "Domestic" ? (
                  // Domestic일 경우 대한민국만 표시
                  <option value="대한민국">대한민국</option>
                ) : (
                  // International일 경우 기존 나라 선택 옵션 제공
                  <>
                    <option value="">나라를 선택하세요</option>
                    <optgroup label="아시아">
                      <option value="일본">일본</option>
                      <option value="중국">중국</option>
                      <option value="인도">인도</option>
                    </optgroup>

                    <optgroup label="유럽">
                      <option value="영국">영국</option>
                      <option value="독일">독일</option>
                      <option value="스페인">스페인</option>
                      <option value="프랑스">프랑스</option>
                      <option value="이탈리아">이탈리아</option>
                    </optgroup>

                    <optgroup label="북아메리카">
                      <option value="미국">미국</option>
                      <option value="캐나다">캐나다</option>
                    </optgroup>

                    <optgroup label="남아메리카">
                      <option value="브라질">브라질</option>
                    </optgroup>

                    <optgroup label="오세아니아">
                      <option value="호주">호주</option>
                    </optgroup>

                    <optgroup label="기타">
                      <option value="러시아">러시아</option>
                      <option value="남아프리카 공화국">남아프리카 공화국</option>
                    </optgroup>
                  </>
                )}
              </select>
            </div>

            <div>
              <label htmlFor="city" className="block text-lg font-medium text-gray-700">
                도시
              </label>
              <select
                className="border-gray-300 rounded-md p-2 w-full"
                id="city"
                value={city}
                onChange={(e) => setCity(e.target.value)}
                disabled={!country} //나라가 선택되지 않으면 비활성화
              >
                <option value="">도시를 선택하세요</option>
                {cities.map((cityOption) => (
                  <option key={cityOption} value={cityOption}>
                    {cityOption}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div>
            <label htmlFor="tags" className="block text-lg font-medium text-gray-700">
              태그
            </label>
            <input
              id="tags"
              value={tagInput}
              onChange={handleTagInput}
              placeholder="#태그 입력 후 스페이스바"
              className="border-gray-300 rounded-md p-2 w-full"
            />
            <div className="flex flex-wrap gap-2 mt-2">
              {tags.map((tag, index) => (
                <span key={index} className="bg-indigo-100 text-indigo-800 px-2 py-1 rounded-full">
                  {tag}
                  <button className="ml-2 text-gray-600 hover:text-gray-900" onClick={() => removeTag(tag)}>
                    &times;
                  </button>
                </span>
              ))}
            </div>
          </div>
        </div>

        <div className="mt-6 space-y-6">
          {days.map((day, dayIndex) => (
            <div key={dayIndex} className="bg-gray-50 p-4 rounded-lg shadow-inner">
              <div className="flex justify-between items-center mb-2">
                <h2 className="text-xl font-semibold">Day {dayIndex + 1} - {selectedDateRange && calculateDate(selectedDateRange[0], dayIndex)}</h2>
                <div className="flex space-x-2">
                  <button onClick={addDay} className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded">
                    Day 추가
                  </button>
                  <button
                    className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
                    onClick={() => removeDay(dayIndex)}>
                    Day 삭제
                  </button>
                </div>
              </div>
              <div className="mb-4">
                <label htmlFor={`dayMemo-${dayIndex}`} className="block text-lg font-medium text-gray-700">
                  Day Record
                </label>
                <textarea
                  className="border-gray-300 rounded-md p-2 w-full"
                  id={`dayMemo-${dayIndex}`}
                  value={day.dayMemo || ""}
                  onChange={(e) => handleDayMemoChange(dayIndex, e.target.value)}
                  placeholder="메모를 입력하세요..."
                />
              </div>

              {day.places.map((place, placeIndex) => (
                <div key={placeIndex} className="mb-4">
                  <div className="flex items-center space-x-2">
                    <span className="w-20">{placeIndex + 1}번 장소</span>
                    <button
                      type="button"
                      className="text-white bg-blue-600 hover:bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full text-sm px-4 py-2.5 text-center"
                      onClick={() => handlePlaceSelection(dayIndex, placeIndex)}>
                      장소 선택
                    </button>
                    <div className="flex-grow flex items-center">
                      <input
                        value={place.place_name || ""}
                        className="flex-grow border-gray-300 rounded-md p-2"
                        disabled
                      />
                      <div className="ml-2 w-1/4">
                        <button
                          className={`text-white bg-red-500 hover:bg-red-600 focus:outline-none focus:ring-4 focus:ring-red-300 font-medium rounded-full text-sm px-4 py-2.5 text-center ${day.places.length === 0 ? "opacity-50 cursor-not-allowed" : ""
                            }`}
                          onClick={() => removePlace(dayIndex, placeIndex)}
                          disabled={day.places.length === 0}>
                          삭제
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
              {/* 장소 추가 버튼을 하단 중앙에 배치 */}
              <div className="flex justify-center mt-4">
                <button
                  onClick={() => addPlace(dayIndex)}
                  className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-full">
                  장소 추가
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {isSelectPlace && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div
            className="bg-white p-6 rounded-lg shadow-lg w-2/3 max-w-4xl"
            style={{ maxHeight: "90vh", overflowY: "auto" }}>
            <div className="flex justify-between items-center mb-4">
              <div className="text-xl font-bold">
                Day {selectedDayIndex + 1} : {selectedPlaceIndex + 1}번 장소 선택 중
              </div>
              <button
                onClick={() => setIsSelectPlace(false)}
                className="text-red-600 hover:text-red-800 text-3xl font-bold"
                aria-label="Close">
                &times;
              </button>
            </div>

            {domesticInternational === "Domestic" ? (
              <CourseKakaoMapComponent
                onSave={handleSavePlace}
                selectedDayIndex={selectedDayIndex}
                selectedPlaceIndex={selectedPlaceIndex}
                isSelectPlace={isSelectPlace}
              />
            ) : (
              <CourseGoogleMapComponent
                onSave={handleSavePlace}
                selectedDayIndex={selectedDayIndex}
                selectedPlaceIndex={selectedPlaceIndex}
                isSelectPlace={isSelectPlace}
              />
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default TripBoardFormNew
