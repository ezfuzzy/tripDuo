import axios from "axios";
import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import moment from "moment";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faHeart, faMessage } from "@fortawesome/free-solid-svg-icons";

function MateBoard() {
  //배열 안에서 객체로 관리
  const [pageData, setPageData] = useState([]);

  //파라미터 값 관리
  // URL에서 검색 매개변수를 가져오기 위한 상태
  const [searchParams, setSearchParams] = useSearchParams();
  const [shouldFetchData, setShouldFetchData] = useState(false);

  // 사용자가 입력한 검색 조건을 저장하기 위한 상태
  const [searchCriteria, setSearchCriteria] = useState({
    country: "",
    city: "",
    startDate: "",
    endDate: "",
    keyword: "",
    condition: "title", // 검색 옵션: 제목 또는 작성자
  });

  // *이후에 di 값이 영향을 미치지는않지만 계속 남아있음
  const [domesticInternational, setDomesticInternational] = useState();
  const [pageTurn, setPageTurn] = useState("to International");

  // 페이지 전환 버튼
  const [whereAreYou, setWhereAreYou] = useState(null);
  const [originalData, setOriginalData] = useState([]); // 원본 데이터를 저장
  const [sortBy, setSortBy] = useState("latest"); // 정렬 기준 초기값 설정

  // 달력에서 선택된 날짜 범위 저장
  const [selectedDateRange, setSelectedDateRange] = useState([null, null]);
  const [isCalendarOpen, setIsCalendarOpen] = useState(false); // 캘린더 표시 여부 상태

  // 검색 기준 변경 핸들러
  const handleConditionChange = (e) => {
    setSearchCriteria({
      ...searchCriteria,
      condition: e.target.value,
      keyword: "",
    });
  };

  // 검색 조건 입력 변화에 대한 처리 함수
  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchCriteria({ ...searchCriteria, [name]: value });
  };

  // 제목 또는 작성자 검색 쿼리 처리
  const handleQueryChange = (e) => {
    const value = e.target.value;
    setSearchCriteria({
      ...searchCriteria,
      keyword: value, // 검색어를 keyword로 저장
    });
  };

  // 날짜 초기화
  const handleDateReset = () => {
    setSelectedDateRange([null, null]); // 날짜 범위를 현재 날짜로 초기화
    setSearchCriteria({
      ...searchCriteria,
      startDate: "", // 시작 날짜 초기화
      endDate: "", // 종료 날짜 초기화
    });
  };
  // 현재 날짜로 돌아오는 함수 추가
  const handleTodayClick = () => {
    const today = new Date();
    setSelectedDateRange([today, today]); // 현재 날짜로 설정
    setSearchCriteria({
      ...searchCriteria,
      startDate: today.toLocaleDateString("ko-KR"),
      endDate: today.toLocaleDateString("ko-KR"),
    });
  };
  // 달력에서 날짜를 선택할 때 호출되는 함수
  const handleDateChange = (dateRange) => {
    setSelectedDateRange(dateRange);
    setIsCalendarOpen(false); // 날짜 선택 후 캘린더 닫기
    setSearchCriteria({
      ...searchCriteria,
      startDate: dateRange[0] ? dateRange[0].toLocaleDateString("ko-KR") : "",
      endDate: dateRange[1] ? dateRange[1].toLocaleDateString("ko-KR") : "",
    });
  };

  // searchParams 가 바뀔때마다 실행된다.
  // searchParams 가 없다면 초기값 "Domestic" 있다면 di 란 key 값의 데이터를 domesticInternational에 전달한다
  useEffect(() => {
    const diValue = searchParams.get("di") || "Domestic"; // 국내/국제 값 가져오기
    const city = searchParams.get("city") || ""; // 도시 가져오기
    const startDate = searchParams.get("startDate") || ""; // 시작 날짜 가져오기
    const endDate = searchParams.get("endDate") || ""; // 종료 날짜 가져오기
    const country = searchParams.get("country") || ""; // 국가 가져오기
    const keyword = searchParams.get("keyword") || "";

    setDomesticInternational(diValue);
    setSearchCriteria({ city, startDate, endDate, country, keyword, condition: searchCriteria.condition }); // 검색 조건 설정
    // 국내/국제 값 업데이트
  }, [searchParams]);

  // domesticInternational 가 바뀔때마다 실행된다.
  // to D~ I~ Button 을 누를때 or 새로운 요청이 들어왔을때
  const fetchData = () => {
    axios
      .get("/api/v1/posts/mate")
      .then((res) => {
        let filtered = res.data.list;
  
        // 검색 조건이 있을 경우 필터링
        if (shouldFetchData) {
          filtered = filtered.filter((item) => {
            // 국내/해외 필터링
            const matchesDomesticInternational =
              domesticInternational === "Domestic" ? item.country === "한국" : item.country !== "한국";
            if (!matchesDomesticInternational) return false;
  
            // 국가 필터링
            const matchesCountry = searchCriteria.country ? item.country.includes(searchCriteria.country) : true;
            if (!matchesCountry) return false;
  
            // 도시 필터링
            const matchesCity = searchCriteria.city ? item.city.includes(searchCriteria.city) : true;
            if (!matchesCity) return false;
  
            // 키워드 필터링
            const matchesKeyword =
              searchCriteria.condition === "title"
                ? item.title.includes(searchCriteria.keyword)
                : searchCriteria.condition === "writer"
                ? item.writer.includes(searchCriteria.keyword)
                : searchCriteria.condition === "content"
                ? item.content.includes(searchCriteria.keyword)
                : searchCriteria.condition === "title_content"
                ? item.title.includes(searchCriteria.keyword) || item.content.includes(searchCriteria.keyword)
                : true;
  
            if (!matchesKeyword) return false;
  
            // 날짜 범위 필터링
            const matchesDateRange = (item) => {
              const itemStartDate = new Date(item.startDate);
              const itemEndDate = new Date(item.endDate);
              const searchStartDate = searchCriteria.startDate ? new Date(searchCriteria.startDate) : null;
              const searchEndDate = searchCriteria.endDate ? new Date(searchCriteria.endDate) : null;
  
              if (!searchStartDate && !searchEndDate) {
                return true;
              }
  
              return (
                (itemStartDate < searchEndDate && itemEndDate > searchStartDate) ||
                (itemStartDate <= searchStartDate && itemEndDate >= searchStartDate) ||
                (itemStartDate <= searchEndDate && itemEndDate >= searchEndDate)
              );
            };
  
            if (!matchesDateRange(item)) return false;
  
            return true;
          });
        }
        setOriginalData(filtered);
        setPageData(filtered);
        applySorting(filtered);
        // setWhereAreYou(domesticInternational === "Domestic" ? "국내 여행 메이트 페이지" : "해외 여행 메이트 페이지");
        setPageTurn(domesticInternational === "Domestic" ? "to International" : "to Domestic");
      })
      .catch((error) => console.log(error));
  };
  

  useEffect(() => {
    fetchData(); // 컴포넌트가 마운트될 때 데이터를 불러옵니다.
  }, []); // 빈 배열을 사용하여 초기 로딩 시에만 실행됩니다.

  useEffect(() => {
    fetchData(); // di가 변경될 때마다 데이터 로딩
  }, [domesticInternational]);
  
  const applySorting = (data) => {
    const sorted = [...data].sort((a, b) => {
      if (sortBy === "latest") {
        return new Date(b.createdAt) - new Date(a.createdAt); // 최신순
      } else if (sortBy === "viewCount") {
        return b.viewCount - a.viewCount; // 조회수순
      } else if (sortBy === "likeCount") {
        return b.likeCount - a.likeCount; // 좋아요순
      }
      return 0; // 기본값
    });
    setPageData(sorted); // 정렬된 데이터를 업데이트
  };
  
  
  useEffect(() => {
    applySorting(originalData); // 정렬 기준이 변경될 때마다 원본 데이터를 정렬하여 적용
  }, [sortBy, originalData]);

  
  // -------------이벤트 관리부

  // 국내/해외 변경 버튼 핸들러-
  const handleButtonClick = () => {
    // 현재 상태에 따라 새로운 상태를 결정
    const newInternationalState = domesticInternational === "International" ? "Domestic" : "International";
    setDomesticInternational(newInternationalState);

    // 여기서 whereAreYou 상태를 업데이트합니다.
    setWhereAreYou(newInternationalState === "Domestic" ? "국내 여행 메이트 페이지" : "해외 여행 메이트 페이지");

    const newSearchParams = {
      di: newInternationalState,
    };
  
    // 검색 조건 초기화
    setSearchCriteria({
      country: "", // 초기화
      city: "",    // 초기화
      startDate: "", // 초기화
      endDate: "",   // 초기화
      keyword: "",   // 초기화
      condition: "title", // 기본 검색 조건으로 초기화
    });

    setSearchParams(newSearchParams);

    // 데이터를 불러오기 위한 상태 변경
    setShouldFetchData(true); // 게시물 불러오기

    // 상태 변경 후 자동으로 데이터를 불러옵니다.
    fetchData(); // 인자를 전달하지 않음
};


  // useEffect를 추가하여 초기 로딩 시 기본 게시물 불러오기
  useEffect(() => {
    // 초기 상태 설정
    setDomesticInternational("Domestic"); // di를 "Domestic"으로 설정
    setWhereAreYou("국내 여행 메이트 페이지"); // 초기 메시지 설정

    // 검색 파라미터를 업데이트
    setSearchParams({
      di: "Domestic", // 초기 검색 파라미터 설정
    });

    fetchData(); // 기본 게시물 불러오기
  }, []);

  // 새로운 검색을 시작하는 함수
  const handleSearch = () => {
  const newSearchParams = {
    di: domesticInternational,
  };

  if (searchCriteria.country) {
    newSearchParams.country = searchCriteria.country;
  }
  if (searchCriteria.city) {
    newSearchParams.city = searchCriteria.city;
  }
  if (searchCriteria.startDate) {
    newSearchParams.startDate = searchCriteria.startDate;
  }
  if (searchCriteria.endDate) {
    newSearchParams.endDate = searchCriteria.endDate;
  }
  if (searchCriteria.keyword) {
    newSearchParams.keyword = searchCriteria.keyword;
  }

  setSearchParams(newSearchParams);
  setShouldFetchData(true); // 데이터를 불러오기 위한 상태 변경
  fetchData(); // 검색 버튼 클릭 시 데이터를 바로 불러옵니다.
};


  const handleSortChange = (e) => {
    setSortBy(e.target.value); // 정렬 기준을 업데이트
    applySorting(originalData); // 기존 데이터를 정렬 기준에 맞게 다시 정렬
  };
  
  useEffect(() => {
  applySorting(originalData); // 정렬 기준이 변경되었을 때 기존 데이터를 정렬
}, [sortBy, originalData]);


  // 캘린더의 날짜 스타일을 설정하는 함수 추가
  const tileClassName = ({ date }) => {
    const day = date.getDay(); // 0: 일요일, 1: 월요일, ..., 6: 토요일
    // 기본적으로 검은색으로 설정
    let className = 'text-black'; 
  
    // 토요일과 일요일에만 빨간색으로 변경
    if (day === 0 || day === 6) {
      className = 'text-red-500'; // 토요일과 일요일에 숫자를 빨간색으로 표시
    }
  
    return className; // 최종 클래스 이름 반환
  };
  

  return (
    <div className="container mx-auto m-4">
     <Link className="px-4 py-2 text-sm font-medium rounded-md bg-green-600 text-gray-100 mr-3" to={{ pathname: "/posts/mate/new", search: `?di=${domesticInternational}` }}>새글 작성</Link>
      <button className="px-4 py-2 text-sm font-medium rounded-md bg-gray-600 text-gray-100" onClick={handleButtonClick}>
        {pageTurn}
      </button>
      <h4 className="font-bold mb-4">{whereAreYou}</h4>

      {/* 검색 조건 입력 폼 */}
      <div className="my-4">
        {domesticInternational === "International" && ( // 국제 검색일 때 위치 입력 필드 렌더링
          <input
            type="text"
            name="country"
            value={searchCriteria.country}
            onChange={handleSearchChange}
            placeholder="국가"
            className="border px-2 py-1 mx-2"
          />
        )}
        <input
          type="text"
          name="city"
          value={searchCriteria.city}
          onChange={handleSearchChange}
          placeholder="도시"
          className="border px-2 py-1 mx-2"
        />

        {/* 제목/작성자 검색 옵션 선택 */}
        <div className="flex items-center mb-2">
          <select value={searchCriteria.condition} onChange={handleConditionChange} className="border px-2 py-1 mx-2">
            <option value="title">제목</option>
            <option value="writer">작성자</option>
            <option value="content">내용</option>
            <option value="title+writer">제목 + 내용</option>
          </select>
        </div>

        <input
          type="text"
          name={searchCriteria.condition} // 동적으로 이름 설정
          value={searchCriteria[searchCriteria.condition]} // 검색 타입에 따라 값 설정
          onChange={handleQueryChange}
          placeholder={searchCriteria.condition === "title" ? "제목" : "작성자"}
          className="border px-2 py-1 mx-2"
        />
        <div>
          {/* 날짜 선택 버튼 */}
          <button
            onClick={() => setIsCalendarOpen(!isCalendarOpen)} // 버튼 클릭 시 캘린더 표시/숨김 토글
            className="bg-blue-500 text-white px-4 py-2 mb-4">
            {selectedDateRange[0] && selectedDateRange[1] // 날짜가 선택되었을 때
              ? `${selectedDateRange[0].toLocaleDateString()} ~ ${selectedDateRange[1].toLocaleDateString()}` // 수정된 부분
              : "날짜 선택"}{" "}
            {/* 날짜가 선택되지 않았을 때 */}
          </button>

          {/* 캘린더 표시 여부에 따라 렌더링 */}
          {isCalendarOpen && (
            <div className="absolute z-50 bg-white shadow-lg p-2">
              <Calendar
                selectRange={true}
                onChange={handleDateChange}
                value={selectedDateRange || [new Date(), new Date()]} // 초기값 또는 선택된 날짜 범위
                minDetail="month" // 상단 네비게이션에서 '월' 단위만 보이게 설정
                maxDetail="month" // 상단 네비게이션에서 '월' 단위만 보이게 설정
                navigationLabel={null}
                showNeighboringMonth={false} //  이전, 이후 달의 날짜는 보이지 않도록 설정
                calendarType="hebrew" //일요일부터 보이도록 설정
                tileClassName={tileClassName} // 날짜 스타일 설정 
                tileContent={({ date }) => {
                  const day = date.getDay();
                  return (
                    <span className={date.getDay() === 0 || date.getDay() === 6 ? 'text-red-500' : 'text-black'}>
                      {date.getDate()} {/* 날짜 숫자만 표시 */}
                    </span>
                  );  
                }} // 날짜 내용 설정
                formatDay={() => null}
              />
              <button onClick={handleDateReset} className="bg-red-500 text-white px-4 py-2 ml-2">
                날짜 초기화
              </button>
              <button onClick={handleTodayClick} className="bg-green-500 text-white px-4 py-2 ml-2">
                오늘로 돌아가기
              </button>
            </div>
          )}
        </div>
        <button onClick={handleSearch} className="bg-blue-500 text-white px-4 py-2">
          검색
        </button>
      </div>

      {/* 검색 정렬 기준 다운바 */}
      <div className="my-4">
        <label htmlFor="sortBy" className="mr-2">
          정렬 기준:
        </label>
        <select id="sortBy" value={sortBy} onChange={handleSortChange} className="border px-2 py-1">
          <option value="latest">최신순</option>
          <option value="viewCount">조회수순</option>
          <option value="likeCount">좋아요순</option>
        </select>
      </div>

      {/* 메이트 게시판 테이블 */}
      <table className="table-auto w-full border divide-y divide-x divide-gray-200">
        <thead className="text-center">
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>날짜</th>
            <th>작성일</th>
            <th>조회수/댓글수/좋아요</th>
          </tr>
        </thead>
        <tbody className="text-center divide-y">
          {pageData.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td className="text-left">
                <div className="flex flex-wrap gap-2 mt-2">
                  <span className="text-sm bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">{`#${item.country}`}</span> 
                  <span className="text-sm bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">{`#${item.city}`}</span> 
                  {item.tags &&
                    item.tags.map((tag, index) => (
                      <span key={index} className="text-sm bg-blue-100 text-blue-800 px-2 py-1 rounded-full flex items-center">
                        {tag}
                      </span>
                    ))}
                </div>
                <Link to={`/posts/mate/${item.id}/detail`}>{item.title}</Link>
              </td>
              <td>{item.writer}</td>
              <td>
                {item.startDate} ~ {item.endDate}
              </td>
              <td className="text-xs ">
                {item.updatedAt ? (
                  <span className="bg-yellow-100 text-yellow-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded">
                    update
                  </span>
                ) : (
                  <span className="px-8"></span>
                )}
                {new Date(item.updatedAt ? item.updatedAt : item.createdAt).toLocaleDateString("ko-KR", {
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                  hour: "numeric",
                  minute: "numeric",
                  second: "numeric",
                })}
              </td>
              {/* 조회수, 좋아요, 덧글 수 */}
              <td>
                <span className="text-sm text-gray-500">
                  <span className="mx-3">
                    <FontAwesomeIcon icon={faEye} className="h-4 w-4 mr-2" />
                    {item.viewCount}
                  </span>
                  <span className="mr-3">
                    <FontAwesomeIcon icon={faHeart} className="h-4 w-4 mr-2" />
                    {item.likeCount}
                  </span>
                  <span className="mr-3">
                    <FontAwesomeIcon icon={faMessage} className="h-4 w-4 mr-2" />
                    {item.commentCount}
                  </span>
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default MateBoard;