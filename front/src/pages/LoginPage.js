// LoginPage.js
import React, { useState } from 'react';
import LoginModal from '../components/LoginModal';
import { NavLink } from 'react-router-dom';
import { Nav } from 'react-bootstrap';
import GoogleAuthLogin from '../components/GoogleAuthLogin';

function LoginPage() {
    const [showLoginModal, setShowLoginModal] = useState(false);

    const handleOpenModal = () => {
        setShowLoginModal(true);  // 모달을 열도록 설정
    };

    const handleCloseModal = () => {
        setShowLoginModal(false); // 모달 닫기
    };

    return (
        <div>
            <h2>로그인 페이지</h2>
            <button onClick={handleOpenModal}>기본 로그인</button> {/* 기본 로그인 버튼 */}
            <LoginModal show={showLoginModal} onClose={handleCloseModal} message="로그인" />
            <br />
            <GoogleAuthLogin show={true} />
            <br />
            <p>회원가입 하지 않으셨다면 <Nav.Link as={NavLink} to="/signup" >클릭</Nav.Link></p>
        </div>
    );
}

export default LoginPage;
