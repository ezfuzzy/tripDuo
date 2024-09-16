import { useState, useCallback, useEffect } from "react";
import { useDispatch, useSelector, shallowEqual } from "react-redux";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import AlertModal from "./AlertModal";
import axios from 'axios';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars, faBell, faPeoplePulling, faPersonSwimming, faPersonThroughWindow, faPersonWalkingLuggage, faUser } from "@fortawesome/free-solid-svg-icons";

function NavBar() {
    const username = useSelector(state => state.userData.username, shallowEqual);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useLocation();

    const [alertShow, setAlertShow] = useState(false);
    const [openSections, setOpenSections] = useState({});
    const [lastVisited, setLastVisited] = useState('/');
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [profile, setProfile] = useState({});
    const [kakaoId, setKakaoId] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const kakaoToken = localStorage.getItem('KakaoToken');
        const KakaoId = localStorage.getItem('kakaoId');
        if (token || kakaoToken || username) {
            setIsLoggedIn(true);
            if (KakaoId) {
                setKakaoId(JSON.parse(KakaoId))
            }
        } else {
            setIsLoggedIn(false);
        }
    }, [username]);

    const toggleSection = (section) => {
        setOpenSections(prevState => ({
            ...prevState,
            [section]: !prevState[section],
        }));
    };

    const handleLogout = () => {
        const kakaoToken = localStorage.getItem("KakaoToken");
        const kakaoId = localStorage.getItem("kakaoId");

        if (kakaoToken && kakaoId) {
            axios.post("/api/v1/auth/kakaoLogout", { kakaoId }, {
                headers: {
                    "Authorization": `Bearer ${kakaoToken.substring(7)}`,
                    "Content-Type": "application/x-www-form-urlencoded",
                },
            })
                .then(res => {
                    console.log("카카오 로그아웃 성공:", res.data);
                    localStorage.removeItem("KakaoToken");
                    localStorage.removeItem("kakaoId");
                    completeLogout();
                })
                .catch(error => {
                    console.error("카카오 로그아웃 실패:", error);
                    alert("로그아웃에 실패했습니다.");
                });
        } else {
            completeLogout();
        }
    };

    const completeLogout = () => {
        localStorage.clear();
        dispatch({ type: "LOGOUT_USER", payload: null });
        setIsLoggedIn(false);
        navigate('/');
        window.location.reload();
    };

    const handleYes = () => {
        setAlertShow(false);
    };

    const handleLogin = useCallback(() => {
        navigate('/login');
    }, [navigate]);

    const handleLoginLogoutClick = () => {
        if (isLoggedIn) {
            handleLogout();
        } else {
            handleLogin();
        }

        const offcanvasElement = document.getElementById('staticBackdrop');
        if (offcanvasElement) {
            offcanvasElement.classList.add('hidden');
        }
    };

    const handleTripDuoClick = () => {
        navigate(lastVisited);
    };

    useEffect(() => {
        if (username) {
            axios.get(`/api/v1/users/username/${username}`)
                .then(res => {
                    console.log(res);
                    setProfile(res.data);
                })
                .catch(error => console.log(error));
        }
    }, [username]);

    useEffect(() => {
        if (location.pathname === '/home-abroad' || location.pathname === '/') {
            setLastVisited(location.pathname);
        }
    }, [location.pathname]);

    return (
        <>
            <AlertModal show={alertShow} message={"로그아웃 되었습니다"} yes={handleYes} />

            <nav className="bg-white p-4 shadow-md flex justify-between items-center">
                {/* Off-canvas toggle button */}
                <button type="button" className="text-gray-600 pl-20" onClick={() => document.getElementById('staticBackdrop').classList.toggle('hidden')}>
                    <FontAwesomeIcon icon={faBars} className="h-5 w-5 mr-2" />
                </button>

                <button className='font-bold text-2xl absolute left-1/2 transform -translate-x-1/2' onClick={handleTripDuoClick}>
                    TripDuo
                </button>

                <div className="flex space-x-4">
                    {isLoggedIn && (
                        <>
                            <FontAwesomeIcon icon={faBell} className="h-5 w-5 mr-2" />
                            <NavLink className="mx-3" to={`/users/${profile.id}`}>
                                {profile.profilePicture
                                    ? <img src={profile.profilePicture} className="w-8 h-8 rounded-full" alt="Profile" />
                                    : <FontAwesomeIcon icon={faUser} color="black" className="h-5 w-5 mr-2" />}
                            </NavLink>
                        </>
                    )}
                    <button className="font-bold text-gray-800 pr-20" onClick={handleLoginLogoutClick}>
                        {isLoggedIn ? '로그아웃' : '로그인/회원가입'}
                    </button>
                </div>
            </nav>

            {/* Tabs */}
            <div className="border-b border-gray-200">
                <ul className="flex flex-wrap justify-around -mb-px text-sm font-medium text-center text-gray-500 ">
                    <li className="mx-2">
                        <NavLink className="font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 border-transparent rounded-t-lg hover:text-gray-600 hover:border-gray-300 group"
                            style={{
                                color: "black",
                                textDecoration: "none"
                            }}
                            to="/"
                            onClick={() => setLastVisited('/')}>
                            <FontAwesomeIcon icon={faPersonThroughWindow} />&nbsp;국내 여행</NavLink>

                    </li>
                    <li className="mx-2">
                        <NavLink className="font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 border-transparent rounded-t-lg hover:text-gray-600 hover:border-gray-300 group"
                            style={{
                                color: "black",
                                textDecoration: "none"
                            }}
                            to="/home-abroad"
                            onClick={() => setLastVisited('/home-abroad')}>
                            <FontAwesomeIcon icon={faPersonSwimming} />&nbsp;해외 여행</NavLink>
                    </li>
                    <li className="mx-2">
                        <NavLink className="font-bold text-lg inline-flex items-center justify-center p-3 border-b-2 border-transparent rounded-t-lg hover:text-gray-600 hover:border-gray-300 group"
                            style={{
                                color: "black",
                                textDecoration: "none"
                            }}
                            to="/home-mate">
                            <FontAwesomeIcon icon={faPeoplePulling} />&nbsp;여행 메이트</NavLink>
                    </li>
                </ul>
            </div>

            {/* Off-canvas */}
            <div className="fixed top-0 left-0 w-64 bg-white shadow-lg h-full hidden" id="staticBackdrop">
                <div className="p-4 border-b flex justify-between">
                    <h5>
                        <button className="text-lg font-semibold" onClick={handleLoginLogoutClick}>
                            {isLoggedIn ? '로그아웃' : '로그인/회원가입'}
                        </button>
                    </h5>
                    <button className="text-lg" onClick={() => document.getElementById('staticBackdrop').classList.add('hidden')}>X</button>
                </div>

                <div className="p-4">
                    <ul>
                        <li className="mb-4">
                            <button className="font-bold" onClick={() => toggleSection('domestic')}>국내 여행</button>
                            {openSections.domestic && (
                                <div className="pl-4">
                                    <div>여행 기록</div>
                                    <div className="hover:bg-gray-100 cursor-pointer" onClick={() => navigate("/posts/course")}>여행 계획</div>
                                    <div>여행 메이트</div>
                                    <div>여행 정보</div>
                                    <div>커뮤니티</div>
                                </div>
                            )}
                        </li>

                        <li className="mb-4">
                            <button className="font-bold" onClick={() => toggleSection('travelMate')}>여행 메이트</button>
                            {openSections.travelMate && (
                                <div className="pl-4">
                                    <div className="hover:bg-gray-100 cursor-pointer" onClick={() => { navigate(`/posts/mate?di=Domestic`) }}>국내 메이트 게시판</div>
                                    <div className="hover:bg-gray-100 cursor-pointer" onClick={() => navigate(`/posts/mate?di=International`)}>해외 메이트 게시판</div>
                                </div>
                            )}
                        </li>

                        <li className="mb-4">
                            <button className="font-bold" onClick={() => toggleSection('myPage')}>마이 페이지</button>
                            {openSections.myPage && (
                                <div className="pl-4">
                                    <div className="hover:bg-gray-100 cursor-pointer" onClick={() => navigate(`/users/${profile.id}`)}>마이 페이지</div>
                                    <div>여행 기록</div>
                                    <div>여행 계획</div>
                                    <div>관심 메이트</div>
                                    <div>마이 플레이스</div>
                                    <div>체크 리스트</div>
                                    <div>예산 관리</div>
                                </div>
                            )}
                        </li>
                    </ul>
                </div>
            </div>
        </>
    );
}

export default NavBar;
