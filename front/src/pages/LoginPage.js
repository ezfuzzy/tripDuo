import React, { useState, useEffect, useRef } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import axios from "axios";
import { decodeToken } from "jsontokens";
import "../css/LoginPage.css";
import { Link, NavLink } from "react-router-dom";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

function LoginPage() {
  const [loginData, setLoginData] = useState({
    username: "",
    password: "",
  });
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const stompClient = useRef(null);  // WebSocket 연결 객체
  const [messages, setMessages] = useState([]);  // 메시지 목록
  const [isWebSocketConnected, setIsWebSocketConnected] = useState(false);  // WebSocket 연결 상태

  // WebSocket 연결 함수
  const connectWebSocket = (roomId, callback) => {
    const socket = new SockJS('http://localhost:8888/api/ws');
    stompClient.current = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,

      onConnect: () => {
        stompClient.current.subscribe(`/topic/room/${roomId}`, (messageOutput) => {
          const newMessage = JSON.parse(messageOutput.body);
          setMessages((prevMessages) => [...prevMessages, newMessage]);
        });
        console.log("WebSocket connected");
        setIsWebSocketConnected(true);  // WebSocket 연결 상태 업데이트
        if (callback) callback();  // WebSocket 연결 후 콜백 함수 호출
      },

      onStompError: (error) => {
        console.error('STOMP error:', error);
      },

      onWebSocketClose: () => {
        console.log('WebSocket connection closed.');
        setIsWebSocketConnected(false);  // WebSocket 연결 상태 업데이트
      }
    });
    stompClient.current.activate();
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLoginData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleLogin();
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    const kakaoToken = localStorage.getItem("KakaoToken");

    if (token || kakaoToken) {
      // 토큰이 있는 상태라면 홈으로 리디렉션
      navigate("/");
    }
  }, [navigate]);

  const processToken = (token) => {
    if (token.startsWith("Bearer+")) {
      localStorage.setItem("token", token);
      const result = decodeToken(token.substring(7));

      const userData = {
        id: result.payload.id,
        username: result.payload.username,
        nickname: result.payload.nickname,
        profilePicture: result.payload.profilePicture,
      };

      const loginStatus = {
        isLogin: true,
        role: result.payload.role,
      };

      dispatch({ type: "LOGIN_USER", payload: { userData, loginStatus } });
      axios.defaults.headers.common["Authorization"] = token;

      // WebSocket 연결
      connectWebSocket(result.payload.roomId, () => {
        // WebSocket 연결 후 경로 이동
        navigate("/");
        window.location.reload();
      });
    }
  };

  const handleLogin = () => {
    if (!loginData.username || !loginData.password) {
      setError("아이디와 비밀번호를 모두 입력해주세요.");
      return;
    }

    axios
      .post("/api/v1/auth/login", loginData)
      .then((res) => {
        processToken(res.data);
      })
      .catch(() => {
        setError("로그인에 실패했습니다.");
      });
  };

  const googleURL = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${process.env.REACT_APP_GOOGLE_LOGIN_API_KEY}&redirect_uri=${process.env.REACT_APP_GOOGLE_REDIRECT_URL}&response_type=code&scope=email profile`;

  const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.REACT_APP_KAKAO_LOGIN_API_KEY}&redirect_uri=${process.env.REACT_APP_KAKAO_REDIRECT_URL}&response_type=code`;

  const handleGoogleLogin = () => {
    window.location = googleURL;
  };

  const handleKakaoLogin = () => {
    window.location = kakaoURL;
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div>
          <img className="w-48 h-auto mx-auto mb-5" src="/img/TripDuologo.png" alt="logo" />
        </div>
        <h2>로그인</h2>
        <div className="login-form">
          <label htmlFor="username">User Name</label>
          <input
            type="text"
            name="username"
            id="username"
            value={loginData.username}
            placeholder="User Name..."
            onChange={handleChange}
            onKeyDown={handleKeyDown}
          />
          <label htmlFor="password">Password</label>
          <input
            type="password"
            name="password"
            id="password"
            value={loginData.password}
            placeholder="Password..."
            onChange={handleChange}
            onKeyDown={handleKeyDown}
          />
          {error && <div style={{ color: "red" }}>{error}</div>}
          <button onClick={handleLogin} className="login-button">
            로그인
          </button>
        </div>
        <div className="flex flex-col items-center space-y-4">
          <img 
            className="w-48 h-auto cursor-pointer" 
            src="/img/KakaoLogin.png" 
            alt="Kakao Login" 
            onClick={handleKakaoLogin}
          />
          <img 
            className="w-48 h-auto cursor-pointer" 
            src="/img/GoogleLogin.png" 
            alt="Google Login" 
            onClick={handleGoogleLogin}
          />
        </div>
        <p className="mt-6 text-center">
          회원가입 하지 않으셨다면{" "}
          <Link as={NavLink} to="/agreement" className="text-blue-500 hover:underline font-semibold">
            클릭
          </Link>
        </p>
        {/* WebSocket 연결 상태 표시 */}
        <div>
          WebSocket 연결 상태: {isWebSocketConnected ? "연결됨" : "연결되지 않음"}
        </div>
      </div>
    </div>
  );
}

export default LoginPage;