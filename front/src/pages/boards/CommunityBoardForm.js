import React, { useEffect, useState } from "react";
import { NavLink, useNavigate, useSearchParams } from "react-router-dom";

// Require Editor CSS files.
import "froala-editor/css/froala_style.min.css";
import "froala-editor/css/froala_editor.pkgd.min.css";

import axios from "axios";
import FroalaEditor from "react-froala-wysiwyg";
import { shallowEqual, useSelector } from "react-redux";

function CommunityBoardForm() {
  //유저 정보 관리
  const userId = useSelector((state) => state.userData.id, shallowEqual);
  const nickname = useSelector((state) => state.userData.nickname, shallowEqual);
  const username = useSelector((state) => state.userData.username, shallowEqual);

  const [domesticInternational, setDomesticInternational] = useState();
  const [SearchParams, setSearchParams] = useSearchParams();
  const [post, setPost] = useState({}); // 게시물의 정보

  const navigate = useNavigate();

  //태그 관리
  const [tagInput, setTagInput] = useState("");
  const [postTags, setPostTags] = useState([]);

  //테스트 데이터
  // const koreanRegion = ["제주도", "서울", "인천", "부산", "대전", "대구", "강원", "경기", "충북", "충남", "경북", "경남", "전북", "전남" ];
  const asianCountries = ["중국", "일본", "대만", "몽골", "라오스", "말레이시아", "베트남", "태국", "필리핀"];
  const europeanCountries = ["영국", "프랑스", "독일", "스페인", "포르투갈", "스위스", "벨기에", "네덜란드"];

  //username 으로 로그인 여부 확인하여 로그인 하지 않으면 로그인 페이지로 넘기기
  useEffect(() => {
    username ?? navigate("/login");
  }, [username, navigate]);

  useEffect(
    (post) => {
      setDomesticInternational(SearchParams.get("di"));

      if (domesticInternational) {
        setPost({
          ...post,
          country: domesticInternational === "Domestic" ? "한국" : "",
          tags: [],
          viewCount: 10,
          likeCount: 10,
          rating: 80,
          status: "OPEN",
          city: "city",
        });
      }
    },
    [domesticInternational]
  );

  // handleChange 처럼 Post 값으로 관리한다.
  const handleModelChange = (e) => {
    setPost({
      ...post,
      content: e,
    });
  };

  const handleChange = (e) => {
    setPost({
      ...post,
      [e.target.name]: e.target.value,
    });
  };

  // 태그 입력 핸들러
  const handleTagInput = (e) => {
    const value = e.target.value;
    setTagInput(value);

    if (value.endsWith(" ") && value.trim() !== "") {
      const newTag = value.trim();
      if (newTag !== "#" && newTag.startsWith("#") && !postTags.includes(newTag)) {
        setPostTags([...postTags, newTag]);
        setTagInput("");
      }
    }
  };

  //태그 제거
  const removeTag = (tagToRemove) => setPostTags(postTags.filter((tag) => tag !== tagToRemove));

  const handleSubmit = async () => {
    const updatedPost = {
      ...post,
      tags: postTags,
      userId: userId,
      writer: nickname,
    };

    try {
      const response = await axios.post("/api/v1/posts/community", updatedPost);

      alert("새 글 작성에 성공하였습니다.");
      navigate(`/posts/community?di=${domesticInternational}`);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="container mx-auto p-4 max-w-[900px]">
      <NavLink
        className="px-4 py-2 text-sm font-medium rounded-md bg-gray-600 text-gray-100"
        to={{
          pathname: "/posts/community",
          search: `?di=${domesticInternational}`,
        }}>
        Community
      </NavLink>

      <h3 className="mt-4">{domesticInternational} 게시판 작성 폼</h3>

      {/* 국가/도시 태그 선택 폼 */}
      <div className="m-3" onSubmit={(e) => e.preventDefault()}>
        <div>
          {/* 국내 도시 태그 선택 */}
          {/*
          {domesticInternational === "Domestic" && (
            <select
              value={selectedRegion}
              onChange={(e) => {
                setSelectedRegion(e.target.value);
              }}
            >
              <option value="">지역</option>
              {koreanRegion.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          )} */}

          {/* 해외 */}
          {domesticInternational === "International" && (
            <select value={post.country} name="country" onChange={handleChange}>
              <option value="">국가</option>
              <optgroup label="아시아">
                {asianCountries.map((item, index) => (
                  <option key={index} value={item}>
                    {item}
                  </option>
                ))}
              </optgroup>
              <optgroup label="유럽">
                {europeanCountries.map((item, index) => (
                  <option key={index} value={item}>
                    {item}
                  </option>
                ))}
              </optgroup>
            </select>
          )}
        </div>

        <div className="flex flex-wrap gap-2 mt-2">
          {post.country ? (
            <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">#{post.country}</span>
          ) : (
            ""
          )}

          {/* 도시 태그 출력*/}
          {/* {selectedRegion ? (
            <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full items-center">
              #{selectedRegion}
            </span>
          ) : (
            ""
          )} */}
        </div>

        {/* 태그 입력 폼 */}
        <div>
          <label htmlFor="tags" className="block font-semibold">
            태그
          </label>
          <input
            id="tags"
            value={tagInput}
            onChange={handleTagInput}
            placeholder="#태그 입력 후 스페이스바"
            className="border p-2 w-1/3"
          />
          <div className="flex flex-wrap gap-2 mt-2">
            {postTags.map((tag, index) => (
              <span key={index} className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full flex items-center">
                {tag}
                <button
                  className="ml-2 p-0 h-4 w-4 text-black flex items-center justify-center"
                  onClick={() => removeTag(tag)}>
                  <span className="text-sm font-bold">&times;</span>
                </button>
              </span>
            ))}
          </div>
        </div>
        <div className="w-100">
          <label htmlFor="title" className="mt-3">
            제목
          </label>
          <input onChange={handleChange} type="text" id="title" name="title" />
        </div>

        <div>
          <label htmlFor="content">내용</label>
          <FroalaEditor
            model={post.content}
            onModelChange={handleModelChange}
            config={{
              placeholderText: "정확한 장소 혹은 주소, 시간, 인원을 필수로 입력해 주세요.",
              height: 200,
            }}></FroalaEditor>
        </div>
      </div>
      <button
        className="px-4 py-2 text-sm font-medium rounded-md bg-gray-600 text-gray-100 mb-4"
        onClick={handleSubmit}>
        제출
      </button>
    </div>
  );
}

export default CommunityBoardForm;
