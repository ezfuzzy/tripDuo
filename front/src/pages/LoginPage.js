import React from 'react';
import LoginModal from '../components/LoginModal'; // 이미 만든 로그인 폼 컴포넌트 사용

function LoginPage() {
    return (
        <div>
            <h2>로그인</h2>
            <LoginModal show={true} onClose={() => {}} />
        </div>
    );
}

export default LoginPage;
