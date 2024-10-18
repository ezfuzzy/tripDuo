import { useState, useCallback, useEffect, useRef } from "react"
import { useDispatch, useSelector, shallowEqual } from "react-redux"
import { NavLink, useLocation, useNavigate } from "react-router-dom"
import AlertModal from "./AlertModal"
import axios from "axios"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import {
  faBars,
  faBell,
  faChevronDown,
  faChevronUp,
  faMessage,
  faMicrophone,
  faPeoplePulling,
  faPersonSwimming,
  faPersonThroughWindow,
} from "@fortawesome/free-solid-svg-icons"

function NavBar() {
  const username = useSelector((state) => state.userData.username, shallowEqual)
  const profilePicture = useSelector((state) => state.userData.profilePicture, shallowEqual)
  const userId = useSelector((state) => state.userData.id, shallowEqual)
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const location = useLocation()

  const [alertShow, setAlertShow] = useState(false)
  // off-canvas 메뉴 관리
  const [openSections, setOpenSections] = useState({})
  const [lastVisited, setLastVisited] = useState("/")
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [kakaoId, setKakaoId] = useState(null)

  // md 사이즈 이하에서 dropdown menu 관리
  const [isDropdownOpen, setDropdownOpen] = useState(false)

  // off-canvas ref
  const offcanvasRef = useRef(null)
  // drop-down ref
  const dropdownRef = useRef(null)

  const [isOffCanvasOpen, setOffCanvasOpen] = useState(false)
  // NavLink 공통 css
  const offCanvasNavLinkStyle = "hover:bg-gray-100 cursor-pointer text-black no-underline"

  useEffect(() => {
    const token = localStorage.getItem("token")
    const kakaoToken = localStorage.getItem("KakaoToken")
    const KakaoId = localStorage.getItem("kakaoId")
    if (token || kakaoToken || username) {
      setIsLoggedIn(true)
      if (KakaoId) {
        setKakaoId(JSON.parse(KakaoId))
      }
    } else {
      setIsLoggedIn(false)
    }
  }, [username])

  useEffect(() => {
    if (location.pathname === "/home-abroad" || location.pathname === "/") {
      setLastVisited(location.pathname)
    }
  }, [location.pathname])

  // off-canvas 바깥 클릭 감지
  useEffect(() => {
    const handleOutsideCanvasClick = (event) => {
      if (
        offcanvasRef.current &&
        !offcanvasRef.current.contains(event.target) &&
        !event.target.closest(".offcanvas-toggle")
      ) {
        setOffCanvasOpen(false)
      }
    }

    document.addEventListener("mousedown", handleOutsideCanvasClick)

    return () => document.removeEventListener("mousedown", handleOutsideCanvasClick)
  }, [])
  // drop-down 바깥 클릭 감지
  useEffect(() => {
    const handleOutsideDropdownClick = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false)
      }
    }

    document.addEventListener("mousedown", handleOutsideDropdownClick)

    return () => document.removeEventListener("mousedown", handleOutsideDropdownClick)
  }, [])

  const toggleSection = (section) => {
    setOpenSections((prevState) => ({
      ...prevState,
      [section]: !prevState[section],
    }))
  }

  const handleLogout = () => {
    const kakaoToken = localStorage.getItem("KakaoToken")
    const kakaoId = localStorage.getItem("kakaoId")

    if (kakaoToken && kakaoId) {
      axios
        .post(
          "/api/v1/auth/kakaoLogout",
          { kakaoId },
          {
            headers: {
              Authorization: `Bearer ${kakaoToken.substring(7)}`,
              "Content-Type": "application/x-www-form-urlencoded",
            },
          }
        )
        .then((res) => {
          console.log("카카오 로그아웃 성공:", res.data)
          localStorage.removeItem("KakaoToken")
          localStorage.removeItem("kakaoId")
          completeLogout()
        })
        .catch((error) => {
          console.error("카카오 로그아웃 실패:", error)
          alert("로그아웃에 실패했습니다.")
        })
    } else {
      completeLogout()
    }
  }

  const completeLogout = () => {
    localStorage.clear()
    dispatch({ type: "LOGOUT_USER" })
    setIsLoggedIn(false)
    navigate("/")
    window.location.reload()
  }

  const handleYes = () => {
    setAlertShow(false)
  }

  const handleLogin = useCallback(() => {
    navigate("/login")
  }, [navigate])

  const handleLoginLogoutClick = () => {
    if (isLoggedIn) {
      handleLogout()
      setDropdownOpen(!isDropdownOpen)
    } else {
      handleLogin()
      setDropdownOpen(!isDropdownOpen)
    }

    if (offcanvasRef.current) {
      setOffCanvasOpen(false)
    }
  }

  const handleTripDuoClick = () => {
    navigate(lastVisited)
  }

  // 링크 클릭시 off-canvas 닫기 로직
  const closeOffCanvas = () => {
    if (offcanvasRef.current) {
      setOffCanvasOpen(false)
    }
  }

  return (
    <>
      <AlertModal show={alertShow} message={"로그아웃 되었습니다"} yes={handleYes} />

      {/* 글로벌 네비게이션 바 */}
      <nav className="bg-white p-4 flex justify-between items-center relative">
        {/* Off-canvas toggle button */}
        <button type="button" className="text-gray-600 px-4 md:px-10" onClick={() => setOffCanvasOpen(true)}>
          <FontAwesomeIcon icon={faBars} className="h-5 w-5 mr-2" />
        </button>

        {/* md 이상에서만 보여주는 icon */}
        <div className="hidden md:flex space-x-4 md:px-10">
          {isLoggedIn && (
            <>
              <NavLink to={`/chatRoom`}>
                <FontAwesomeIcon icon={faMessage} color="black" className="h-5 w-5 mr-2" />
              </NavLink>
              <FontAwesomeIcon icon={faBell} className="h-5 w-5 mr-2" />
              <NavLink className="mx-3" to={`/users/${userId}`}>
                {profilePicture ? (
                  <img src={profilePicture} className="w-8 h-8 rounded-full" alt="Profile" />
                ) : (
                  <img
                    className="bi bi-person-circle w-6 h-6"
                    src={`${process.env.PUBLIC_URL}/img/defaultImages/defaultProfilePicture.svg`}
                    alt="default profile img"
                  />
                )}
              </NavLink>
            </>
          )}
          <button className="font-bold text-gray-800 pr-4" onClick={handleLoginLogoutClick}>
            {isLoggedIn ? "로그아웃" : "로그인/회원가입"}
          </button>
        </div>
        {/* md 이하에서만 보여주는 icon */}
        <div ref={dropdownRef} className="md:hidden">
          <div className="pr-4">
            <img
              onClick={(e) => {
                e.stopPropagation()
                setDropdownOpen(!isDropdownOpen)
              }}
              className={`bi bi-person-circle ${profilePicture ? "w-8 h-8" : "w-6 h-6"} cursor-pointer rounded-full`}
              src={
                profilePicture
                  ? profilePicture
                  : `${process.env.PUBLIC_URL}/img/defaultImages/defaultProfilePicture.svg`
              }
              alt="default profile img"
            />
          </div>
          {isDropdownOpen && (
            <div className="absolute right-4 top-10 mt-2 w-42 bg-white shadow divide-y divide-gray-100 rounded-lg py-2 z-10">
              <p className="block px-4 py-2 hover:bg-gray-100 ">
                <button className="font-bold text-gray-800" onClick={handleLoginLogoutClick}>
                  {isLoggedIn ? "로그아웃" : "로그인/회원가입"}
                </button>
              </p>
              {isLoggedIn && (
                <>
                  <p className="block px-4 py-2 hover:bg-gray-100">
                    <button
                      onClick={() => {
                        navigate(`/users/${userId}`)
                        setDropdownOpen(!isDropdownOpen)
                      }}>
                      마이 페이지
                    </button>
                  </p>
                  <p className="block px-4 py-2 hover:bg-gray-100">
                    <button
                      onClick={() => {
                        navigate("/chatRoom")
                        setDropdownOpen(!isDropdownOpen)
                      }}>
                      채팅
                    </button>
                  </p>
                </>
              )}
            </div>
          )}
        </div>
        {/* TripDuo 로고는 항상 가로 정중앙에 위치 */}
        <div className="absolute left-1/2 transform -translate-x-1/2">
          <button className="font-bold text-2xl" onClick={handleTripDuoClick}>
            <img className="w-24 h-auto" src="/img/TripDuologo.png" alt="logo" />
          </button>
        </div>
      </nav>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <ul className="flex flex-wrap justify-around -mb-px text-sm font-medium text-center text-gray-500 md:flex-nowrap lg:flex-wrap">
          <li className="mx-2 md:mx-4 lg:mx-2">
            <NavLink
              className={`font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 rounded-t-lg transition-all duration-300 ${
                location.pathname.startsWith("/posts/course") && location.search.includes("di=Domestic")
                  ? "border-b-2 border-tripDuoGreen text-tripDuoGreen transform scale-105"
                  : "border-transparent hover:text-gray-600 hover:border-gray-300"
              } group`}
              to="/posts/course?di=Domestic"
              onClick={() => setLastVisited("/")}>
              <FontAwesomeIcon icon={faPersonThroughWindow} />
              &nbsp;국내 여행
            </NavLink>
          </li>
          <li className="mx-2 md:mx-4 lg:mx-2">
            <NavLink
              className={`font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 rounded-t-lg transition-all duration-300 ${
                location.pathname.startsWith("/posts/course") && location.search.includes("di=International")
                  ? "border-b-2 border-tripDuoGreen text-tripDuoGreen transform scale-105"
                  : "border-transparent hover:text-gray-600 hover:border-gray-300"
              } group`}
              to="/posts/course?di=International"
              onClick={() => setLastVisited("/")}>
              <FontAwesomeIcon icon={faPersonSwimming} />
              &nbsp;해외 여행
            </NavLink>
          </li>
          <li className="mx-2 md:mx-4 lg:mx-2">
            <NavLink
              className={`font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 rounded-t-lg transition-all duration-300 ${
                location.pathname === "/posts/mate"
                  ? "border-b-2 border-tripDuoGreen text-tripDuoGreen transform scale-105"
                  : "border-transparent hover:text-gray-600 hover:border-gray-300"
              } group`}
              to="/posts/mate?di=Domestic">
              <FontAwesomeIcon icon={faPeoplePulling} />
              &nbsp;여행 메이트
            </NavLink>
          </li>
          <li className="mx-2 md:mx-4 lg:mx-2">
            <NavLink
              className={`font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 rounded-t-lg transition-all duration-300 ${
                location.pathname === "/posts/community"
                  ? "border-b-2 border-tripDuoGreen text-tripDuoGreen transform scale-105"
                  : "border-transparent hover:text-gray-600 hover:border-gray-300"
              } group`}
              to="/posts/community?di=Domestic">
              <FontAwesomeIcon icon={faMicrophone} />
              &nbsp;커뮤니티
            </NavLink>
          </li>
        </ul>
      </div>

      {/* Off-canvas */}
      <div
        ref={offcanvasRef}
        className={`z-20 fixed top-0 left-0 w-64 bg-white shadow-lg h-full transition-transform duration-300 ease-in-out ${
          isOffCanvasOpen ? "translate-x-0" : "-translate-x-full"
        }`}
        style={{ transitionProperty: "transform" }}>
        <div className="p-5 border-b flex justify-between">
          <h5>
            <button className="text-lg font-semibold" onClick={handleLoginLogoutClick}>
              {isLoggedIn ? "로그아웃" : "로그인/회원가입"}
            </button>
          </h5>
          <button className="text-lg" onClick={() => offcanvasRef.current.classList.add("hidden")}>
            X
          </button>
        </div>

        <div className="p-4">
          <ul>
            <li className="mb-4">
              <div
                className={`font-bold cursor-pointer flex justify-between mb-4`}
                onClick={() => toggleSection("ourTrip")}>
                <button>여행</button>
                {openSections.ourTrip ? (
                  <FontAwesomeIcon icon={faChevronUp} />
                ) : (
                  <FontAwesomeIcon icon={faChevronDown} />
                )}
              </div>

              <div
                className={`bg-gray-50 rounded-md space-y-2 pl-5 overflow-hidden transition-all duration-300 ease-in-out ${
                  openSections.ourTrip ? "max-h-60" : "max-h-0"
                }`}>
                <div>
                  <NavLink to={"/posts/trip_log"} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    여행 기록
                  </NavLink>
                </div>
                <div>
                  <NavLink to={"/posts/course?di=Domestic"} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    여행 계획
                  </NavLink>
                </div>
                <div>
                  <NavLink to={"/posts/community"} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    커뮤니티
                  </NavLink>
                </div>
              </div>
            </li>

            <li className="mb-4">
              <div
                className={`font-bold cursor-pointer flex justify-between mb-4`}
                onClick={() => toggleSection("tripMate")}>
                <button>여행 메이트</button>
                {openSections.tripMate ? (
                  <FontAwesomeIcon icon={faChevronUp} />
                ) : (
                  <FontAwesomeIcon icon={faChevronDown} />
                )}
              </div>

              <div
                className={`bg-gray-50 rounded-md space-y-2 pl-5 overflow-hidden transition-all duration-300 ease-in-out ${
                  openSections.tripMate ? "max-h-60" : "max-h-0"
                }`}>
                <div>
                  <NavLink to={"/posts/mate?di=Domestic"} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    국내 메이트 게시판
                  </NavLink>
                </div>
                <div>
                  <NavLink
                    to={"/posts/mate?di=International"}
                    className={offCanvasNavLinkStyle}
                    onClick={closeOffCanvas}>
                    해외 메이트 게시판
                  </NavLink>
                </div>
              </div>
            </li>

            <li className="mb-4">
              <div
                className={`font-bold cursor-pointer flex justify-between mb-4`}
                onClick={() => toggleSection("myPage")}>
                <button>마이 페이지</button>
                {openSections.myPage ? (
                  <FontAwesomeIcon icon={faChevronUp} />
                ) : (
                  <FontAwesomeIcon icon={faChevronDown} />
                )}
              </div>

              <div
                className={`bg-gray-50 rounded-md space-y-2 pl-5 overflow-hidden transition-all duration-300 ease-in-out ${
                  openSections.myPage ? "max-h-60" : "max-h-0"
                }`}>
                <div>
                  <NavLink to={`/users/${userId}`} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    마이 페이지
                  </NavLink>
                </div>
                <div>
                  <NavLink to={`/private/myPlan`} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    여행 계획
                  </NavLink>
                </div>
                <div>
                  <NavLink to={`/private/myTripLog`} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    여행 기록
                  </NavLink>
                </div>
                <div>
                  <NavLink to={`/private/wishMate`} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    관심 메이트
                  </NavLink>
                </div>
                <div>
                  <NavLink to={`/private/myPlace`} className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    마이 플레이스
                  </NavLink>
                </div>
              </div>
            </li>

            <li className="mb-4">
              <div
                className={`font-bold cursor-pointer flex justify-between mb-4`}
                onClick={() => toggleSection("extra")}>
                <button>부가 기능</button>
                {openSections.extra ? <FontAwesomeIcon icon={faChevronUp} /> : <FontAwesomeIcon icon={faChevronDown} />}
              </div>
              <div
                className={`bg-gray-50 rounded-md space-y-2 pl-5 overflow-hidden transition-all duration-300 ease-in-out ${
                  openSections.extra ? "max-h-80" : "max-h-0"
                }`}>
                <div>
                  <NavLink to="/utils/checklist" className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    체크 리스트
                  </NavLink>
                </div>
                <div>
                  <NavLink to="/utils/exchangeInfo" className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    환율 정보
                  </NavLink>
                </div>
                <div>
                  <NavLink to="/utils/calculator" className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    여행 경비 계산기
                  </NavLink>
                </div>
                <div>
                  <NavLink to="/utils/safetyInfo" className={offCanvasNavLinkStyle} onClick={closeOffCanvas}>
                    안전 정보
                  </NavLink>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </>
  )
}

export default NavBar
