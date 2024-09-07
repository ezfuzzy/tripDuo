import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

// Require Editor CSS files.
import 'froala-editor/css/froala_style.min.css';
import 'froala-editor/css/froala_editor.pkgd.min.css';
            
import axios from "axios";
import FroalaEditor from "react-froala-wysiwyg";

function MateBoardForm(props) {
  const [selectedBorder, setSelectedBorder] = useState("domestic");
  const [selectedContinent, setSelectedContinent] = useState();
  const [selectedCountry, setSelectedCountry] = useState(); // 초기값 설정
  const [selectedRegion, setSelectedRegion] = useState();

  const countries = {
    asia: [
      "중국",
      "일본",
      "대만",
      "몽골",
      "라오스",
      "말레이시아",
      "베트남",
      "태국",
      "필리핀",
    ],
    europe: [
      "영국",
      "프랑스",
      "독일",
      "스페인",
      "포르투갈",
      "스위스",
      "벨기에",
      "네덜란드"
    ],
  };


  return (
    <>
        <Link to={"/mateBoard"}>Mate</Link>
      <h3>메이트 게시판 작성 폼</h3>

      <form action="">
        <div>
          <select
            value={selectedBorder}
            onChange={(e) => {
              setSelectedBorder(e.target.value);
            }}
            name="border"
            id="border"
          >
            <option value="domestic">국내</option>
            <option value="international">해외</option>
          </select>
            {/* 해외 */}
          {selectedBorder === "international" && (
            <select
              value={selectedContinent}
              onChange={(e) => setSelectedContinent(e.target.value)}
            >
              <option value="">대륙</option>
              <option value="asia">아시아</option>
              <option value="europe">유럽</option>
            </select>
          )}

          {selectedBorder === "international" && selectedContinent && (
            <select
              value={selectedCountry}
              onChange={(e) => {
                setSelectedCountry(e.target.value);
              }}
            >
                <option value="">국가</option>
              {countries[selectedContinent].map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          )}

          
        </div>

        <div>
            { selectedCountry ? <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">#{selectedCountry}</span> : "" }
            { selectedRegion ? <span className="border border-white bg-sky-200 text-gray-800 rounded px-1">#{selectedRegion}</span> : "" }
        </div>
        <div>
          <label htmlFor="title">제목</label>
          <input type="text" id="title" name="title"/>
        </div>

        <div>태그 입력</div>

        <div>
          <label htmlFor="content">내용</label>
        <FroalaEditor ></FroalaEditor>
        </div>

      </form>
    </>
  );
}

export default MateBoardForm;
