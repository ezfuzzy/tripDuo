
import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button, Card } from "react-bootstrap";
import { useNavigate, useParams } from "react-router";
import { Link } from "react-router-dom";


function MateBoardDetail(props) {
  const { num } = useParams(); // 게시물 번호

  const [post, setPost] = useState({});
  const [writerProfile, setWriterProfile] = useState({});
  const [isRecruited, setIsRecruited] = useState(false);

  const navigate = useNavigate()

  const buttonClasses = ` btn btn-sm ${
    isRecruited ? "btn-secondary" : "btn-success"
  }`;

  const handleRecruit = (e) => {
    setIsRecruited(!isRecruited);
  };

  useEffect(() => {
    axios
      .get(`/api/v1/posts/${num}`)
      .then((res) => {
        //글 정보 전달 확인
        setPost(res.data);
        const id = res.data.userId  

        //유저 정보 받아서 state 값으로 저장
        axios.get(`/api/v1/users/${id}`)
        .then(res=>{
            //유저 정보 전달 확인
            setWriterProfile(res.data)
      })
        .catch(error=>console.log(error))
      })
      .catch((error) => console.log(error));
  }, [num]);
  return (
    <>
      <div className="container">
        <Link to={"/mateBoard"}>Mate</Link>
        <div>
          <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">#{post.country}</span>
          <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">#{post.city}</span>
          <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full items-center">#{post.tags}</span>
        </div>
        <h5>{num}번 {post.title}</h5>

        <div className="container">
          <p><strong>{writerProfile.nickname}</strong> {writerProfile.gender} / {writerProfile.age}</p>
        </div>

        <p>안녕하세요~</p>

        <a href="/">대충 경로 공유한 url</a>
        <br />
        <br />

        <div dangerouslySetInnerHTML={{__html:post.content}}></div>

         <Card style={{ width: "18rem" }}>
          <Card.Body>
            <Card.Title>
              {post.country} / {post.city}
            </Card.Title>
            <Card.Subtitle className="mb-2 text-muted">
              태그 / {post.tags}
            </Card.Subtitle>
            <Card.Text>저녁 7시 30분 tmp 역 앞 test 카페</Card.Text>
            <Button className={buttonClasses} onClick={handleRecruit}>
              {isRecruited ? "취소하기" : "신청하기"}
            </Button>
          </Card.Body>
        </Card> 
      </div>

      <div className="container mt-3">
        <Button className="m-1" onClick={()=>navigate(`/mateBoard/${num}/edit`)}>수정</Button>
        <Button className="m-1">삭제</Button>
      </div>
    </>
  );
}

export default MateBoardDetail;
