import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { Link, NavLink } from "react-router-dom";

function MateBoard() {

  //배열 안에서 객체로 관리
  const [pageData, setPageData] = useState([
    {
      id: 1,
      userId: "Sample Id",
      type: "Sample Type",
      title: "SAMPLE TITLE",
      Country: "",
      City: "강릉",
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
      Country: "베트남",
      City: "다낭",
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
    useState("Domestic");
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
    if (domesticInternational === "Domestic") {
      // 국내 상태
      //국내 메이트 게시판 요청
      setWhereAreYou("국내 여행 메이트 페이지");
      setPageTurn("to International");
    } else if (domesticInternational === "International") {
      //해외 메이트 게시판 요청
      setWhereAreYou("해외 여행 메이트 페이지");
      setPageTurn("to Domestic");
    }
  }, [domesticInternational]);

  
  const [post, setPost] = useState({})

  useEffect(()=>{
    axios.get("/api/v1/posts/mate")
    .then(res=>{
        console.log(res.data);
        setPost(res.data)
    })
    .catch(error=>console.log(error)
    )
  },[])

  return (
    <>
      <h3>{whereAreYou}</h3><NavLink to={"/mateBoard/new"}>새글 작성</NavLink>
      <button className="border border-1 bg-light-green-200" onClick={handleButtonClick}>{pageTurn}</button>

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
                    <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">{`#${item.Country}`}</span>
                    <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">{`#${item.City}`}</span>
                  </>
                ) : (
                  <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">{`#${item.City}`}</span>
                )}
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
