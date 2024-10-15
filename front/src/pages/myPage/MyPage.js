import axios from "axios"
import { useEffect, useState } from "react"
import { shallowEqual, useDispatch, useSelector } from "react-redux"
import { Link, useNavigate, useParams } from "react-router-dom"
import BlockModal from "../../components/BlockModal"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faUser } from "@fortawesome/free-solid-svg-icons"
import { ratingConfig } from "../../constants/mapping"

function MyPage() {
  const navigate = useNavigate()

  const userId = useSelector((state) => state.userData.id, shallowEqual) // 접속된 사용자의 id
  const userRole = useSelector((state) => state.loginStatus.role, shallowEqual)
  const [profile, setProfile] = useState({})
  const { id } = useParams()
  const [imageData, setImageData] = useState(null)

  // 차단 목록 모달 상태 관리
  const [isBlockModalOpen, setBlockModalOpen] = useState(false)

  //--------------------------------------------------------------------------------------------------------------rating 관리 부
  // rating 비교 조건 데이터

  // rating 값에 따른 아이콘과 색상 계산 //
  const getRatingDetails = (ratings) => {
    return (
      ratingConfig.find((config) => ratings >= config.min && ratings <= config.max) || { icon: faUser, color: "black" }
    ) // 기본값
  }

  const { icon: ratingIcon, color: ratingColor } = getRatingDetails(profile.ratings || 0)
  //---------------------------------------------------------------------------------------------------------------rating 관리부

  // 접속된 사용자가 없거나 본인이 아니라면 home 으로 리다일렉트
  useEffect(() => {
    axios
      .get(`/api/v1/users/${id}`)
      .then((res) => {
        if (!userId || userId !== res.data.userProfileInfo.userId) {
          alert("잘못된 접근입니다.")
          navigate(`/`)
        }

        setProfile(res.data.userProfileInfo)

        if (res.data.userProfileInfo.profilePicture) {
          setImageData(res.data.userProfileInfo.profilePicture)
        }
      })
      .catch((error) => console.log(error))
  }, [id, userId, navigate, ratingIcon])

  //------------------------------------------------------------------------ 이벤트 관리부
  // 프로필 보기 클릭
  const handleClick = () => {
    navigate(`/users/${id}/profile`)
  }

  // 차단 목록 모달 open
  const handleOpenBlockModal = () => {
    setBlockModalOpen(true)
  }
  // 차단 목록 모달 close
  const handleCloseBlockModal = () => {
    setBlockModalOpen(false)
  }

  const handleDeleteUser = () => {
    if (
      window.prompt(
        `[주의] \n회원 탈퇴를 진행하시면 사용자의 정보가 영구히 삭제됩니다. \n이해하셨으면 [탈퇴] 를 입력해 주세요.`
      ) === "탈퇴"
    ) {
      axios
        .delete(`/api/v1/users/${id}`)
        .then((res) => {
          navigate("/", { state: { needLogout: true } })
        })
        .catch((error) => console.log(error))
    }
  }

  return (
    <div className="container mx-auto p-4 max-w-[900px] shadow-md rounded-lg">
      <div>
        <button
          type="button"
          className="mb-20 px-4 py-2 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600"
          onClick={() => {
            navigate(`/`)
          }}>
          HOME
        </button>
      </div>
      {/* 프로필 */}
      <div className="m-3 flex justify-center">
        <div className="flex items-center gap-x-6 m-3">
          {imageData ? (
            <img src={imageData} className="w-20 h-20 rounded-full shadow-lg" />
          ) : (
            <img
              className="bi bi-person-circle w-20 h-20"
              src={`${process.env.PUBLIC_URL}/img/defaultImages/defaultProfilePicture.svg`}
              alt="default profile img"
            />
          )}
          <div>
            <h3 className="text-base font-semibold leading-7 tracking-tight text-gray-900">
              <FontAwesomeIcon icon={ratingIcon} color={ratingColor} className="mr-2"></FontAwesomeIcon>
              {profile.nickname}
            </h3>
            <p className="text-sm font-semibold leading-6 text-green-600">
              {profile.gender} / {profile.age}
            </p>
          </div>
          <div>
            <button
              className="px-4 py-2 text-sm font-medium rounded-md bg-gray-600 text-gray-100"
              onClick={handleClick}>
              프로필 보기
            </button>
          </div>
        </div>
      </div>

      {/* 마이 페이지 메뉴 */}
      <h1 className="text-3xl font-bold m-4">My Page</h1>
      <div className="borderbox">
        <ul className="grid grid-cols-2 gap-4">
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3>
              <Link className="text-gray-500 hover:text-black text-decoration-none" to={`/myPlan`}>
                <strong>Travel Plan</strong>(여행 계획)
              </Link>
            </h3>
            <p>
              여행을 계획하거나
              <br />
              계획한 여행들을 확인하실 수 있습니다.
            </p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3>
              <Link className="text-gray-500 hover:text-black text-decoration-none" to={`/myRecord`}>
                <strong>Travel Record</strong>(여행 기록)
              </Link>
            </h3>
            <p>고객님의 여행 기록을 확인하실 수 있습니다.</p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3>
              <Link className="text-gray-500 hover:text-black text-decoration-none" to={`/wishMate`}>
                <strong>Wish Mate</strong>(관심 메이트)
              </Link>
            </h3>
            <p>관심 메이트로 등록하신 여행 메이트를 확인하실 수 있습니다.</p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3>
              <Link className="text-gray-500 hover:text-black text-decoration-none" to={`/myPlace`}>
                <strong>My Place</strong>(마이 플레이스)
              </Link>
            </h3>
            <p>관심있는 지역, 음식점들을 관리할 수 있습니다.</p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3>
              <Link className="text-gray-500 hover:text-black text-decoration-none" to={`/likedCourse`}>
                <strong>Liked Courses</strong>(관심 여행 계획)
              </Link>
            </h3>
            <p>좋아요를 누른 여행 계획을 볼 수 있습니다.</p>
          </li>
          {userRole === "ADMIN" ? (
            <li className="bg-white shadow-md rounded-lg p-4">
              <h3>
                <Link className="text-gray-500 hover:text-black text-decoration-none" to={`/admin-dashboard`}>
                  <strong>ADMIN DASHBOARD</strong>
                </Link>
              </h3>
              <p>admin dashboard</p>
            </li>
          ) : (
            ""
          )}
        </ul>
      </div>

      {isBlockModalOpen && <BlockModal id={id} onClose={handleCloseBlockModal} />}

      <div className="mt-20 space-y-3">
        <p>
          <strong className="py-2 hover:bg-gray-100 cursor-pointer" onClick={handleOpenBlockModal}>
            차단 목록
          </strong>
        </p>
        <p>
          <strong className="py-2 hover:bg-gray-100 cursor-pointer" onClick={handleDeleteUser}>
            회원 탈퇴
          </strong>
        </p>
        <p className="py-2">내 활동 기록</p>
      </div>
    </div>
  )
}

export default MyPage
