import axios from "axios";
import React, { useEffect, useState } from "react";
import FroalaEditor from "react-froala-wysiwyg";
import { Link, useNavigate, useParams } from "react-router-dom";

function MateBoardEditForm(props) {
  const { num } = useParams();

  const [post, setPost] = useState({});
  const [model, setModel] = useState("")

  const navigate = useNavigate()

  const handleSubmit = ()=>{

      axios.put(`/api/v1/posts/${num}`, post)
      .then(res=>{
        //데이터 전달 확인
        alert("수정에 성공하였습니다.")
        navigate(`/mateBoard/${num}/detail`) // 상세 페이지로 돌려보내기
      })
      .catch(error=>console.log(error))
    
  }

  const handleModelChange = (e)=>{
    // Froala 데이터를 model 값으로 관리해야 하기때문에 model 값을 수정 : 사용자에게 보여주기 + 데이터 관리하기
    setModel(e)
    // handleChange 처럼 Post 값으로 관리한다.
    setPost({
        ...post,
        content : e
    })
  }

  const handleChange = (e) => {
    setPost({
      ...post,
      [e.target.name]: e.target.value
    });
  };

  useEffect(() => {
    axios
      .get(`/api/v1/posts/${num}`)
      .then((res) => {
        // 데이터 전달 확인
        setPost(res.data);
        setModel(res.data.content) // 저장된 내용을 model 을 통해 Froala 에디터에 전달
      })
      .catch((error) => console.log(error));
  }, [num]);

  return (
    <>
      <Link to={`/mateBoard/${num}/detail`}>상세 페이지로</Link>
      <h3>{num} 번 게시물 수정 폼</h3>
        <div>
          <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">#{post.country}</span>
          <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">#{post.city}</span>
          <span className="border border-white bg-orange-200 text-gray-800 rounded px-1">#{post.tags}</span>
        </div>
      <form >
        <div>
          <label htmlFor="title">제목</label>
          <input onChange={handleChange} type="text" id="title" name="title" value={post.title || ""}/>
        </div>

        <div>태그 입력</div>

        <div>
          <label htmlFor="content">내용</label>
          <FroalaEditor model={model} onModelChange={handleModelChange} />
        </div>
        <button onClick={handleSubmit} type="button" className="mt-3">수정</button>
      </form>

    </>
  );
}

export default MateBoardEditForm;
