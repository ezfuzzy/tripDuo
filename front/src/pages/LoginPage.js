import React from 'react';
import LoginModal from '../components/LoginModal'; // 이미 만든 로그인 폼 컴포넌트 사용
import { NavLink } from 'react-router-dom';
import { Nav } from 'react-bootstrap';

function LoginPage() {
    return (
        <div>
            <h2>로그인</h2>
            <LoginModal show={true} onClose={() => {}} />
            <p>회원가입 하지 않으셨다면 <Nav.Link as={NavLink} to="/signup" >클릭</Nav.Link></p>
        </div>
    );
}

export default LoginPage;
