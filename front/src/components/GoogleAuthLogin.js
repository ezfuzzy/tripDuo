import React from 'react';

function GoogleAuthLogin() {
  const handleLogin = () => {
    // 사용자를 서버의 OAuth 엔드포인트로 리디렉션
    window.location.href = 'https://your-server.com/auth/google';//경로 설정 필요
  };

  return (
    <div>
      <button onClick={handleLogin}>Login with Google</button>
    </div>
  );
}

export default GoogleAuthLogin;
