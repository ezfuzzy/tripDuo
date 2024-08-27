import React from 'react';
import { NavLink } from 'react-router-dom';
import { Nav } from 'react-bootstrap';
import GoogleAuthLogin from '../components/GoogleAuthLogin';

function LoginListPage() {

    return (
        <div>
            <h2>로그인 페이지</h2>
            <Nav.Link as={NavLink} to="/login">기본 로그인</Nav.Link>
            <br />
            <GoogleAuthLogin show={true} />
            <br />
            <p>회원가입 하지 않으셨다면 <Nav.Link as={NavLink} to="/agreement">클릭</Nav.Link></p>
        </div>
    );
}

export default LoginListPage;
