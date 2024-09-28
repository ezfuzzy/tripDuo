import { useSelector } from "react-redux";
import { Navigate } from "react-router";

function ProtectedRoute({ children }) {
  const isLogin = useSelector((state) => state.loginStatus.isLogin)

    if (!isLogin) {
      alert("해당 페이지는 로그인이 필요합니다.")
      return <Navigate to="/login" replace />;
    }

  return children;

}

export default ProtectedRoute;
