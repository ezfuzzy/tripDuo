import axios from 'axios';
import { useEffect, useState } from 'react';
import { shallowEqual, useSelector } from 'react-redux';
import { Link, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';

function MyPage() {
  const userId = useSelector(state => state.userData.id, shallowEqual) // 접속된 사용자의 id
  const [profile, setProfile] = useState({})
  const { id } = useParams()
  const navigate = useNavigate()
  const [imageData, setImageData] = useState(null)

  useEffect(() => {
    axios.get(`/api/v1/users/${id}`)
      .then(res => {
        if(!userId || userId !== res.data.userId){
          alert("잘못된 접근입니다.")
          navigate(`/`)
        }

        setProfile(res.data)

        if(res.data.profilePicture){
          setImageData(res.data.profilePicture)
        }


      })
      .catch(error => console.log(error))

  }, [id])

  const handleClick = () => {
    navigate(`/users/${id}/profile`);
  }

  return (
    <>
      {/* 프로필 */}
      <div className='m-3 flex justify-center'>
        <div className="flex items-center gap-x-6 m-3">
          {
            imageData
              ?
              <img src={imageData} className='w-20 h-20 rounded-full' />
              :
              <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" fill="currentColor" className="bi bi-person-circle" viewBox="0 0 16 16">
                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                <path fillRule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z" />
              </svg>
          }
          <div>
            <h3 className="text-base font-semibold leading-7 tracking-tight text-gray-900">{profile.nickname}</h3>
            <p className="text-sm font-semibold leading-6 text-indigo-600">{profile.gender} / {profile.age}</p>
          </div>
          <div>
            <button className='btn btn-secondary btn-sm' onClick={handleClick}>프로필 보기</button>
          </div>
        </div>
      </div>

      <h1 className="text-3xl font-bold m-4">My Page</h1>
        <div className="borderbox">
          <ul className="grid grid-cols-2 gap-4">
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3><Link className="text-gray-500 hover:text-black text-decoration-none" to={`/myPlan/${id}`}><strong>Travel Plan</strong>(여행 계획)</Link></h3>
            <p>여행을 계획하거나<br />계획한 여행들을 확인하실 수 있습니다.</p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3><Link className="text-gray-500 hover:text-black text-decoration-none" to={`/myRecord/${id}`}><strong>Travel Record</strong>(여행 기록)</Link></h3>
            <p>고객님의 여행 기록을 확인하실 수 있습니다.</p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3><Link className="text-gray-500 hover:text-black text-decoration-none" to={`/wishMate/${id}`}><strong>Wish Mate</strong>(관심 메이트)</Link></h3>
            <p>관심 메이트로 등록하신 여행 메이트를 확인하실 수 있습니다.</p>
          </li>
          <li className="bg-white shadow-md rounded-lg p-4">
            <h3><Link className="text-gray-500 hover:text-black text-decoration-none" to={`/myPlace/${id}`}><strong>My Place</strong>(마이 플레이스)</Link></h3>
            <p>관심있는 지역, 음식점들을 관리할 수 있습니다.</p>
          </li>
        </ul>
      </div>
    </>
  );
}

export default MyPage;