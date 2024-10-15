import React, { createRef, useEffect, useRef, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import axios from "axios"
import { shallowEqual, useSelector } from "react-redux"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import {
  faCheck,
  faFaceLaughSquint,
  faFaceMeh,
  faFaceSmile,
  faPersonCircleXmark,
  faUser,
} from "@fortawesome/free-solid-svg-icons"
import "../../css/MyProfile.css"
import FollowModal from "../../components/FollowModal"
import LoadingAnimation from "../../components/LoadingAnimation"
import useWebSocket from "../../components/useWebSocket"
import { ratingConfig, reviewNegativeTagList, reviewPositiveTagList } from "../../constants/mapping"

// ###

// 리뷰 최대 글자수
const maxLength = 3000

//--------------------------------------------------------------------------------------------------------------rating 관리 부

function MyProfile(props) {
  // to do : cur_location, last_login
  //로딩 상태 추가
  const [loading, setLoading] = useState(false)

  const { id } = useParams() // 프로필 사용자의 id
  const userId = useSelector((state) => state.userData.id, shallowEqual) // 접속된 사용자의 id
  const nickname = useSelector((state) => state.userData.nickname, shallowEqual) // 접속된 사용자의 nickname

  // 접족된 사용자와 profile 의 사용자가 같은지
  const [isProfileOwner, setProfileOwner] = useState(false)
  // 프로필 정보
  const [profile, setProfile] = useState({})
  // 리뷰 정보
  const [reviewList, setReviewList] = useState([])
  // 필터링된 프로필 메세지
  const [filteredTextValue, setFilteredTextValue] = useState()

  // BAD or GOOD or EXCELLENT
  const [experience, setExperience] = useState()

  // 팔로우 상태 관리
  const [followingStatus, setFollowingStatus] = useState(false)
  // 팔로우 알림 관리
  const toastMessageRef = useRef()

  const navigate = useNavigate()

  //프로필 토글 관리
  const dropdownMenuRef = useRef()

  // 리뷰 작성 관련
  // 새로 작성하는 리뷰 Content // to do : 변수명 수정하기
  const [userReview, setUserReview] = useState("")
  // 작성된 리뷰들의 수정 content
  const [editTexts, setEditTexts] = useState({})
  //리뷰 체크박스 상태 설정
  const [selectedTags, setSelectedTags] = useState([])
  //리뷰 이미 작성한 사용자인지 체크
  const [isReviewed, setReviewed] = useState(false)
  //리뷰를 작성할 조건이 되는 사용자 (나를 팔로우한 사용자인지)
  const [isFollowing, setFollowing] = useState(false)

  // 팔로잉/팔로워 모달 상태관리
  const [isModalOpen, setModalOpen] = useState(false)
  const [modalTab, setModalTab] = useState()

  // 차단 상태 관린
  const [blockStatus, setBlockStatue] = useState(false)

  // 버튼 스타일 - 신청 전/후 색상 변경
  const followButtonClasses = `px-4 py-2 text-sm font-medium rounded-md ${
    followingStatus ? "bg-gray-200 text-gray-800" : "bg-blue-500 text-white"
  }`

  // rating 값에 따른 아이콘과 색상 계산 //
  const getRatingDetails = (ratings) => {
    return (
      ratingConfig.find((config) => ratings >= config.min && ratings <= config.max) || { icon: faUser, color: "black" }
    ) // 기본값
  }

  const { icon: ratingIcon, color: ratingColor } = getRatingDetails(profile.ratings || 0)
  //---------------------------------------------------------------------------------------------------------------rating 관리부
  useEffect(() => {
    // 로딩 애니메이션을 0.5초 동안만 표시
    setLoading(true)
    setTimeout(() => {
      setLoading(false)
    }, 700)

    axios
      .get(`/api/v1/users/${id}`)
      .then((res) => {
        console.log(res.data)

        if (res.data.message) {
          alert(res.data.message)
          navigate("/")
        }

        // is Blocked?
        if (res.data.theirFollowType === "BLOCK") {
          alert("해당 사용자가 당신을 차단하여 더 이상 프로필을 볼 수 없습니다.")
          navigate("/")
        } else if (res.data.theirFollowType === "FOLLOW") {
          setFollowing(true)
        }

        // BLOCK or FOLLOW
        if (res.data.myFollowType === "BLOCK") {
          setBlockStatue(true)
        } else if (res.data.myFollowType === "FOLLOW") {
          setFollowingStatus(true)
        }

        // 불러온 사용자의 정보 저장
        const userProfileInfo = res.data.userProfileInfo
        const socialLinksArray = userProfileInfo.socialLinks || ["tictok+", "instagram+"]

        setProfile({
          ...userProfileInfo,
          socialLinks: socialLinksArray,
        })

        if (res.data.userProfileInfo.profileMessage !== null && res.data.userProfileInfo.profileMessage !== "") {
          setFilteredTextValue(res.data.userProfileInfo.profileMessage.replace(/\n/g, "<br>"))
        }

        //리뷰 목록이 존재하는지 확인하고, 존재한다면 ref 라는 방 추가
        const list = Array.isArray(res.data.userReviewList)
          ? res.data.userReviewList.map((item) => {
              item.ref = createRef()
              return item
            })
          : []

        setReviewList(list)

        // 접속된 사용자와 프로필 사용자의 id 가 같으면 Owner = true
        if (userId === res.data.userProfileInfo.userId) {
          setProfileOwner(true)
        } else {
          setProfileOwner(false)
        }

        const findReviewerId = res.data.userReviewList.find((item) => item.reviewerId === userId)
        // undefined = false / 이 외에 = true
        setReviewed(!!findReviewerId)
      })
      .catch((error) => console.log(error))
  }, [id, userId])

  //--------------------------이벤트 관리 부--------------------------------------

  // 프로필 수정 클릭
  const handleClick = () => {
    navigate(`/users/${id}/profile/edit`)
  }

  // 팔로잉/팔로워 모달 open
  const handleOpenModal = (tab) => {
    setModalTab(tab)
    setModalOpen(true)
  }
  // 팔로잉/팔로워 모달 close
  const handleCloseModal = () => {
    setModalOpen(false)
  }

  // 토스트 메세지
  const toastOn = () => {
    toastMessageRef.current.classList.add("active")
    setTimeout(() => {
      toastMessageRef.current.classList.remove("active")
    }, 1500)
  }

  // 차단 버튼 클릭 이벤트
  const handleBlock = () => {
    if (!blockStatus) {
      // 차단 중이지 않은 경우 (blockStatus = false)
      if (window.confirm(`${profile.nickname}님을 차단하시겠습니까?`)) {
        axios
          .post(`/api/v1/users/${id}/block/${userId}`)
          .then((res) => {
            console.log(res.data)
            setBlockStatue(true)
            //뷰 페이지 설정
            setFollowingStatus(false)
            setProfile({
              ...profile,
              followerCount: profile.followerCount - 1,
            })
            dropdownMenuRef.current.classList.toggle("hidden") // dropdown 메뉴 숨김
          })
          .catch((error) => console.log(error))
      }
    } else {
      // 차단 중인 경우 (blockStatus = true)
      if (window.confirm(`차단을 해제하시겠습니까?`)) {
        axios
          .delete(`/api/v1/users/${id}/block/${userId}`)
          .then((res) => {
            console.log(res.data)
            setBlockStatue(false)

            dropdownMenuRef.current.classList.toggle("hidden") // dropdown 메뉴 숨김
          })
          .catch((error) => console.log(error))
      }
    }
  }

  // 팔로우 버튼 클릭 이벤트
  // to do : 팔로우 상태 알림 ( ex) android splash )
  const handleClickFollow = () => {
    if (!followingStatus) {
      //팔로우 중이지 않은경우 (followingStatue = false)
      toastOn()
      axios
        .post(`/api/v1/users/${id}/follow/${userId}`)
        .then((res) => {
          console.log(res.data)
          //뷰 페이지 설정
          setFollowingStatus(true)
          setProfile({
            ...profile,
            followerCount: profile.followerCount + 1,
          })
        })
        .catch((error) => console.log(error))
    } else {
      // 팔로우 중인 경우 (followingStatus = true)
      if (window.confirm("팔로우를 취소 하시겠습니까?")) {
        axios
          .delete(`/api/v1/users/${id}/follow/${userId}`)
          .then((res) => {
            console.log(res.data)
            //뷰 페이지 설정
            setFollowingStatus(false)
            setProfile({
              ...profile,
              followerCount: profile.followerCount - 1,
            })
          })
          .catch((error) => console.log(error))
      }
    }
  }

  //프로필 토글 버튼 클릭
  const handleClickToggle = () => {
    dropdownMenuRef.current.classList.toggle("hidden")
  }

  // 입력된 리뷰 내용 상태값으로 저장
  const handleInputReview = (e) => {
    setUserReview(e.target.value)
  }

  // 리뷰 드롭다운 태그 메뉴 관련
  const handleReviewDropDown = (e) => {
    if (e === "BAD") {
      setExperience("BAD")
      setSelectedTags([])
    } else if (e === "GOOD") {
      setExperience("GOOD")
      setSelectedTags([])
    } else if (e === "EXCELLENT") {
      setExperience("EXCELLENT")
      setSelectedTags([])
    }
  }

  // 체크박스 데이터 핸들링
  const handleCheckboxChange = (e) => {
    const checked = e.target.checked
    const value = e.target.value

    if (checked) {
      setSelectedTags([...selectedTags, value])
    } else {
      setSelectedTags(selectedTags.filter((tag) => tag !== value))
    }
  }

  //프로필 링크 복사
  const handleCopy = () => {
    const tmpText = `localhost:3000/users/${id}/profile`
    navigator.clipboard
      .writeText(tmpText)
      .then(() => {
        alert("클립보드에 복사되었습니다.")
        dropdownMenuRef.current.classList.toggle("hidden") // dropdown 메뉴 숨김
      })
      .catch((error) => console.log(error))
  }

  // 프로필 사용자 신고
  const handleReportUser = () => {
    const data = { content: "신고 테스트" }
    if (window.confirm("사용자를 신고하시겠습니까")) {
      axios
        .post(`/api/v1/reports/${id}/user/${userId}`, data)
        .then((res) => {
          console.log(res.data)
          if (res.data.isSuccess) {
            alert("해당 사용자에 대한 신고가 접수되었습니다.")
          } else {
            alert(res.data.message)
          }
        })
        .catch((error) => console.log(error))
    }
  }

  // 리뷰 작성
  const handleReviewSubmit = () => {
    if (selectedTags.length === 0) {
      alert("1개 이상의 태그를 선택해주세요")
      return
    }
    const reviewData = {
      content: userReview,
      experience: experience,
      tags: selectedTags,
    }

    console.log(reviewData)

    axios
      .post(`/api/v1/users/${id}/review/${userId}`, reviewData)
      .then((res) => {
        console.log(res.data)
        const newReview = res.data
        newReview.ref = createRef()
        setReviewList([...reviewList, newReview])
        // textArea 초기화
        setUserReview("")
        setSelectedTags([])
        setExperience("")
        // 리뷰 작성 조건 조정 => 리뷰 작성 창 제거
        setReviewed(true)
      })
      .catch((error) => console.log(error))
  }

  // 리뷰 신고 처리 함수
  const handleReportReview = (reviewId) => {
    const data = {
      content: "신고 테스트",
    }
    if (window.confirm("해당 리뷰를 신고하시겠습니까")) {
      axios
        .post(`/api/v1/reports/${reviewId}/user_review/${userId}`, data)
        .then((res) => {
          console.log(res.data)
          if (res.data.isSuccess) {
            alert("해당 사용자에 대한 신고가 접수되었습니다.")
          } else {
            alert(res.data.message)
          }
        })
        .catch((error) => console.log(error))
    }
  }

  // 리뷰 수정 함수
  const handleUpdateReview = (item, editIndex) => {
    const editReviewData = {
      content: editTexts[editIndex],
      experience: item.experience,
      tags: item.tags,
    }
    axios
      .put(`/api/v1/users/${id}/review/${userId}`, editReviewData)
      .then((res) => {
        console.log(res.data)
        const newReviewList = reviewList.map((tmp) => {
          if (tmp.id === parseInt(item.id)) {
            return {
              ...tmp,
              content: editTexts[editIndex],
            }
          }
          return tmp
        })
        setReviewList(newReviewList)
      })
      .catch((error) => console.log(error))
  }

  // 리뷰 삭제 함수
  const handleDeleteReview = () => {
    if (window.confirm("리뷰를 삭제하시겠습니까?")) {
      axios
        .delete(`/api/v1/users/${id}/review/${userId}`)
        .then((res) => {
          console.log(res.data)
          // 사용자당 한개의 리뷰를 작성하기때문에 삭제가 가능한 reviewerId = userId 이다
          setReviewList(reviewList.filter((item) => item.reviewerId !== userId))

          //리뷰 작성 조건 수정
          setReviewed(false)
        })
        .catch((error) => console.log(error))
    }
  }

  // 수정 텍스트 상태 업데이트
  const handleEditTextChange = (index, value) => {
    setEditTexts((prev) => ({
      ...prev,
      [index]: value,
    }));
  };

  return (
    <div className="container mx-auto p-4 max-w-[900px] shadow-md rounded-lg">
      {/* 로딩 애니메이션 */}
      {loading && <LoadingAnimation />}
      {isProfileOwner && (
        <div>
          <button
            type="button"
            className="mb-20 px-4 py-2 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600"
            onClick={() => {
              navigate(`/users/${id}`)
            }}>
            마이 페이지
          </button>
        </div>
      )}
      {/* 전체 div */}
      <div className={`flex-col ${!isProfileOwner ? "mt-24" : ""}`}>
        {blockStatus && (
          <p className="ml-10 mb-0 text-sm text-red-600">
            <FontAwesomeIcon icon={faPersonCircleXmark} />
            &nbsp;차단된 사용자 입니다.
          </p>
        )}
        {/* 프로필 부분 전체 */}
        <div className="relative flex-col ">
          {/* 세로로 가운데, 아이템들 수평 간격 6px 마진 3  */}
          <div className="flex flex-col items-center gap-x-6 m-3 justify-center">
            {/* 프로필 이미지 핸들링 */}
            {profile.profilePicture ? (
              <img src={profile.profilePicture} className="w-40 h-40 rounded-full shadow-lg" alt="" />
            ) : (
              <img
                className="bi bi-person-circle w-40 h-40"
                src={`${process.env.PUBLIC_URL}/img/defaultImages/defaultProfilePicture.svg`}
                alt="default profile img"
              />
            )}

            <div className="mt-5 flex">
              <h3 className="text-xl text-base font-semibold leading-7 tracking-tight text-gray-900">
                <FontAwesomeIcon icon={ratingIcon} color={ratingColor} className="mr-2"></FontAwesomeIcon>
                {profile.nickname}
              </h3>
              {/* Toggle 버튼 (신고/차단) 프로필 주인이 아닐시에만 랜더링*/}
              {!isProfileOwner && (
                <div className="flex items-center dropdown-wrapper">
                  <button
                    onClick={handleClickToggle}
                    data-dropdown-toggle="dropdownDots"
                    className="dropdown-button inline-flex items-center p-2 text-sm font-medium text-center text-gray-600 bg-white rounded-lg hover:bg-gray-100 focus:ring-1focus:outline-none focus:ring-gray-50"
                    type="button">
                    <svg
                      className="w-4 h-4"
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
                      {/* 유저 신고 */}
                      <p onClick={handleReportUser} className="block px-4 py-2 hover:bg-gray-100 cursor-pointer">
                        신고
                      </p>
                    </div>
                  </div>
                </div>
              )}
            </div>
            <div>
              <p className="text-md font-semibold leading-6 text-green-600 space-x-3">
                <span>{profile.gender}</span> <span>{profile.age}</span>
              </p>
            </div>

            {/* 팔로우/팔로우 카운트 뷰 + 팝업 페이지 */}
            <div className="flex space-x-4 justify-center mt-3">
              <button
                onClick={() => {
                  handleOpenModal("followee")
                }}>
                <strong className="text-lg">{profile.followeeCount}</strong>{" "}
                <span className="text-gray-500">Following</span>
              </button>
              <button
                onClick={() => {
                  handleOpenModal("follower")
                }}>
                <strong className="text-lg">{profile.followerCount}</strong>{" "}
                <span className="text-gray-500">Followers</span>
              </button>
            </div>

            {/* 프로필 사용자 / 방문자 구분 */}
            {isProfileOwner ? (
              <div className="mt-5">
                <button
                  className="px-4 py-2 text-sm font-medium rounded-md bg-gray-600 text-gray-100"
                  onClick={handleClick}>
                  프로필 수정하기
                </button>
              </div>
            ) : (
              // 팔로우, 평가
              <div className="flex space-x-3 mt-5">
                <button className={followButtonClasses} onClick={handleClickFollow}>
                  {followingStatus && <FontAwesomeIcon icon={faCheck} />}
                  &nbsp;팔로우
                </button>
                <div id="toast_message" ref={toastMessageRef}>
                  {profile.nickname}님을 팔로우하기 시작합니다
                </div>
              </div>
            )}
          </div>
        </div>
        {/* MODAL */}
        {isModalOpen && <FollowModal id={id} ff={modalTab} onClose={handleCloseModal} />}
        {/*  소셜 링크 렌더링 */}
        <div className="mt-5 flex justify-center">
          {Array.isArray(profile.socialLinks) &&
            profile.socialLinks.map((link, index) => {
              const [platform, url] = link.split("+")
              return (
                <div key={index} className="flex items-center ml-5">
                  <a className="h-8 w-8 rounded-full outline-none focus:outline-none" type="button" href={url}>
                    {platform === "tictok" && (
                      <img src="/img/socialLinks/tictok.svg" className="w-6 mb-3" alt="tictok.svg" />
                    )}
                    {platform === "instagram" && (
                      <img src="/img/socialLinks/instagram.svg" className="mb-3" alt="instagram.svg" />
                    )}
                  </a>
                  <span className="ml-2">{url}</span>
                </div>
              )
            })}
        </div>
        {/* profile message */}
        <div className="mt-5">
          <div>
            <h3 className="text-xl font-bold text-center text-gray-800">Profile Message</h3>
          </div>
          <div
            id="profileMessage"
            className="whitespace-pre-line mt-5 py-5 border-t border-slate-200 text-center min-h-40 overflow-y-auto">
            {profile.profileMessage}
          </div>
        </div>
        {/* -------------------------------------------------------------------------------------- */}
        {/* 리뷰 작성 */}
        {/* 두 조건 모두 거짓(리뷰 하지않음, 프로필 사용자가 아님)이어야 true 를 반환 => 리뷰 작성 랜더링 */}
        {/* + 프로필 사용자가 팔로우 하고 있어야한다. */}
        {!isReviewed && !isProfileOwner && isFollowing && (
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
                <ul
                  className={`w-full transition-all duration-500 ${
                    experience === "BAD"
                      ? "bg-red-100"
                      : experience === "GOOD" || experience === "EXCELLENT"
                      ? "bg-green-100"
                      : "hidden"
                  } p-4 rounded-lg shadow-lg`}>
                  {experience === "BAD"
                    ? reviewNegativeTagList.map((item) => (
                        <li
                          key={item.key}
                          className="flex items-center mb-2 transform transition-transform duration-100">
                          <input
                            id={item.keyword}
                            type="checkbox"
                            value={item.keyword || ""}
                            checked={selectedTags.includes(item.keyword)} // selectedTags 에 포함된 요소만 체크
                            onChange={handleCheckboxChange}
                            className="w-5 h-5 text-red-600 bg-gray-100 border-gray-300 rounded focus:ring-red-500 focus:ring-2"
                          />
                          <label
                            htmlFor={item.keyword}
                            className="ms-2 text-md font-bold text-gray-900 transition-colors duration-300 hover:text-red-600">
                            {item.text}
                          </label>
                        </li>
                      ))
                    : experience === "GOOD" || experience === "EXCELLENT"
                    ? reviewPositiveTagList.map((item) => (
                        <li
                          key={item.key}
                          className="flex items-center mb-2 transform transition-transform duration-100">
                          <input
                            id={item.keyword}
                            type="checkbox"
                            value={item.keyword || ""}
                            checked={selectedTags.includes(item.keyword)} // selectedTags 에 포함된 요소만 체크
                            onChange={handleCheckboxChange}
                            className="w-5 h-5 text-green-600 bg-gray-100 border-gray-300 rounded focus:ring-green-500 focus:ring-2"
                          />
                          <label
                            htmlFor={item.keyword}
                            className="ms-2 text-md font-bold text-gray-900 transition-colors duration-300 hover:text-green-600">
                            {item.text}
                          </label>
                        </li>
                      ))
                    : ""}
                </ul>
              </ul>
            </div>

            <div className="font-bold">{nickname}</div>
            <div className="relative">
              <textarea
                className="border border-white rounded w-full h-24 p-2 "
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
            {reviewList.map((item, index) => (
              <li key={item.id} ref={item.ref}>
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
                      <FontAwesomeIcon className="w-7 h-7 text-green-600 ml-10" icon={faFaceSmile} />
                    ) : item.experience === "EXCELLENT" ? (
                      <FontAwesomeIcon className="w-7 h-7 text-cyan-600 ml-10" icon={faFaceLaughSquint} />
                    ) : (
                      ""
                    )}

                    {/* 리뷰 수정/삭제 신고 */}
                    {/* 리뷰어와 현재 접속자가 같을때만 수정/삭제 랜더링 */}
                    {item.reviewerId === userId ? (
                      <p className="text-xs text-gray-500 ml-auto mr-4 space-x-3">
                        <span
                          className="cursor-pointer"
                          onClick={() => {
                            item.ref.current.querySelector(".updateReviewForm").classList.remove("hidden")
                          }}>
                          수정
                        </span>
                        <span onClick={handleDeleteReview} className="cursor-pointer">
                          삭제
                        </span>
                      </p>
                    ) : (
                      // 리뷰 신고
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

                  {/* 리뷰 수정 폼 */}
                  <div className="updateReviewForm border-3 rounded-lg p-3 mt-4 mb-6 bg-white hidden">
                    <div className="font-bold text-lg">{item.nickname}</div>
                    <div className="relative">
                      <textarea
                        name="content"
                        className="border border-white rounded w-full h-24 p-2"
                        defaultValue={item.content}
                        maxLength={maxLength}
                        onChange={(e) => handleEditTextChange(index, e.target.value)}
                      />
                      <div className="absolute top-2 right-2 text-gray-500 text-sm">
                        {editTexts[index]?.length || 0}/{maxLength}
                      </div>
                    </div>
                    <div className="flex items-center justify-between">
                      <button
                        type="button"
                        className="text-blue-500 hover:text-blue-700 font-semibold"
                        onClick={() => {
                          item.ref.current.querySelector(".updateReviewForm").classList.add("hidden")
                          handleUpdateReview(item, index)
                        }}>
                        수정 확인
                      </button>
                    </div>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  )
}

export default MyProfile
