import axios from "axios";
import React, { useState } from "react";

function PasswordUpdate(props) {
  
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [isValidNewPassword, setIsValidNewPassword] = useState(true);
  const [isPasswordMatch, setIsPasswordMatch] = useState(true);

  const validatePassword = (password) => {
    const regex = /^[가-힣a-zA-Z0-9]{2,16}$/;
    return regex.test(password);
  };

  const handleNewPasswordChange = (e) => {
    const password = e.target.value;
    setNewPassword(password);
    setIsValidNewPassword(validatePassword(password));
    setError(""); // 에러 메시지 초기화
  };

  const handleConfirmPasswordChange = (e) => {
    const password = e.target.value;
    setConfirmPassword(password);
    setIsPasswordMatch(password === newPassword);
  };

  const handleChangePassword = async () => {
    if (!isValidNewPassword) {
      setError("새 비밀번호는 2~16자 사이의 한글, 영문, 숫자만 가능합니다.");
      return;
    }
    if (!isPasswordMatch) {
      setError("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      // 현재 비밀번호 확인
      const response = await axios.get("/api/check-password", {
        params: { password: currentPassword },
      });

      if (response.data.valid) {
        // 비밀번호 변경 요청
        await axios.put("/api/change-password", { newPassword });
        setSuccess("비밀번호가 성공적으로 변경되었습니다.");
        setError("");
      } else {
        setError("현재 비밀번호가 잘못되었습니다.");
      }
    } catch (err) {
      setError("오류가 발생했습니다. 다시 시도해 주세요.");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-6 rounded-lg shadow-md w-96">
        <h2 className="text-2xl font-bold mb-4">비밀번호 변경</h2>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">
            현재 비밀번호:
          </label>
          <input
            type="password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded-md"
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">새 비밀번호:</label>
          <input
            type="password"
            value={newPassword}
            onChange={handleNewPasswordChange}
            className={`w-full p-2 border rounded-md ${
              isValidNewPassword ? "border-gray-300" : "border-red-500"
            }`}
          />
          {!isValidNewPassword && (
            <p className="text-red-500 text-sm">
              새 비밀번호는 2~16자 사이의 한글, 영문, 숫자만 가능합니다.
            </p>
          )}
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">
            새 비밀번호 확인:
          </label>
          <input
            type="password"
            value={confirmPassword}
            onChange={handleConfirmPasswordChange}
            className={`w-full p-2 border rounded-md ${
              isPasswordMatch ? "border-gray-300" : "border-red-500"
            }`}
          />
          {!isPasswordMatch && (
            <p className="text-red-500 text-sm">
              새 비밀번호와 확인 비밀번호가 일치하지 않습니다.
            </p>
          )}
        </div>
        <button
          onClick={handleChangePassword}
          className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 transition"
        >
          변경
        </button>
        {error && <p className="text-red-500 mt-2">{error}</p>}
        {success && <p className="text-green-500 mt-2">{success}</p>}
      </div>
    </div>
  );
}

export default PasswordUpdate;
