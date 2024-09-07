import axios from "axios";
import { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";

function MateBoard() {
  //배열 안에서 객체로 관리
  const [pageData, setPageData] = useState([]);

  const [domesticInternational, setDomesticInternational] = useState("Domestic");
  const [pageTurn, setPageTurn] = useState("to International"); // 페이지 전환 버튼
  const [whereAreYou, setWhereAreYou] = useState(null);

  //이벤트 관리부
  const handleButtonClick = () => {
    if (domesticInternational === "International") {
      // 국내 상태에서 눌렀을 때
      //상태 변경
      setDomesticInternational("Domestic");
    } else if (domesticInternational === "Domestic") {
      setDomesticInternational("International");
    }
  };

  useEffect(() => {
    axios
      .get("/api/v1/posts/mate")
      .then((res) => {
        const filteredData = res.data.filter((item) => {
          return domesticInternational === "Domestic"
            ? item.country === "한국"
            : item.country !== "한국";
        });

        setPageData(filteredData);

        setWhereAreYou(
          domesticInternational === "Domestic"
            ? "국내 여행 메이트 페이지"
            : "해외 여행 메이트 페이지"
        );
        setPageTurn(
          domesticInternational === "Domestic"
            ? "to International"
            : "to Domestic"
        );
      })
      .catch((error) => console.log(error));
  }, [domesticInternational]);

  return (
    <>
      <h3>{whereAreYou}</h3>
      <NavLink to={{pathname: '/mateBoard/new', search: `?di=${domesticInternational}`}}>새글 작성</NavLink>
      <button
        className="border border-1 bg-light-green-200"
        onClick={handleButtonClick}
      >
        {pageTurn}
      </button>

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
              <td>{item.id}</td> {/* 글 번호 */}
              <td className="text-left">
                  <>
                    <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">{`#${item.country}`}</span>
                    <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">{`#${item.city}`}</span>
                  </>
                <Link to={`/mateBoard/${item.id}/detail`}>{item.title}</Link>
              </td>
              <td>{item.userId}</td> {/* 작성자 */}
              <td>{item.updatedAt ? item.updatedAt : item.createdAt}</td>
              <td>{item.views}</td> {/* 조회수 */}
              <td>{item.likes}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default MateBoard;
