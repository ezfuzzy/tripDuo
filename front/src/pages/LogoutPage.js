import { useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function LogoutPage() {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        // 로컬 스토리지에서 토큰 삭제
        localStorage.removeItem('token');
        
        // Redux 상태에서 사용자 정보 제거
        dispatch({ type: "UPDATE_USER", payload: null });
        
        navigate('/');  // 홈 페이지 또는 로그인 페이지로 리다이렉트
    })

    return (
        <div>
            
        </div>
    );
}

export default LogoutPage;
