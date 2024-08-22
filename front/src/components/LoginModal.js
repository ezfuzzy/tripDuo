import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";

function LoginModal({ show, message, onClose }) {
    const [state, setState] = useState({
        userName: '',
        password: ''
    });
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [error, setError] = useState('');
    // Re-enable the handleChange function to update the state when the user types
    const handleChange = (e) => {
        const { name, value } = e.target;
        setState(prevState => ({
            ...prevState,
            [name]: value
        }));
    };
    // 기존 코드 주석 처리
    /*토큰 관련 코드 주석 처리
   
    const handleLogin = () => {
        axios.post("/auth", state)
            .then(res => {
                const token = res.data;
                localStorage.setItem('token', token);
                setError(false);
                
                const result = decodeToken(token.substring(7));
                const userName = result.payload.sub;
                dispatch({ type: "UPDATE_USER", payload: userName });
                dispatch({ type: "LOGIN_MODAL", payload: { show: false } });
                axios.defaults.headers.common["Authorization"] = token;
            })
            .catch(() => {
                setError(true);
            });
    };
    */
    //임시로 어떤 상황에서든 로그인 가능하게 만듦
    const handleLogin = () => {
        if (!state.userName || !state.password) {
            setError("아이디와 비밀번호를 모두 입력해주세요.");
            return;  // 조건을 충족하지 않으면 로그인 절차를 중단
        }
    
        // 로그인 성공 시 처리
        alert("로그인이 수락되었습니다.");
        dispatch({ type: "LOGIN_MODAL", payload: { show: false } });
        dispatch({ type: "UPDATE_USER", payload: { userName: state.userName } });
        navigate('/');  // 홈 페이지로 이동
    };

    if (!show) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h5>{message}</h5>
                    <button className="close-button" onClick={onClose}>×</button>   
                </div>
                <div className="modal-body">
                    <label htmlFor="userName">User Name</label>
                    <input 
                        type="text" 
                        name="userName" 
                        id="userName"
                        value={state.userName} 
                        placeholder="User Name..." 
                        onChange={handleChange}  // Attach onChange handler
                    />
                    <label htmlFor="password">Password</label>
                    <input 
                        type="password" 
                        name="password" 
                        id="password"
                        value={state.password} 
                        placeholder="Password..." 
                        onChange={handleChange}  // Attach onChange handler
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
