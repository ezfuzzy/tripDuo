import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { Link } from "react-router-dom";

function MateBoard() {
  //배열 안에서 객체로 관리
  const [pageData, setPageData] = useState([
    {
      id: 1,
      userId: "Sample Id",
      type: "Sample Type",
      title: "SAMPLE TITLE",
      locationCountry: "SampCountry",
      locationCity: "SampCity",
      tags: "",
      views: "165",
      likes: "6",
      rating: "78",
      status: "",
      createdAt: "2024-09-03 16:21",
      updatedAt: "2024-09-03 16:34",
    },
    {
      id: 2,
      userId: "Sample Id2",
      type: "Sample Type2",
      title: "SAMPLE TITLE2",
      locationCountry: "SampCountry2",
      locationCity: "SampCity2",
      tags: "",
      views: "178",
      likes: "12",
      rating: "86",
      status: "",
      createdAt: "2024-09-03 16:26",
      updatedAt: "2024-09-03 16:51",
    },
  ]);

  const [domesticInternational, setDomesticInternational] =
    useState("International");
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
    if (domesticInternational === "International") {
      // 국내 상태에서 눌렀을 때
      //국내 메이트 게시판 요청
      setWhereAreYou("국내 여행 메이트 페이지");
    } else if (domesticInternational === "Domestic") {
      //해외 메이트 게시판 요청
      setWhereAreYou("해외 여행 메이트 페이지");
    }
  }, [domesticInternational]);

  return (
    <>
      <h3>{whereAreYou}</h3>
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
                {domesticInternational === "Domestic" ? (
                  <>
                    <span className="border border-white bg-sky-200 text-gray-800 rounded px-2">{`#${item.locationCountry}`}</span>
                    <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">{`#${item.locationCity}`}</span>
                  </>
                ) : (
                  <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">{`#${item.locationCity}`}</span>
                )}
                <Link to={`/mateBoard/${item.id}/detail`}>{item.title}</Link>
              </td>{/* 글 제목 */}
              <td>{item.userId}</td> {/* 작성자 */}
              <td>{item.updatedAt ? item.updatedAt : item.createdAt}</td>{/* 작성일/ 수정일 */}
              <td>{item.views}</td> {/* 조회수 */}
              <td>{item.likes}</td>
            </tr>
          ))}
        </tbody>

      </table>

      <button className="mt-30" onClick={handleButtonClick}>
        to {domesticInternational}
      </button>

    </>
  );
}

export default MateBoard;
