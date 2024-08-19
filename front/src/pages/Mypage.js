import { Link } from 'react-router-dom';

function Mypage() {


  return (
    <>
      <h1>My Page</h1>
      <div className="borderbox">
        <ul className="myPage">
          <li className="myPageList">
            <h3><Link to="/myplan"><strong>Travel Plan</strong>(여행 계획)</Link></h3>
            <p><Link to="/myplan">여행을 계획하거나<br/>계획한 여행들을 확인하실 수 있습니다.</Link></p>
          </li>
          <li className="myPageList">
            <h3><Link to="/myrecord"><strong>Travel Record</strong>(여행 기록)</Link></h3>
            <p><Link to="/myrecord">고객님의 여행 기록을 확인하실 수 있습니다.</Link></p>
          </li>
          <li className="myPageList">
            <h3><Link to="/wishmate"><strong>Wish Mate</strong>(관심 메이트)</Link></h3>
            <p><Link to="/wishmate">관심 메이트로 등록하신 여행 메이트를 확인하실 수 있습니다.</Link></p>
          </li>
          <li className="myPageList">
            <h3><Link to="/myplace"><strong>My Place</strong>(마이 플레이스)</Link></h3>
            <p><Link to="/myplace">관심있는 지역, 음식점들을 관리할 수 있습니다.</Link></p>
          </li>
        </ul>    
      </div>
    </>
  );
}

export default Mypage;