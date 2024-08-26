// LoginModal.js
import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import '../css/LoginPage.css';

function LoginModal({ show, message, onClose }) {
    const [state, setState] = useState({
        userName: '',
        password: ''
    });
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setState(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    //실제 로그인 방식이니 지우지 마세요
    // const handleLogin = () => {
    //     if (!state.userName || !state.password) {
    //         setError("아이디와 비밀번호를 모두 입력해주세요.");
    //         return;  // 여기서 함수가 종료되므로 아래 코드가 실행되지 않음
    //     }

    //     axios.post("/api/v1/auth/login", state)
    //         .then(res => {
    //             const token = res.data;
    //             localStorage.setItem('token', token);
    //             setError(false);

    //             const result = decodeToken(token.substring(7));
    //             const userName = result.payload.sub;
    //             dispatch({ type: "UPDATE_USER", payload: userName });
    //             dispatch({ type: "LOGIN_MODAL", payload: { show: false } });
    //             axios.defaults.headers.common["Authorization"] = token;

    //             navigate('/'); // 로그인 성공 시 리다이렉트
    //         })
    //         .catch(() => {
    //             setError("로그인에 실패했습니다.");  // 에러 메시지 설정
    //         });
    // };
    
    //해당 방식은 임시 로그인
    const handleLogin = () => {
        if (!state.userName || !state.password) {
            setError("아이디와 비밀번호를 모두 입력해주세요.");
            return;
        }

        alert("로그인이 수락되었습니다.");
        dispatch({ type: "LOGIN_MODAL", payload: { show: false } });
        dispatch({ type: "UPDATE_USER", payload: { userName: state.userName } });
        navigate('/');  
        onClose(); 
    };

    const handleClose = () => {
        onClose(); 
    };

    if (!show) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h5>{message}</h5>
                    <button className="close-button" onClick={handleClose}>×</button>
                </div>
                <div className="modal-body">
                    <label htmlFor="userName">User Name</label>
                    <input 
                        type="text" 
                        name="userName" 
                        id="userName"
                        value={state.userName} 
                        placeholder="User Name..." 
                        onChange={handleChange} 
                    />
                    <label htmlFor="password">Password</label>
                    <input 
                        type="password" 
                        name="password" 
                        id="password"
                        value={state.password} 
                        placeholder="Password..." 
                        onChange={handleChange} 
                    />
                    {error && <div style={{ color: 'red' }}>{error}</div>} 
                </div>
                <div className="modal-footer">
                    <button onClick={handleLogin}>로그인</button>
                </div>
            </div>
        </div>
    );
}

export default LoginModal;
