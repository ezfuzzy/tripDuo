import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button, Card } from "react-bootstrap";
import { useParams } from "react-router";
import { Link } from "react-router-dom";

function MateBoardDetail(props) {
  const { num } = useParams(); // 게시물 번호

  const [post, setPost] = useState({});
  const [writerProfile, setWriterProfile] = useState({});
  const [isRecruited, setIsRecuruited] = useState(false);

  const buttonClasses = ` btn btn-sm ${isRecruited ? 'btn-secondary' : 'btn-success'}`;

  const handleRecruit = (e)=>{
        setIsRecuruited(!isRecruited)
  }

  useEffect(() => {
    axios
      .get(`/api/v1/posts/post/${num}`)
      .then((res) => {
        console.log(res.data);
        setPost(res.data);

        // //유저 정보 받아서 state 값으로 저장
        // axios.get(`/api/v1/users/${res.data.userId}`)
        // .then(res=>setWriterProfile(res.data))
        // .catch(error=>console.log(error))
      })
      .catch((error) => console.log(error));
  }, [num]);
  return (
    <>
      <Link to={"/mateBoard"}>Mate</Link>
      <div>
        <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">#{post.country}</span>
        <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">#{post.city}</span>
        <span className="border border-white bg-orange-200 text-gray-800 rounded px-1">#{post.tags}</span>
      </div>
      <h5>{num}번 {post.title}</h5>

      <p>안녕하세요~</p>

      <a href="/">대충 경로 공유한 url</a>
      <br />
      <br />

      <p>{post.content}</p>
      <p>{post.content}</p>
      <p>{post.content}</p>
      <p>{post.content}</p>
      
      <Card style={{ width: '18rem' }}>
      <Card.Body>
        <Card.Title>{post.country} / {post.city}</Card.Title>
        <Card.Subtitle className="mb-2 text-muted">태그 / {post.tags}</Card.Subtitle>
        <Card.Text>
            저녁 7시 30분
            tmp 역 앞 test 카페 
        </Card.Text>
        <Button className={buttonClasses} onClick={handleRecruit}>{isRecruited ? '취소하기' : '신청하기'}</Button>
      </Card.Body>
    </Card>
    
    </>
  );
}

export default MateBoardDetail;
