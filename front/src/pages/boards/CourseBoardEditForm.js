import axios from "axios"
import React, { useEffect, useRef, useState } from "react"
import { useNavigate, useParams, useSearchParams } from "react-router-dom"
import CourseKakaoMapComponent from "../../components/CourseKakaoMapComponent"
import CourseGoogleMapComponent from "../../components/CourseGoogleMapComponent"
import LoadingAnimation from "../../components/LoadingAnimation"
import { citiesByCountry } from "../../constants/mapping"
import moment from "moment"
import Calendar from "react-calendar"
import { FaChevronLeft, FaChevronRight } from "react-icons/fa"


const CourseBoardEditForm = () => {
    //로딩 상태 추가
    const [loading, setLoading] = useState(false)

    // postInfo 하나의 state로 통합 관리
    const [postInfo, setPostInfo] = useState({
        id: "",
        userId: "",
        writer: "",
        type: "COURSE",
        title: "",
        postData: [{ places: [], dayMemo: "" }],
        country: "",
        city: "",
        tags: [],
        status: ""
    })
    const [tagInput, setTagInput] = useState("")

    const calendarRef = useRef(null)

    // 달력에서 선택된 날짜 범위 저장
    const [selectedDateRange, setSelectedDateRange] = useState([null, null])
    const [isCalendarOpen, setIsCalendarOpen] = useState(false) // 캘린더 표시 여부 상태

    const [selectedDayIndex, setSelectedDayIndex] = useState(null)
    const [selectedPlaceIndex, setSelectedPlaceIndex] = useState(null)
    const [savedPlaces, setSavedPlaces] = useState([])
    const [isSelectPlace, setIsSelectPlace] = useState(false)

    const [searchParams] = useSearchParams()
    const domesticInternational = searchParams.get("di") || "Domestic"

    const navigate = useNavigate()
    const { id } = useParams()  // URL에서 게시물 ID를 가져옴

    const cities = citiesByCountry[postInfo.country] || []

    useEffect(() => {
        // 로딩 애니메이션을 0.5초 동안만 표시
        setLoading(true)
        setTimeout(() => {
            setLoading(false)
        }, 700)
        // 기존 게시물 데이터를 가져와 초기화
        axios.get(`/api/v1/posts/${id}/update`)
            .then((res) => {
                setPostInfo(res.data)
            })
            .catch((error) => console.log(error))
    }, [id])

    const handleSubmit = () => {
        if (!postInfo.title) {
            alert("제목을 입력해주세요.")
            return
        }

        if (!postInfo.country) {
            alert("나라를 선택해주세요.")
            return
        }

        const submittedPostInfo = {
            ...postInfo,
            startDate: selectedDateRange[0],
            endDate: selectedDateRange[1],
        }

        axios.put(`/api/v1/posts/${id}`, submittedPostInfo)  // PUT 요청으로 업데이트
            .then((res) => {
                alert("수정했습니다")
                // 업데이트 후 해당글 자세히보기로 이동
                navigate(`/posts/course/${id}/detail?di=${domesticInternational}`)
            })
            .catch((error) => console.log(error))
    }

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

    // 캘린더의 날짜 스타일을 설정하는 함수 추가
    const tileClassName = ({ date }) => {
        const day = date.getDay() // 0: 일요일, 1: 월요일, ..., 6: 토요일
        // 기본적으로 검은색으로 설정
        let className = "text-black"

        // 토요일과 일요일에만 빨간색으로 변경
        if (day === 0 || day === 6) {
            className = "text-red-500" // 토요일과 일요일에 숫자를 빨간색으로 표시
        }

        return className // 최종 클래스 이름 반환
    }

    const calculateNightsAndDays = (startDate, endDate) => {

        if (!startDate || !endDate) return ""

        // Date 객체로 초기화
        const endDateTime = new Date(endDate)
        const startDateTime = new Date(startDate)

        // 두 날짜 간의 차이를 밀리초 단위로 계산
        const diffTime = endDateTime.getTime() - startDateTime.getTime()


        // 차이를 일(day) 단위로 변환
        const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

        if (diffDays > 0) {
            // "박"의 계산 (diffDays - 1)
            const days = diffDays + 1
            // const nights = diffDays > 0 ? diffDays : 0
            const nights = diffDays

            return `(${nights}박 ${days}일)`
        } else {
            return `(당일 일정)`
        }
    }

    //태그 입력
    const handleTagInput = (e) => {
        const value = e.target.value

        // 태그 길이 15자로 제한
        if (value.length > 15) {
            alert("태그는 최대 15자까지 입력 가능합니다.")
            return
        }

        setTagInput(value)

        if (value.endsWith(" ") && value.trim() !== "") {
            const newTag = value.trim()

            //조건: #으로 시작, 중복 방지, # 단독 입력 방지
            if (newTag !== "#" && newTag.startsWith("#") && !postInfo.tags.includes(newTag)) {
                // 태그 최대 6개로 제한
                if (postInfo.tags.length >= 6) {
                    alert("태그는 최대 6개까지 추가할 수 있습니다.")
                    return
                }

                setPostInfo((prev) => ({
                    ...prev,
                    tags: [...prev.tags, newTag]
                }))
                e.target.value = ""
            }
        }
    }

    //태그 지우기 버튼
    const removeTag = (tagToRemove) => {
        setPostInfo((prev) => ({
            ...prev,
            tags: prev.tags.filter((tag) => tag !== tagToRemove)
        }))
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

    //Day 추가 버튼
    const addDay = () => {
        setPostInfo((prev) => ({
            ...prev,
            postData: [...prev.postData, { places: [{ place_name: "", placeMemo: "" }], dayMemo: "" }]
        }))
    }

    //Day 삭제 버튼
    const removeDay = (dayIndex) => {
        if (postInfo.postData.length > 1) {
            setPostInfo((prev) => ({
                ...prev,
                postData: prev.postData.filter((_, index) => index !== dayIndex)
            }))
        }
    }

    //장소 추가 버튼
    const addPlace = (dayIndex) => {
        const newDays = [...postInfo.postData]
        newDays[dayIndex].places.push("")
        setPostInfo((prev) => ({
            ...prev,
            postData: newDays
        }))
    }

    //장소 삭제 버튼
    const removePlace = (dayIndex, placeIndex) => {
        const newDays = [...postInfo.postData]

        if (newDays[dayIndex].places.length > 1) {
            // 장소 데이터가 2개 이상일 때 UI와 장소 데이터를 모두 삭제
            newDays[dayIndex].places.splice(placeIndex, 1)
        } else {
            // 장소 데이터가 1개일 때 UI는 남기고 장소 데이터만 삭제
            newDays[dayIndex].places[placeIndex] = ""
        }

        setPostInfo((prev) => ({
            ...prev,
            postData: newDays
        }))
    }

    //장소 선택 버튼
    const handlePlaceSelection = (dayIndex, placeIndex) => {
        setSelectedDayIndex(dayIndex)
        setSelectedPlaceIndex(placeIndex)
        setIsSelectPlace(true)
    }

    //장소 저장 버튼
    const handleSavePlace = (place) => {
        if (place && isSelectPlace) {
            const newDays = [...postInfo.postData]
            const currentPlace = newDays[place.dayIndex].places[place.placeIndex]
            const updatedPlace = {
                ...place,
                placeMemo: currentPlace.placeMemo || "",
            }

            newDays[place.dayIndex].places[place.placeIndex] = updatedPlace
            setPostInfo((prev) => ({
                ...prev,
                postData: newDays
            }))
            setSavedPlaces([...savedPlaces, updatedPlace])
            setIsSelectPlace(false)
        }
    }

    //장소 메모 
    const handlePlaceMemoChange = (dayIndex, placeIndex, memo) => {
        const newDays = [...postInfo.postData]
        newDays[dayIndex].places[placeIndex] = {
            ...newDays[dayIndex].places[placeIndex],
            placeMemo: memo,
        }
        setPostInfo((prev) => ({
            ...prev,
            postData: newDays
        }))
    }

    //Day 메모
    const handleDayMemoChange = (dayIndex, memo) => {
        const newDays = [...postInfo.postData]
        newDays[dayIndex].dayMemo = memo
        setPostInfo((prev) => ({
            ...prev,
            postData: newDays
        }))
    }

    return (
        <div className="container mx-auto p-6 max-w-[900px] bg-gradient-to-r from-green-100 to-white rounded-xl shadow-lg">
            {/* 로딩 애니메이션 */}
            {loading && <LoadingAnimation />}
            <div className="flex flex-col h-full bg-white p-6 shadow-xl rounded-lg">
                <div className="flex justify-between items-center mb-4">
                    <h1 className="text-4xl font-bold text-gray-900">
                        여행 코스 수정
                    </h1>
                    <div className="flex justify-end space-x-4">
                        <button
                            onClick={() => navigate(`/posts/course/${id}/detail?di=${domesticInternational}`)}
                            className="text-white bg-green-400 hover:bg-green-700 rounded-full text-sm font-bold px-6 py-2 shadow-md transition duration-150">
                            게시글로
                        </button>
                        <button
                            className="text-white bg-tripDuoMint hover:bg-tripDuoGreen rounded-full text-sm font-bold px-6 py-2 shadow-lg transition duration-150"
                            onClick={handleSubmit}>
                            수정 완료
                        </button>
                    </div>
                </div>

                <div className="pl-6">
                    <div className="flex mb-4">
                        {/* 제목 요소 */}
                        <div className="flex-grow-[4]">
                            <input
                                className="border-gray-300 rounded-md p-2 w-full"
                                type="text"
                                id="title"
                                value={postInfo.title}
                                onChange={(e) =>
                                    setPostInfo((prev) => ({ ...prev, title: e.target.value }))
                                }
                                maxLength={50}
                            />
                        </div>

                        {/* 날짜 선택 및 날짜 초기화 버튼 */}
                        <div className="flex flex-grow-[1] items-end justify-end ml-4 mr-6">
                            <button
                                onClick={() => setIsCalendarOpen(!isCalendarOpen)}
                                className="text-green-900 text-sm font-bold border-2 border-green-900 hover:bg-indigo-200 rounded-full px-4 py-1"
                            >
                                날짜 선택
                            </button>
                            <button
                                onClick={handleDateReset}
                                className="text-sm text-gray-500 border-2 hover:bg-indigo-200 rounded-full px-3 py-1 ml-2"
                            >
                                날짜 초기화
                            </button>
                        </div>
                    </div>

                    {/* 선택한 날짜 */}
                    <div className="sm:col-span-6 mr-6">
                        {
                            selectedDateRange[0] !== null ?
                                <p style={{ marginTop: '-10px', marginBottom: '0px' }} className="text-sm text-gray-600 text-right">
                                    {selectedDateRange[0] ? moment(selectedDateRange[0]).format("YYYY. MM. DD") : "0000. 00. 00."} ~
                                    {selectedDateRange[1] ? moment(selectedDateRange[1]).format("YYYY. MM. DD") : "0000. 00. 00."}
                                    {calculateNightsAndDays(selectedDateRange[0], selectedDateRange[1])}
                                </p>
                                :
                                <p style={{ marginTop: '-10px', marginBottom: '0px' }} className="text-sm text-gray-600 text-right">
                                    {postInfo.startDate ? moment(postInfo.startDate).format("YYYY. MM. DD") : "0000. 00. 00."} ~
                                    {postInfo.endDate ? moment(postInfo.endDate).format("YYYY. MM. DD") : "0000. 00. 00."}
                                    {calculateNightsAndDays(postInfo.startDate, postInfo.endDate)}
                                </p>
                        }
                    </div>

                    {/* 캘린더 표시 여부에 따라 렌더링 */}
                    <div ref={calendarRef}>
                        {isCalendarOpen && (
                            <div className="absolute z-50 bg-white shadow-lg p-2">
                                <button
                                    onClick={handleDateReset}
                                    className="text text-sm absolute top-8 right-20 bg-tripDuoGreen text-white px-2 py-1 rounded hover:bg-green-700 transition duration-150">
                                    today
                                </button>
                                <Calendar
                                    selectRange={true}
                                    className="w-full p-4 bg-white rounded-lg border-none" // 달력 컴포넌트의 테두리를 없애기 위해 border-none 추가
                                    onChange={handleDateChange}
                                    value={selectedDateRange || [new Date(), new Date()]} // 초기값 또는 선택된 날짜 범위
                                    minDetail="month" // 상단 네비게이션에서 '월' 단위만 보이게 설정
                                    maxDetail="month" // 상단 네비게이션에서 '월' 단위만 보이게 설정
                                    navigationLabel={null}
                                    showNeighboringMonth={false} //  이전, 이후 달의 날짜는 보이지 않도록 설정
                                    calendarType="hebrew" //일요일부터 보이도록 설정
                                    tileClassName={tileClassName} // 날짜 스타일 설정
                                    formatYear={(locale, date) => moment(date).format("YYYY")} // 네비게이션 눌렀을때 숫자 년도만 보이게
                                    formatMonthYear={(locale, date) => moment(date).format("YYYY. MM")} // 네비게이션에서 2023. 12 이렇게 보이도록 설정
                                    prevLabel={
                                        <FaChevronLeft className="text-green-500 hover:text-green-700 transition duration-150 mx-auto" />
                                    }
                                    nextLabel={
                                        <FaChevronRight className="text-green-500 hover:text-green-700 transition duration-150 mx-auto" />
                                    }
                                    prev2Label={null}
                                    next2Label={null}
                                    tileContent={({ date }) => {
                                        return (
                                            <span className={date.getDay() === 0 || date.getDay() === 6 ? "text-red-500" : "text-black"}>
                                                {date.getDate()} {/* 날짜 숫자만 표시 */}
                                            </span>
                                        )
                                    }} // 날짜 내용 설정
                                    formatDay={() => null}
                                />
                            </div>
                        )}
                    </div>

                    <div className="w-5/6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <select
                                    className="border-gray-300 rounded-md p-1 text-sm w-full"
                                    id="country"
                                    value={postInfo.country}
                                    onChange={(e) =>
                                        setPostInfo((prev) => ({ ...prev, country: e.target.value }))
                                    }
                                >
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
                                <select
                                    className="border-gray-300 rounded-md p-1 text-sm w-full"
                                    id="city"
                                    value={postInfo.city}
                                    onChange={(e) =>
                                        setPostInfo((prev) => ({ ...prev, city: e.target.value }))
                                    }
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

                        <div className="mt-4">
                            <input
                                id="tags"
                                value={tagInput}
                                onChange={handleTagInput}
                                placeholder="#태그 입력 후 스페이스바"
                                className="border-gray-300 rounded-md p-1 text-sm w-full"
                            />
                            <div className="flex flex-wrap gap-2 mt-2">
                                {postInfo.tags.map((tag, index) => (
                                    <span key={index} className="bg-indigo-100 text-indigo-800 text-sm font-bold px-2 py-1 rounded-md">
                                        {tag}
                                        <button
                                            className="ml-2 text-gray-600 hover:text-gray-900"
                                            onClick={() => removeTag(tag)}>
                                            &times;
                                        </button>
                                    </span>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>

                <div className="mt-4 space-y-6">
                    {postInfo.postData.map((day, dayIndex) => (
                        <div key={dayIndex} className="bg-gray-50 p-6 rounded-lg shadow-md">
                            <div className="flex justify-between items-center mb-2">
                                <h2 className="text-xl font-semibold">Day {dayIndex + 1} - {postInfo.startDate && calculateDate(postInfo.startDate, dayIndex)}</h2>
                                <div className="flex space-x-2">
                                    <button
                                        onClick={addDay}
                                        className="border-2 border-blue-900 hover:bg-blue-100 text-blue-900 px-2 py-1 rounded-md shadow-lg transition duration-300 text-sm font-bold">
                                        Day 추가
                                    </button>
                                    <button
                                        className="border-2 border-red-700 hover:bg-red-100 text-red-700 px-2 py-1 rounded-md shadow-lg transition duration-300 text-sm font-bold"
                                        onClick={() => removeDay(dayIndex)}>
                                        Day 삭제
                                    </button>
                                </div>
                            </div>

                            <div className="mb-4">
                                <label htmlFor={`dayMemo-${dayIndex}`} className="block text-lg font-medium text-gray-700">
                                    Day Memo
                                </label>
                                <textarea
                                    className="border-gray-300 rounded-md p-2 w-full"
                                    id={`dayMemo-${dayIndex}`}
                                    value={day.dayMemo || ""}
                                    onChange={(e) => handleDayMemoChange(dayIndex, e.target.value)}
                                    placeholder="메모를 입력하세요..."
                                    maxLength={200}
                                />
                            </div>

                            {day.places.map((place, placeIndex) => (
                                <div key={placeIndex} className="mb-4">
                                    <div className="flex items-center space-x-2">
                                        <span className="w-20">
                                            {placeIndex + 1}번 장소: {place.place_name || "장소 없음"}
                                        </span>
                                        <button
                                            type="button"
                                            className="text-blue-900 text-sm font-bold border border-blue-900 hover:bg-blue-100 px-2 py-1 rounded-md shadow-lg transition duration-300"
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
                                                    className={`border border-red-700 hover:bg-red-100 text-red-700 px-2 py-1 rounded-md shadow-lg transition duration-300 text-sm font-bold ${day.places.length === 0 ? "opacity-50 cursor-not-allowed" : ""
                                                        }`}
                                                    onClick={() => removePlace(dayIndex, placeIndex)}
                                                    disabled={day.places.length === 0}>
                                                    삭제
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <label htmlFor={`placeMemo-${dayIndex}-${placeIndex}`} className="text-sm text-gray-700">
                                            장소 메모
                                        </label>
                                        <input
                                            className="border-gray-300 rounded-md p-2 w-full"
                                            type="text"
                                            id={`placeMemo-${dayIndex}-${placeIndex}`}
                                            value={place.placeMemo || ""}
                                            onChange={(e) => handlePlaceMemoChange(dayIndex, placeIndex, e.target.value)}
                                            maxLength={150}
                                        />
                                    </div>
                                </div>


                            ))}

                            {/* 장소 추가 버튼 */}
                            <div className="flex justify-center mt-4">
                                <button
                                    onClick={() => addPlace(dayIndex)}
                                    className="border-2 border-blue-900 hover:bg-blue-100 text-blue-900 px-4 py-2 rounded-md shadow-lg transition duration-300 text-sm font-bold"
                                >
                                    장소 추가
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {isSelectPlace && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-2/3 max-w-4xl" style={{ maxHeight: '90vh', overflowY: 'auto' }}>
                        <div className="flex justify-between items-center mb-4">
                            <div className="text-xl font-bold">Day {selectedDayIndex + 1} : {selectedPlaceIndex + 1}번 장소 선택 중</div>
                            <button
                                onClick={() => setIsSelectPlace(false)}
                                className="text-red-600 hover:text-red-800 text-3xl font-bold"
                                aria-label="Close"
                            >
                                &times;
                            </button>
                        </div>

                        {postInfo.country === "대한민국" ? (
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

export default CourseBoardEditForm
