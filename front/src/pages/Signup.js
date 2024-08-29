import axios from 'axios'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router'

function Signup() {
  const [username, setUsername] = useState('')
  const [nickname, setNickName] = useState('')
  const [isValidUsername, setIsValidUsername] = useState(true)
  const [isValidPassword, setIsValidPassword] = useState(true)
  const [isValidNickname, setIsValidNickname] = useState(true)
  const [isValidEmail, setisValidEmail] = useState(true)

  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [isPasswordMatched, setIsPasswordMatched] = useState(true)
  const [hasLowerCase, setHasLowerCase] = useState(false)
  const [hasUpperCase, setHasUpperCase] = useState(false)
  const [hasNumber, setHasNumber] = useState(false)
  const [hasSpecialChar, setHasSpecialChar] = useState(false)
  const [isValidLength, setIsValidLength] = useState(false)
  const [showValidationMessage, setShowValidationMessage] = useState(false)

  const [email, setEmail] = useState('')

  const [phoneNumber, setPhoneNumber] = useState('')
  const [verificationCode, setVerificationCode] = useState('')
  const [isCodeSent, setIsCodeSent] = useState(false)
  const [isVerified, setIsVerified] = useState(false)

  const [isAllChecked, setIsAllChecked] = useState(false)
  const navigate = useNavigate()

  const validateUsername = (value) => {
    // 아이디 : 영어 소문자와 숫자로 이루어진 6~16자리
    const regex = /^[a-z0-9]{6,16}$/
    return regex.test(value)
  }
  const validatePassword = (value) => {
    // 비밀번호 : 영어 대소문자, 숫자, 특수문자를 포함한 8~15자리
    setHasLowerCase(/[a-z]/.test(value))
    setHasUpperCase(/[A-Z]/.test(value))
    setHasNumber(/\d/.test(value))
    setHasSpecialChar(/[!@#$%^&*]/.test(value))
    setIsValidLength(value.length >= 8 && value.length <= 15)
  }
  const validateNickname = (value) => {
    // 닉네임 : 한글, 영어 대소문자, 숫자로 이루어진 2~16자리
    const regex = /^[가-힣a-zA-Z0-9]{2,16}$/
    return regex.test(value)
  }
  const validateEmail = (value) => {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    return regex.test(value)
  }

  const usernameHandleChange = (e) => {
    const value = e.target.value
    setUsername(value)
    setIsValidUsername(validateUsername(value))
  }
  const passwordHandleChange = (e) => {
    const value = e.target.value
    setPassword(value)
    validatePassword(value)
    setShowValidationMessage(true)
  }
  const confirmPasswordHandleChange = (e) => {
    setConfirmPassword(e.target.value);
  }
  const nicknameHandleChange = (e) => {
    const value = e.target.value
    setNickName(value)
    setIsValidNickname(validateNickname(value))
  }

  const phoneNumberHandleChange = (e) => {
    setPhoneNumber(e.target.value)
  }
  const emailHandleChange = (e) => {
    const value = e.target.value
    setEmail(value)
    setisValidEmail(validateEmail(value))
  }
  const verificationCodeHandleChange = (e) => {
    setVerificationCode(e.target.value)
  }

  const sendVerificationCode = () => {
    //인증요청 전송 api
    axios.post('/api/v1/auth/send', phoneNumber)
      .then((response) => {
        setIsCodeSent(true)
        alert('인증 코드가 전송되었습니다.')
      })
      .catch((error) => {
        alert('인증 코드 전송에 실패했습니다.')
      })
  }
  const verifyPhoneNumber = () => {
    const code = verificationCode
    //인증 확인 api
    axios.post('/api/v1/auth/verify', {phoneNumber, code})
      .then((response) => {
        setIsVerified(true)
        alert('휴대폰 번호가 성공적으로 인증되었습니다.')
      })
      .catch((error) => {
        alert('인증 코드가 잘못되었습니다.')
      })
  }

  const handleSignUp = async (e) => {
    e.preventDefault()
    try {
      //회원가입 api
      axios.post(`/api/v1/auth/signup`, { username, password, nickname, phoneNumber, email })
        .then((response) => {
          // 회원가입 성공시 토큰 발급으로 로그인 처리
          const token = response.data
          localStorage.setItem('token', token)
          axios.defaults.headers.common["Authorization"] = token

          navigate("/completedSignup")
        })
        .catch((error) => {
          console.error('회원가입에 실패하였습니다')
        })
    } catch (error) {
      console.error('회원가입에 실패하였습니다')
    }
  }

  useEffect(() => {
    if (hasLowerCase && hasUpperCase && hasNumber && hasSpecialChar && isValidLength) {
      setIsValidPassword(true)
      setShowValidationMessage(false)
    } else {
      setIsValidPassword(false)
    }
  }, [hasLowerCase, hasUpperCase, hasNumber, hasSpecialChar, isValidLength])

  useEffect(() => {
    setIsPasswordMatched(password === confirmPassword);
  }, [password, confirmPassword])

  useEffect(() => {
    if (isValidUsername && isValidPassword && isValidNickname && isValidEmail && isVerified) {
      setIsAllChecked(true)
    } else {
      setIsAllChecked(false)
    }
  }, [isValidUsername, isValidPassword, isValidNickname, isValidEmail, isVerified])

  return (
    <div className="isolate bg-white px-6 py-24 sm:py-32 lg:px-8">
      <div className="mx-auto max-w-2xl text-center">
        <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">회원가입</h2>
      </div>
      <form action="#" method="POST" className="mx-auto mt-16 max-w-xl sm:mt-20">
        <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-6 sm:gap-x-4">
          <div className="sm:col-span-6">
            <label htmlFor="username" className="block text-sm font-semibold leading-6 text-gray-900">
              아이디
            </label>
            <div className="mt-2.5">
              <input
                id="username"
                name="username"
                type="text"
                value={username}
                onChange={usernameHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                required
              />
              {!isValidUsername && (
                <p className="mt-2 text-sm text-red-600">
                  아이디는 영어 소문자와 숫자로 이루어진 6~16자리여야 합니다.
                </p>
              )}
            </div>
          </div>

          <div className="sm:col-span-6">
            <label htmlFor="password" className="block text-sm font-semibold leading-6 text-gray-900">
              비밀번호
            </label>
            <div className="mt-2.5">
              <input
                id="password"
                name="password"
                type="password"
                value={password}
                onChange={passwordHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                required
              />
              {showValidationMessage && !isValidPassword && (
                <div className="mt-2 text-sm">
                  <p>비밀번호는 영어
                    <span className={hasLowerCase ? 'text-blue-600' : 'text-red-600'}> 소문자</span>,
                    <span className={hasUpperCase ? 'text-blue-600' : 'text-red-600'}> 대문자</span>,
                    <span className={hasNumber ? 'text-blue-600' : 'text-red-600'}> 숫자</span>,
                    <span className={hasSpecialChar ? 'text-blue-600' : 'text-red-600'}> 특수문자</span>를 포함한
                    <span className={isValidLength ? 'text-blue-600' : 'text-red-600'}> 8~15자리</span>여야 합니다.
                  </p>
                </div>
              )}
            </div>
          </div>

          <div className="sm:col-span-6">
            <label htmlFor="confirmPassword" className="block text-sm font-semibold leading-6 text-gray-900">
              비밀번호 확인
            </label>
            <div className="mt-2.5">
              <input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                value={confirmPassword}
                onChange={confirmPasswordHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                required
              />
              {!isPasswordMatched && (
                <div className="mt-2 text-sm">
                  <p className="mt-2 text-sm text-red-600">
                    비밀번호가 일치하지 않습니다.
                  </p>
                </div>
              )}
            </div>
          </div>

          <div className="sm:col-span-6">
            <label htmlFor="nickname" className="block text-sm font-semibold leading-6 text-gray-900">
              닉네임
            </label>
            <div className="mt-2.5">
              <input
                id="nickname"
                name="nickname"
                type="text"
                value={nickname}
                onChange={nicknameHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                required
              />
              {!isValidNickname && (
                <p className="mt-2 text-sm text-red-600">
                  닉네임은 한글, 영어 대소문자, 숫자로 이루어진 2~16자리여야 합니다.
                </p>
              )}
            </div>
          </div>
          <div className="sm:col-span-6">
            <label htmlFor="email" className="block text-sm font-semibold leading-6 text-gray-900">
              Email
            </label>
            <div className="mt-2.5">
              <input
                id="email"
                name="email"
                type="email"
                value={email}
                onChange={emailHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                required
              />
            </div>
          </div>

          <div className="sm:col-span-6">
            <label htmlFor="phonenumber" className="block text-sm font-semibold leading-6 text-gray-900">
              휴대전화번호
            </label>
            <div className="mt-2.5 flex">
              <input
                id="phonenumber"
                name="phonenumber"
                type="tel"
                value={phoneNumber}
                onChange={phoneNumberHandleChange}
                className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                required
              />
              <button
                type="button"
                onClick={sendVerificationCode}
                className="ml-4 inline-flex justify-center rounded-md bg-indigo-600 px-3.5 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                인증 코드 전송
              </button>
            </div>
          </div>

          {/* 인증 코드 입력 */}
          {isCodeSent && (
            <div className="sm:col-span-6">
              <label htmlFor="verification-code" className="block text-sm font-semibold leading-6 text-gray-900">
                인증 코드
              </label>
              <div className="mt-2.5 flex">
                <input
                  id="verification-code"
                  name="verification-code"
                  type="text"
                  value={verificationCode}
                  onChange={verificationCodeHandleChange}
                  className="block w-full rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
                <button
                  type="button"
                  onClick={verifyPhoneNumber}
                  className="ml-4 inline-flex justify-center rounded-md bg-indigo-600 px-3.5 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                  인증 확인
                </button>
              </div>
            </div>
          )}
        </div>

        <div className="mt-10">
          <button
            type="submit"
            className="block w-full rounded-md bg-indigo-600 px-3.5 py-2.5 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            disabled={!isAllChecked}  // 인증 성공 후에만 버튼 활성화
            onClick={handleSignUp}
          >
            회원가입
          </button>
        </div>
      </form>
    </div>
  )
}

export default Signup