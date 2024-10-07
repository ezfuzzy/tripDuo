import axios from "axios";
import React, { useState } from "react";

function ResetPassword(props) {
  const [phoneNumber, setPhoneNumber] = useState("");
  const [verificationCode, setVerificationCode] = useState(""); 
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isVerified, setIsVerified] = useState(false);

  const [isAllChecked, setIsAllChecked] = useState(false)

  //실제 저장용 값('-' 제거)
  const getUnformattedPhoneNumber = () => phoneNumber.replace(/\D/g, "");

  const updateIsAllChecked = (updates = {}) => {
    const newState = {
      isVerified,
      ...updates
    }

    const allChecked = Object.values(newState).every(Boolean)
    setIsAllChecked(allChecked)
  }

  const phoneNumberHandleChange = (e) => {
    const { value } = e.target;
    // 숫자만 추출
    const numericValue = value.replace(/\D/g, "");
    // 전화번호 형식으로 변환
    const formattedValue = formatPhoneNumber(numericValue);
    setPhoneNumber(formattedValue);

    updateIsAllChecked()
  };

  const verificationCodeHandleChange = (e) => {
    setVerificationCode(e.target.value)
    updateIsAllChecked()
  }

  //전화번호 포맷처리 함수
  const formatPhoneNumber = (value) => {
    if (value.length <= 3) return value;
    if (value.length <= 6) return `${value.slice(0, 3)}-${value.slice(3)}`;
    if (value.length <= 10) return `${value.slice(0, 3)}-${value.slice(3, 6)}-${value.slice(6)}`;
    return `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
  };

  const sendVerificationCode = () => {
    //인증요청 전송 api
    axios
      .post("/api/v1/auth/phone/send-code", phoneNumber)
      .then((response) => {
        setIsCodeSent(true);
        alert("인증 코드가 전송되었습니다.");
      })
      .catch((error) => {
        alert("인증 코드 전송에 실패했습니다.");
      });
  };

  const verifyPhoneNumber = () => {
    //인증 확인 api
    axios
      .post("/api/v1/auth/phone/verify-code", { phoneNumber, code: verificationCode })
      .then((response) => {
        setIsVerified(true);
        updateIsAllChecked({ isVerified: true })
        alert("휴대폰 번호가 성공적으로 인증되었습니다.");
      })
      .catch((error) => {
        alert("인증 코드가 잘못되었습니다.");
      });
  };

  return (
    <>
      <div>
        <div className="sm:col-span-6">
          <label htmlFor="phoneNumber" className="block text-sm font-semibold leading-6 text-gray-900">
            휴대전화번호
          </label>
          <div className="mt-2.5 flex">
            <input
              id="phoneNumber"
              name="phoneNumber"
              type="tel"
              value={phoneNumber}
              onChange={phoneNumberHandleChange}
              className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              required
            />
            <button
              type="button"
              onClick={() => sendVerificationCode(getUnformattedPhoneNumber())}
              className="ml-4 inline-flex justify-center rounded-md bg-indigo-600 px-3.5 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
              인증 코드 전송
            </button>
          </div>
        </div>

        {/* 인증 코드 입력 */}
        {isCodeSent && (
          <div className="sm:col-span-6">
            <label htmlFor="verificationCode" className="block text-sm font-semibold leading-6 text-gray-900">
              인증 코드
            </label>
            <div className="mt-2.5 flex">
              <input
                id="verificationCode"
                name="verificationCode"
                type="text"
                value={verificationCode}
                onChange={verificationCodeHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
              <button
                type="button"
                onClick={verifyPhoneNumber}
                className="ml-4 inline-flex justify-center rounded-md bg-indigo-600 px-3.5 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                인증 확인
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}

export default ResetPassword;
