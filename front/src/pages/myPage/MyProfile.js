import React, { createRef, useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { shallowEqual, useSelector } from "react-redux";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCheck,
  faFaceLaugh,
  faFaceLaughSquint,
  faFaceMeh,
  faFaceSmile,
  faPersonCircleXmark,
} from "@fortawesome/free-solid-svg-icons";
import "../../css/MyProfile.css";
import FollowerFolloweeModal from "../../components/FollowerFolloweeModal";

function MyProfile(props) {
  // to do : cur_location, last_login

  const { id } = useParams(); // 프로필 사용자의 id
  const userId = useSelector((state) => state.userData.id, shallowEqual); // 접속된 사용자의 id
  const nickname = useSelector((state) => state.userData.nickname, shallowEqual); // 접속된 사용자의 nickname

  // 접족된 사용자와 profile 의 사용자가 같은지
  const [isProfileOwner, setProfileOwner] = useState(false);
  // 프로필 정보
  const [profile, setProfile] = useState({});
  // 리뷰 정보
  const [reviewList, setReviewList] = useState([]);

  // BAD or GOOD or EXCELLENT
  const [experience, setExperience] = useState();

  // 팔로우 상태 관리
  const [followingStatus, setFollowingStatus] = useState(false);
  // 팔로우 알림 관리
  const toastMessageRef = useRef();

  const navigate = useNavigate();

  //프로필 토글 관리
  const dropdownMenuRef = useRef();

  // 리뷰 작성 관련
  // 리뷰 Content
  const [userReview, setUserReview] = useState("");
  //리뷰 체크박스 상태 설정
  const [selectedTags, setSelectedTags] = useState([]);
  //리뷰 이미 작성한 사용자인지 체크
  const [isReviewed, setReviewed] = useState(false);

  // 팔로잉/팔로워 모달 상태관리
  const [isModalOpen, setModalOpen] = useState(false);
  const [modalTab, setModalTab] = useState();

  // 차단 상태 관린
  const [blockStatus, setBlockStatue] = useState(false);

  // 버튼 스타일 - 신청 전/후 색상 변경
  const followButtonClasses = `px-4 py-2 text-sm font-medium rounded-md ${
    followingStatus ? "bg-gray-200 text-gray-800" : "bg-blue-500 text-white"
  }`;

  const reviewPositiveTagList = [
    { key: 1, keyword: "COMMUNICATION", text: "메시지에 항상 빠르게 답변해주어 소통이 원활했어요." },
    { key: 2, keyword: "TRUST", text: "계획된 일정을 철저히 지켜 믿음직했어요." },
    { key: 3, keyword: "ONTIME", text: "약속 시간을 잘 지켜 여유로운 여행을 즐길 수 있었어요." },
    { key: 4, keyword: "MANNER", text: "친절하고 배려심 넘치는 태도로 편안하게 여행했어요." },
    { key: 5, keyword: "FLEXIBLE", text: "변경된 계획에도 유연하게 대처하여 즐거운 여행이 되었어요." },
    { key: 6, keyword: "ACTIVE", text: "적극적인 태도로 다양한 경험을 할 수 있도록 이끌어주었어요." },
    { key: 7, keyword: "FRIENDLY", text: "함께 시간을 보내는 내내 즐거웠고, 좋은 친구를 얻은 기분이었어요." },
    { key: 8, keyword: "PAY", text: "비용 분담에 있어 투명하고 공정하게 처리하여 신뢰가 갔어요." },
    { key: 9, keyword: "CLEAN", text: "깔끔한 여행 스타일로 쾌적한 환경을 유지해주었어요." },
  ];

  const reviewNegativeTagList = [
    { key: 1, keyword: "COMMUNICATION", text: "메시지 답변이 느려 소통에 어려움을 느꼈어요." },
    { key: 2, keyword: "TRUST", text: "계획된 일정을 자주 변경하여 불안했어요." },
    { key: 3, keyword: "ONTIME", text: "약속 시간에 자주 늦어 불편했어요." },
    { key: 4, keyword: "MANNER", text: "무례한 언행으로 불쾌한 경험을 했어요." },
    { key: 5, keyword: "FLEXIBLE", text: "변경된 상황에 대한 대처가 미흡하여 아쉬웠어요." },
    { key: 6, keyword: "ACTIVE", text: "소극적인 태도로 여행에 대한 적극적인 참여가 부족했어요." },
    { key: 7, keyword: "FRIENDLY", text: "함께 시간을 보내는 것이 불편했어요." },
    { key: 8, keyword: "PAY", text: "비용 분담에 있어 불투명하고 불공정하여 신뢰가 떨어졌어요." },
    { key: 9, keyword: "CLEAN", text: "개인 위생 관리가 부족하여 함께 여행하는 것이 불편했어요." },
  ];

  //새로 등록한 댓글을 추가할 인덱스
  let commentIndex = 0;
  // 리뷰 최대 글자수
  const maxLength = 3000;

  useEffect(() => {
    axios
      .get(`/api/v1/users/${id}`)
      .then((res) => {
        console.log(res.data);

        // is Blocked?
        if (res.data.theirFollowType === "BLOCK") {
          alert("해당 사용자가 당신을 차단하여 더 이상 프로필을 볼 수 없습니다.");
          navigate("/");
        }

        // BLOCK or FOLLOW
        if (res.data.myFollowType === "BLOCK") {
          setBlockStatue(true);
        } else if (res.data.myFollowType === "FOLLOW") {
          setFollowingStatus(true);
        }

        //불러온 사용자의 정보 저장
        setProfile(res.data.userProfileInfo);
        //불러온 사용자에게 달린 리뷰 저장
        setReviewList(res.data.userReviewList);

        console.log(res.data);

        // 접속된 사용자와 프로필 사용자의 id 가 같으면 Owner = true
        if (userId === res.data.userProfileInfo.userId) {
          setProfileOwner(true);
        }

        const findReviewerId = res.data.userReviewList.find((item) => item.reviewerId === userId);
        // undefined = false / 이 외에 = true
        setReviewed(!!findReviewerId);
      })
      .catch((error) => console.log(error));
  }, [id, userId]);

  //--------------------------이벤트 관리 부--------------------------------------

  // 프로필 수정 클릭
  const handleClick = () => {
    navigate(`/users/${id}/profile/edit`);
  };

  // 팔로잉/팔로워 모달 open
  const handleOpenModal = (tab) => {
    setModalTab(tab);
    setModalOpen(true);
  };
  // 팔로잉/팔로워 모달 close
  const handleCloseModal = () => {
    setModalOpen(false);
  };

  // 토스트 메세지
  const toastOn = () => {
    toastMessageRef.current.classList.add("active");
    setTimeout(() => {
      toastMessageRef.current.classList.remove("active");
    }, 1500);
  };

  // 차단 버튼 클릭 이벤트
  const handleBlock = () => {
    if (!blockStatus) {
      // 차단 중이지 않은 경우 (blockStatus = false)
      if (window.confirm(`${profile.nickname}님을 차단하시겠습니까?`)) {
        axios
          .post(`/api/v1/users/${id}/block/${userId}`)
          .then((res) => {
            console.log(res.data);
            setBlockStatue(true);
            dropdownMenuRef.current.classList.toggle("hidden"); // dropdown 메뉴 숨김
          })
          .catch((error) => console.log(error));
      }
    } else {
      // 차단 중인 경우 (blockStatus = true)
      if (window.confirm(`차단을 해제하시겠습니까?`)) {
        axios
          .post(`/api/v1/users/${id}/block/${userId}`)
          .then((res) => {
            console.log(res.data);
            setBlockStatue(false);
            dropdownMenuRef.current.classList.toggle("hidden"); // dropdown 메뉴 숨김
          })
          .catch((error) => console.log(error));
      }
    }
  };

  // 팔로우 버튼 클릭 이벤트
  // to do : 팔로우 상태 알림 ( ex) android splash )
  const handleClickFollow = () => {
    if (!followingStatus) {
      //팔로우 중이지 않은경우 (followingStatue = false)
      toastOn();
      axios
        .post(`/api/v1/users/${id}/follow/${userId}`)
        .then((res) => {
          console.log(res.data);
          setFollowingStatus(true);
        })
        .catch((error) => console.log(error));
    } else {
      // 팔로우 중인 경우 (followingStatus = true)
      if (window.confirm("팔로우를 취소 하시겠습니까?")) {
        axios
          .delete(`/api/v1/users/${id}/follow/${userId}`)
          .then((res) => {
            console.log(res.data);
            setFollowingStatus(false);
          })
          .catch((error) => console.log(error));
      }
    }
  };

  //프로필 토글 버튼 클릭
  const handleClickToggle = () => {
    dropdownMenuRef.current.classList.toggle("hidden");
  };

  // 입력된 리뷰 내용 상태값으로 저장
  const handleInputReview = (e) => {
    setUserReview(e.target.value);
  };

  // 리뷰 드롭다운 태그 메뉴 관련
  const handleReviewDropDown = (e) => {
    if (e === "BAD") {
      setExperience("BAD");
      setSelectedTags([]);
    } else if (e === "GOOD") {
      setExperience("GOOD");
      setSelectedTags([]);
    } else if (e === "EXCELLENT") {
      setExperience("EXCELLENT");
      setSelectedTags([]);
    }
  };

  // 체크박스 데이터 핸들링
  const handleCheckboxChange = (e) => {
    const checked = e.target.checked;
    const value = e.target.value;

    if (checked) {
      setSelectedTags([...selectedTags, value]);
    } else {
      setSelectedTags(selectedTags.filter((tag) => tag !== value));
    }
  };

  //프로필 링크 복사
  const handleCopy = () => {
    const tmpText = `localhost:3000/users/${id}/profile`;
    navigator.clipboard
      .writeText(tmpText)
      .then(() => {
        alert("클립보드에 복사되었습니다.");
        dropdownMenuRef.current.classList.toggle("hidden"); // dropdown 메뉴 숨김
      })
      .catch((error) => console.log(error));
  };

  // 리뷰 작성
  const handleReviewSubmit = () => {
    if (selectedTags.length === 0) {
      alert("1개 이상의 태그를 선택해주세요");
      return;
    }
    const reviewData = {
      content: userReview,
      experience: experience,
      tags: selectedTags,
    };

    console.log(reviewData);

    axios
      .post(`/api/v1/users/${id}/review/${userId}`, reviewData)
      .then((res) => {
        console.log(res.data);
        const newReview = res.data;
        newReview.ref = createRef();
        setReviewList([...reviewList, newReview]);
        // textArea 초기화
        setUserReview("");
        setSelectedTags([]);
      })
      .catch((error) => console.log(error));
  };

  // 리뷰 신고 처리 함수
  const handleReportReview = (commentId) => {
    // 신고 기능 구현
    alert(`댓글 ID ${commentId}가 신고되었습니다.`);
    // 추가로 서버에 신고 요청을 보내는 로직을 여기에 추가
  };

  // 리뷰 수정 함수
  const handleUpdateRiview = () => {

  };

  // 리뷰 삭제 함수
  const handleDeleteReview = () => {
    if(window.confirm("리뷰를 삭제하시겠습니까?")){ 
      axios
      .delete(`/api/v1/users/${id}/review/${userId}`)
      .then((res) => {
        console.log(res.data);
        // 사용자당 한개의 리뷰를 작성하기때문에 삭제가 가능한 reviewerId = userId 이다
        setReviewList(reviewList.filter((item) => item.reviewerId !== userId));
      })
      .catch((error) => console.log(error));
    }
  };

  return (
    <div className="container mx-auto p-4 max-w-[900px]">
      {isProfileOwner && (
        <div>
          <button
            type="button"
            className="mb-20 px-4 py-2 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600"
            onClick={() => {
              navigate(`/users/${id}`);
            }}>
            마이 페이지
          </button>
        </div>
      )}
      {/* 전체 div */}
      <div className="flex-col">
        {blockStatus && (
          <p className="ml-10 mb-0 text-sm text-red-600">
            <FontAwesomeIcon icon={faPersonCircleXmark} />
            &nbsp;차단된 사용자 입니다.
          </p>
        )}

        {/* 프로필 부분 전체 */}
        <div className="relative flex-col ">
          {/* 세로로 가운데, 아이템들 수평 간격 6px 마진 3  */}
          <div className="flex items-center gap-x-6 m-3 justify-center">
            {/* 프로필 이미지 핸들링 */}
            {profile.profilePicture ? (
              <img src={profile.profilePicture} width={100} height={100} className="rounded-full" alt="" />
            ) : (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="100"
                height="100"
                fill="currentColor"
                className="bi bi-person-circle"
                viewBox="0 0 16 16">
                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                <path
                  fillRule="evenodd"
                  d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"
                />
              </svg>
            )}

            <div>
              <h3 className="text-base font-semibold leading-7 tracking-tight text-gray-900">{profile.nickname}</h3>
              <p className="text-sm font-semibold leading-6 text-indigo-600">
                {profile.gender} / {profile.age}
              </p>
            </div>

            {/* 프로필 사용자 / 방문자 구분 */}
            {isProfileOwner ? (
              <div>
                <button
                  className="px-4 py-2 text-sm font-medium rounded-md bg-gray-600 text-gray-100"
                  onClick={handleClick}>
                  프로필 수정하기
                </button>
              </div>
            ) : (
              // 팔로우, 평가
              <div className="flex ">
                <button className={followButtonClasses} onClick={handleClickFollow}>
                  {followingStatus && <FontAwesomeIcon icon={faCheck} />}
                  &nbsp;팔로우
                </button>
                <div id="toast_message" ref={toastMessageRef}>
                  {profile.nickname}님을 팔로우하기 시작합니다
                </div>
              </div>
            )}
            {/* Toggle 버튼 (신고/차단) 프로필 주인이 아닐시에만 랜더링*/}
            {!isProfileOwner && (
              <div className="flex items-center dropdown-wrapper">
                <button
                  onClick={handleClickToggle}
                  data-dropdown-toggle="dropdownDots"
                  className="dropdown-button inline-flex items-center p-2 text-sm font-medium text-center text-gray-600 bg-white rounded-lg hover:bg-gray-100 focus:ring-1focus:outline-none focus:ring-gray-50"
                  type="button">
                  <svg
                    className="w-5 h-5"
                    aria-hidden="true"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="currentColor"
                    viewBox="0 0 4 15">
                    <path d="M3.5 1.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0Zm0 6.041a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0Zm0 5.959a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0Z" />
                  </svg>
                </button>

                <div
                  id="dropdownDots"
                  ref={dropdownMenuRef}
                  className="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-40 dropdown-inner">
                  <div className="py-2 text-sm text-gray-700" aria-labelledby="dropdownMenuIconButton">
                    <p onClick={handleCopy} className="block px-4 py-2 hover:bg-gray-100 cursor-pointer">
                      프로필 링크
                    </p>
                    <p onClick={handleBlock} className="block px-4 py-2 hover:bg-gray-100 cursor-pointer">
                      {!blockStatus ? (
                        <>차단</>
                      ) : (
                        <>
                          <FontAwesomeIcon icon={faCheck} />
                          차단됨
                        </>
                      )}
                    </p>
                    <p className="block px-4 py-2 hover:bg-gray-100 cursor-pointer">신고</p>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* 팔로우/팔로우 카운트 뷰 + 팝업 페이지 */}
          <div className="flex space-x-4 justify-center">
            <button
              onClick={() => {
                handleOpenModal("followee");
              }}>
              <strong>{profile.followeeCount}</strong>Following
            </button>
            <button
              onClick={() => {
                handleOpenModal("follower");
              }}>
              <strong>{profile.followerCount}</strong>Followers
            </button>
          </div>
        </div>

        {/* MODAL */}
        {isModalOpen && <FollowerFolloweeModal id={id} ff={modalTab} onClose={handleCloseModal} />}

        {/* sns 아이콘 */}
        <div className="mt-3 flex justify-center">
          <a className="h-8 w-8 rounded-full outline-none focus:outline-none" type="button" href={profile.socialLinks}>
            <svg
              className="fill-current transition duration-700 ease-in-out text-gray-700 hover:text-pink-600"
              role="img"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg">
              <title>Instagram</title>
              <path d="M12 0C8.74 0 8.333.015 7.053.072 5.775.132 4.905.333 4.14.63c-.789.306-1.459.717-2.126 1.384S.935 3.35.63 4.14C.333 4.905.131 5.775.072 7.053.012 8.333 0 8.74 0 12s.015 3.667.072 4.947c.06 1.277.261 2.148.558 2.913.306.788.717 1.459 1.384 2.126.667.666 1.336 1.079 2.126 1.384.766.296 1.636.499 2.913.558C8.333 23.988 8.74 24 12 24s3.667-.015 4.947-.072c1.277-.06 2.148-.262 2.913-.558.788-.306 1.459-.718 2.126-1.384.666-.667 1.079-1.335 1.384-2.126.296-.765.499-1.636.558-2.913.06-1.28.072-1.687.072-4.947s-.015-3.667-.072-4.947c-.06-1.277-.262-2.149-.558-2.913-.306-.789-.718-1.459-1.384-2.126C21.319 1.347 20.651.935 19.86.63c-.765-.297-1.636-.499-2.913-.558C15.667.012 15.26 0 12 0zm0 2.16c3.203 0 3.585.016 4.85.071 1.17.055 1.805.249 2.227.415.562.217.96.477 1.382.896.419.42.679.819.896 1.381.164.422.36 1.057.413 2.227.057 1.266.07 1.646.07 4.85s-.015 3.585-.074 4.85c-.061 1.17-.256 1.805-.421 2.227-.224.562-.479.96-.899 1.382-.419.419-.824.679-1.38.896-.42.164-1.065.36-2.235.413-1.274.057-1.649.07-4.859.07-3.211 0-3.586-.015-4.859-.074-1.171-.061-1.816-.256-2.236-.421-.569-.224-.96-.479-1.379-.899-.421-.419-.69-.824-.9-1.38-.165-.42-.359-1.065-.42-2.235-.045-1.26-.061-1.649-.061-4.844 0-3.196.016-3.586.061-4.861.061-1.17.255-1.814.42-2.234.21-.57.479-.96.9-1.381.419-.419.81-.689 1.379-.898.42-.166 1.051-.361 2.221-.421 1.275-.045 1.65-.06 4.859-.06l.045.03zm0 3.678c-3.405 0-6.162 2.76-6.162 6.162 0 3.405 2.76 6.162 6.162 6.162 3.405 0 6.162-2.76 6.162-6.162 0-3.405-2.76-6.162-6.162-6.162zM12 16c-2.21 0-4-1.79-4-4s1.79-4 4-4 4 1.79 4 4-1.79 4-4 4zm7.846-10.405c0 .795-.646 1.44-1.44 1.44-.795 0-1.44-.646-1.44-1.44 0-.794.646-1.439 1.44-1.439.793-.001 1.44.645 1.44 1.439z" />
            </svg>
          </a>
          <span> {profile.socialLinks} </span>

          <a
            className="ml-5 h-8 w-8 rounded-full outline-none focus:outline-none"
            type="button"
            href={profile.socialLinks}>
            <svg
              className="fill-current transition duration-700 ease-in-out text-gray-700 hover:text-green-600"
              role="img"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg">
              <title>Github</title>
              <path d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113.82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998.108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0-6.627-5.373-12-12-12" />
            </svg>
          </a>
          <span> {profile.socialLinks} </span>
        </div>

        {/* profile message */}
        <div className="my-3">
          <div id="profileMessage" className="border-2 border-gray-400 rounded-md p-2 min-h-[100px] overflow-y-auto">
            {profile.profileMessage}
          </div>
        </div>

        {/* -------------------------------------------------------------------------------------- */}
        {/* 리뷰 작성 */}
        {/* 두 조건 모두 거짓(리뷰 하지않음, 프로필 사용자가 아님)이어야 true 를 반환 => 리뷰 작성 랜더링 */}
        {!isReviewed && !isProfileOwner && (
          <form className="border-3 rounded-lg p-3 mb-6 bg-gray-100" onSubmit={handleReviewSubmit}>
            <div className="mt-10 text-center space-x-20">
              <FontAwesomeIcon
                className={`w-12 h-12 fill-current transition duration-700 ease-in-out ${
                  experience === "BAD" ? "text-orange-600" : "text-gray-700"
                } hover:text-orange-600`}
                onClick={() => handleReviewDropDown("BAD")}
                icon={faFaceMeh}
              />
              <FontAwesomeIcon
                className={`w-12 h-12 fill-current transition duration-700 ease-in-out ${
                  experience === "GOOD" ? "text-green-600" : "text-gray-700"
                } hover:text-green-600`}
                onClick={() => handleReviewDropDown("GOOD")}
                icon={faFaceSmile}
              />
              <FontAwesomeIcon
                className={`w-12 h-12 fill-current transition duration-700 ease-in-out ${
                  experience === "EXCELLENT" ? "text-cyan-600" : "text-gray-700"
                } hover:text-cyan-600`}
                onClick={() => handleReviewDropDown("EXCELLENT")}
                icon={faFaceLaughSquint}
              />

              {/* to do : index 값 설정 */}
              <ul className="pb-10">
                {experience === "BAD" ? (
                  <p className="py-5 text-orange-600 font-bold">별로에요 :(</p>
                ) : experience === "GOOD" ? (
                  <p className="py-5 text-green-600 font-bold">좋아요 :)</p>
                ) : experience === "EXCELLENT" ? (
                  <p className="py-5 text-cyan-600 font-bold">최고에요 :D</p>
                ) : (
                  ""
                )}
                {experience === "BAD"
                  ? reviewNegativeTagList.map((item) => (
                      <li key={item.key}>
                        <input
                          id={item.keyword}
                          type="checkbox"
                          value={item.keyword}
                          checked={selectedTags.includes(item.keyword)} // selectedTags 에 포함된 요소만 체크
                          onChange={handleCheckboxChange}
                          className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                        />
                        <label htmlFor="default-checkbox" className="ms-2 text-sm font-medium text-gray-900 ">
                          {item.text}
                        </label>
                      </li>
                    ))
                  : experience === "GOOD" || experience === "EXCELLENT"
                  ? reviewPositiveTagList.map((item) => (
                      <li key={item.key}>
                        <input
                          id={item.keyword}
                          type="checkbox"
                          value={item.keyword}
                          checked={selectedTags.includes(item.keyword)} // selectedTags 에 포함된 요소만 체크
                          onChange={handleCheckboxChange}
                          className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
                        />
                        <label htmlFor="default-checkbox" className="ms-2 text-sm font-medium text-gray-900 ">
                          {item.text}
                        </label>
                      </li>
                    ))
                  : ""}
              </ul>
            </div>

            <div className="font-bold">{nickname}</div>
            <div className="relative">
              <textarea
                className="border border-white rounded w-full h-24 p-2"
                placeholder={userId ? "리뷰를 남겨보세요" : "리뷰를 작성하시려면 로그인이 필요합니다."}
                value={userReview}
                maxLength={maxLength}
                onChange={handleInputReview}
              />
              <div className="absolute top-2 right-2 text-gray-500 text-sm">
                {userReview.length}/{maxLength}
              </div>
              <div className="flex items-center justify-between">
                <button
                  type="button"
                  onClick={handleReviewSubmit}
                  className="p-4 text-gray-500 hover:text-gray-700 font-semibold">
                  등록
                </button>
              </div>
            </div>
          </form>
        )}

        {/* REVIEW */}
        <div>
          <ul className="space-y-4">
            {reviewList.map((item) => (
              <li key={item.id}>
                <div className="flex-1">
                  {/* 유저 정보 */}
                  <div className="flex items-end">
                    {/* 프로필 사진 또는 기본 아이콘 */}
                    {item.reviewerProfilePicture === null ? (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="w-10 h-10 text-gray-400 mx-2"
                        viewBox="0 0 16 16"
                        fill="currentColor">
                        <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                        <path
                          fillRule="evenodd"
                          d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.206 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"
                        />
                      </svg>
                    ) : (
                      <img src={item.reviewerProfilePicture} className="w-10 h-10 rounded-full" alt="프로필" />
                    )}
                    <span className="font-bold text-gray-900">{item.reviewerNickname}</span>

                    {item.experience === "BAD" ? (
                      <FontAwesomeIcon className="w-7 h-7 text-orange-600 ml-10" icon={faFaceMeh} />
                    ) : item.experience === "GOOD" ? (
                      <FontAwesomeIcon className="w-7 h-7 text-green-600 ml-10" icon={faFaceLaugh} />
                    ) : item.experience === "EXCELLENT" ? (
                      <FontAwesomeIcon className="w-7 h-7 text-cyan-600 ml-10" icon={faFaceLaughSquint} />
                    ) : (
                      ""
                    )}

                    {/* 리뷰 수정/삭제 신고 */}
                    {/* 리뷰어와 현재 접속자가 같을때만 수정/삭제 랜더링 */}
                    {item.reviewerId === userId ? (
                      <p className="text-xs text-gray-500 ml-auto mr-4 space-x-3">
                        <span onClick={handleUpdateRiview} className="cursor-pointer">
                          수정
                        </span>
                        <span onClick={handleDeleteReview} className="cursor-pointer">
                          삭제
                        </span>
                      </p>
                    ) : (
                      <p
                        onClick={() => handleReportReview(item.id)}
                        className="text-xs text-gray-500 ml-auto mr-4 cursor-pointer">
                        신고
                      </p>
                    )}
                  </div>

                  {/* 리뷰 내용 */}
                  <p className="whitespace-pre-wrap text-gray-700 mt-1">{item.content}</p>

                  {/* 작성 시간 */}
                  <div className="mt-2 text-sm text-gray-500">
                    <small className="ml-4 text-gray-400">{item.createdAt}</small>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default MyProfile;
