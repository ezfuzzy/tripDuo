import axios from "axios";
import { useEffect, useState } from "react";
import { Pagination } from "react-bootstrap";
import { Link, useNavigate, useSearchParams } from "react-router-dom";


function Course() {
    // 글 목록 페이지 정보
    const [pageInfo, setPageInfo] = useState({
        list: []
    })
    //검색 조건과 키워드를 상태값으로 관리
    const [searchState, setSearchState] = useState({
        condition: "",
        keyword: ""
    })
    // "/course?pageNum=x"에서 pageNum을 추출하기 위한 Hook
    const [params, setParams] = useSearchParams({ pageNum: 1 })
    //페이징 숫자를 출력할 때 사용하는 배열을 상태값으로 관리
    const [pageArray, setPageArray] = useState([])

    const navigate = useNavigate()

    //글 목록 새로 읽어오는 함수
    const refresh = (pageNum) => {
        //검색 기능과 관련된 query 문자열 읽어오기
        const query = new URLSearchParams(searchState).toString()
        axios.get(`/course?pageNum=${pageNum}&${query}`)
            .then(res => {
                //서버로부터 응답된 데이터 state에 넣기
                setPageInfo(res.data)
                //페이징 처리에 관련된 배열을 state로 넣기
                const result = range(res.data.startPageNum, res.data.endPageNum)
                setPageArray(result)
            })
            .catch(error => {
                console.log(error)
            })
    }

    useEffect(() => {
        let pageNum = params.get("pageNum")
        if (pageNum == null)
            pageNum = 1
        refresh(pageNum)
    }, [params])

    //페이징 UI를 만들 때 사용할 배열 리턴하는 함수
    function range(start, end) {
        const result = []
        for (let i = start; i <= end; i++) {
            result.push(i)
        }
        return result
    }

    //페이지를 변경하는 함수
    const move = (pageNum = 1) => {
        //검색조건에 맞는 query 문자열 얻어내기
        const query = new URLSearchParams(searchState).toString()
        navigate(`/course?pageNum=${pageNum}&${query}`)
    }

    //검색 조건을 변경하거나 검색어를 입력하면 호출되는 함수
    const handleSearchChange = (e) => {
        setSearchState({
            ...searchState,
            [e.target.name]: e.target.value  //검색조건 혹은 검색 키워드가 변경된 값을 반영
        })
    }

    //Reset 버튼을 눌렀을 때
    const handleReset = () => {
        //검색조건과 검색어 초기화
        setSearchState({
            condition: "",
            keyword: ""
        })
        //1페이지 내용이 보여지게
        move(1)
    }


    return (
        <>
            <Link to="/course/new">여행코스 계획하기</Link>
            <h2>여행 코스 게시판 입니다</h2>

            <label htmlFor="search">검색조건</label> {/* htmlFor와 id의 값을 같게 */}
            <select onChange={handleSearchChange} value={searchState.condition} name="condition" id="search">
                <option value="">선택</option>
                <option value="title_content">제목+내용</option>
                <option value="title">제목</option>
                <option value="writer">작성자</option>
            </select>
            <input onChange={handleSearchChange} value={searchState.keyword} type="text" placeholder="검색어..." name="keyword" />
            <button onClick={() => move()}>검색</button>
            <button onClick={handleReset}>Reset</button>
            <table>
                <thead>
                    <tr>
                        <th>좋아요</th>
                        <th>작성자</th>
                        <th>나라</th>
                        <th>도시</th>
                        <th>제목</th>
                        <th>조회수</th>
                    </tr>
                </thead>
                <tbody>

                    {
                        pageInfo.list.map(item => (
                            <tr key={item.num}>
                                <td>{item.likes}</td>
                                {/* user_id 대신 nickname 들어가는게 좋을 것 같음 */}
                                <td>{item.user_id}</td>
                                <td>{item.location_country}</td>
                                <td>{item.location_city}</td>
                                <td>{item.title}</td>
                                <td>{item.views}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>

            <p><strong>{pageInfo.totalRow}</strong>개의 글이 있습니다</p>
            <Pagination className="mt-3">
                <Pagination.Item onClick={() => move(pageInfo.startPageNum - 1)} disabled={pageInfo.startPageNum === 1}>&laquo;</Pagination.Item>
                {
                    pageArray.map(item => ( // item 은 pageNum
                        <Pagination.Item onClick={() => move(item)} key={item} active={pageInfo.pageNum === item}>
                            {item}
                        </Pagination.Item>
                    ))
                }
                <Pagination.Item onClick={() => move(pageInfo.endPageNum + 1)} disabled={pageInfo.endPageNum >= pageInfo.totalPageCount}>&raquo;</Pagination.Item>
            </Pagination>
        </>
    );
}

export default Course;