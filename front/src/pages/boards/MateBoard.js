import axios from "axios";
import { useEffect, useState } from "react";
import { Link, NavLink, useSearchParams } from "react-router-dom";

function MateBoard() {
   //배열 안에서 객체로 관리
  const [pageData, setPageData] = useState([]);
 
  //파라미터 값 관리
  // URL에서 검색 매개변수를 가져오기 위한 상태
  const [searchParams, setSearchParams] = useSearchParams();
  
  // 사용자가 입력한 검색 조건을 저장하기 위한 상태
  const [searchCriteria, setSearchCriteria] = useState({
    city: "",
    startDate: "",
    endDate: "",
    location: ""
  });

  // searchPrams 에 di 값이 있으면 그 값으로 없다면 Domestic 으로 설정
  // *이후에 di 값이 영향을 미치지는않지만 계속 남아있음
  const [domesticInternational, setDomesticInternational] = useState("Domestic");
  const [pageTurn, setPageTurn] = useState("to International");
  // 페이지 전환 버튼
  const [whereAreYou, setWhereAreYou] = useState(null);

  // searchParams 가 바뀔때마다 실행된다. 
  // searchParams 가 없다면 초기값 "Domestic" 있다면 di 란 key 값의 데이터를 domesticInternational에 전달한다
  useEffect(() => {
    const diValue = searchParams.get("di") || "Domestic"; // 국내/국제 값 가져오기
    const city = searchParams.get("city") || ""; // 도시 가져오기
    const startDate = searchParams.get("startDate") || ""; // 시작 날짜 가져오기
    const endDate = searchParams.get("endDate") || ""; // 종료 날짜 가져오기
    const location = searchParams.get("location") || ""; // 위치 가져오기

    setDomesticInternational(diValue);
    setSearchCriteria({ city, startDate, endDate, location }); // 검색 조건 설정
  }, [searchParams]);

  // domesticInternational 가 바뀔때마다 실행된다.
  // to D~ I~ Button 을 누를때 or 새로운 요청이 들어왔을때
  useEffect(() => {
    axios
      .get("/api/v1/posts/mate")
      .then((res) => {
        const filtered = res.data.list.filter((item) => {
          // 도시 일치 여부: 사용자가 입력한 도시가 존재하면, 해당 도시가 게시물의 도시에 포함되는지 확인
          const matchesCity = searchCriteria.city ? item.city.includes(searchCriteria.city) : true; 
          
          // 시작 날짜 일치 여부: 사용자가 입력한 시작 날짜가 존재하면, 게시물의 시작 날짜가 그 날짜보다 크거나 같은지 확인
          const matchesStartDate = searchCriteria.startDate ? item.startDate >= searchCriteria.startDate : true; 
          
          // 종료 날짜 일치 여부: 사용자가 입력한 종료 날짜가 존재하면, 게시물의 종료 날짜가 그 날짜보다 작거나 같은지 확인
          const matchesEndDate = searchCriteria.endDate ? item.endDate <= searchCriteria.endDate : true; 
          
          // 국내/국제 일치 여부: 현재 선택된 상태에 따라 게시물의 국가가 한국인지 아닌지 확인
          const matchesDomesticInternational = domesticInternational === "Domestic" ? item.country === "한국" : item.country !== "한국"; 

          // 모든 필터 조건이 만족하는 경우에만 해당 게시물을 반환
          return matchesCity && matchesStartDate && matchesEndDate && matchesDomesticInternational; 
        });

        setPageData(filtered); // 필터링된 데이터를 상태에 설정
        setWhereAreYou(domesticInternational === "Domestic" ? "국내 여행 메이트 페이지" : "해외 여행 메이트 페이지"); // 국내/국제에 따라 헤더 설정
        setPageTurn(domesticInternational === "Domestic" ? "to International" : "to Domestic"); // 현재 상태에 따라 버튼 텍스트 변경
      })
      .catch((error) => console.log(error));
  }, [domesticInternational, searchCriteria]);

  // -------------이벤트 관리부

  // 국내/해외 변경 버튼 핸들러-
  const handleButtonClick = () => {
    // 국내 상태에서 눌렀을 때
    //상태 변경
    setDomesticInternational(domesticInternational === "International" ? "Domestic" : "International");
    setSearchParams({
      ...searchCriteria,
      di: domesticInternational === "International" ? "Domestic" : "International",
    });
  };

  // 검색 조건 입력 변화에 대한 처리 함수
  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchCriteria({ ...searchCriteria, [name]: value });
  };

  // 새로운 검색을 시작하는 함수
  const handleSearch = () => {
    setSearchParams({
      location: searchCriteria.location,
      city: searchCriteria.city,
      startDate: searchCriteria.startDate,
      endDate: searchCriteria.endDate,
      di: domesticInternational,
    });
  };

  return (
    <>
      <h3>{whereAreYou}</h3>
      <NavLink to={{ pathname: "/posts/mate/new", search: `?di=${domesticInternational}` }}>새글 작성</NavLink>
      <button className="border border-1 bg-light-green-200" onClick={handleButtonClick}>
        {pageTurn}
      </button>

      {/* 검색 조건 입력 폼 */}
      <div className="my-4">
        {domesticInternational === "International" && ( // 국제 검색일 때 위치 입력 필드 렌더링
          <input
            type="text"
            name="location"
            value={searchCriteria.location}
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
        <input
          type="date"
          name="startDate"
          value={searchCriteria.startDate}
          onChange={handleSearchChange}
          className="border px-2 py-1 mx-2"
        />
        <input
          type="date"
          name="endDate"
          value={searchCriteria.endDate}
          onChange={handleSearchChange}
          className="border px-2 py-1 mx-2"
        />
        <button onClick={handleSearch} className="bg-blue-500 text-white px-4 py-2">
          검색
        </button>
      </div>

      {/* 메이트 게시판 테이블 */}
      <table className="table-auto w-full border divide-y divide-x divide-gray-200">
        <thead className="text-center">
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
            <th>좋아요</th>
          </tr>
        </thead>
        <tbody className="text-center divide-y">
          {pageData.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td className="text-left">
                <div className="flex flex-wrap gap-2 mt-2">
                  <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">{`#${item.country}`}</span>
                  <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">{`#${item.city}`}</span>
                  {item.tags &&
                    item.tags.map((tag, index) => (
                      <span key={index} className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full flex items-center">
                        {tag}
                      </span>
                    ))}
                </div>
                <Link to={`/posts/mate/${item.id}/detail`}>{item.title}</Link>
              </td>
              <td>{item.writer}</td>
              <td>{item.updatedAt ? item.updatedAt : item.createdAt}</td>
              <td>{item.viewCount}</td>
              <td>{item.likeCount}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default MateBoard;
