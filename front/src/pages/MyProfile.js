import React, { useState } from 'react';
import 'holderjs'
import 'bootstrap/dist/css/bootstrap.css'
import { Link, useNavigate } from 'react-router-dom';

function MyProfile(props) {
    /*
        cur_location, rating, last_login
    */
    const navigate = useNavigate()
    const [profile, setProfile] = useState({
        id: 0,
        signin_id: "MY ID",
        userNickname: "userNick-000789",
        name:"MY NAME",
        age: 20,
        gender: "attack helicopter",
        phone_num: "01048854885",
        email: "prfile-sample@google.com",
        profile_pics: ["https://picsum.photos/id/237/200/300"],
        profile_msg: "this is profile massage",
        social_links: {
            instagram: "https://www.instagram.com/katarinabluu/",
            gitHub: "https://github.com/ezfuzzy/tripDuo/"
        }

    })
    const handleClick = () => {
        navigate("/myProfileForm")
    }
    return (
        <>
            <h3>My Profile</h3>
            <div className="container h-screen">
                <div>
                    <div class="flex items-center gap-x-6">
                        {
                            profile.profile_pics != null
                                ?
                                <img src={profile.profile_pics[0]} className='w-20 h-20 rounded-full' />
                                :
                                <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                                    <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z" />
                                </svg>
                        }
                        <div>
                            <h3 class="text-base font-semibold leading-7 tracking-tight text-gray-900">{profile.userNickname}</h3>
                            <p class="text-sm font-semibold leading-6 text-indigo-600">{profile.gender} / {profile.age}</p>
                        </div>
                        <div>
                            <button className='btn btn-secondary btn-sm' onClick={handleClick}>to updataform</button>
                        </div>
                    </div>
                    <div className="mt-3">
                        <a class="h-8 w-8 rounded-full outline-none focus:outline-none" type="button"
                            href={profile.social_links.instagram}>
                            <svg class="fill-current transition duration-700 ease-in-out text-gray-700 dark:text-gray-400 hover:text-pink-600 dark:hover:text-pink-600"
                                role="img" width="24" height="24" viewBox="0 0 24 24"
                                xmlns="http://www.w3.org/2000/svg">
                                <title>Instagram</title>
                                <path
                                    d="M12 0C8.74 0 8.333.015 7.053.072 5.775.132 4.905.333 4.14.63c-.789.306-1.459.717-2.126 1.384S.935 3.35.63 4.14C.333 4.905.131 5.775.072 7.053.012 8.333 0 8.74 0 12s.015 3.667.072 4.947c.06 1.277.261 2.148.558 2.913.306.788.717 1.459 1.384 2.126.667.666 1.336 1.079 2.126 1.384.766.296 1.636.499 2.913.558C8.333 23.988 8.74 24 12 24s3.667-.015 4.947-.072c1.277-.06 2.148-.262 2.913-.558.788-.306 1.459-.718 2.126-1.384.666-.667 1.079-1.335 1.384-2.126.296-.765.499-1.636.558-2.913.06-1.28.072-1.687.072-4.947s-.015-3.667-.072-4.947c-.06-1.277-.262-2.149-.558-2.913-.306-.789-.718-1.459-1.384-2.126C21.319 1.347 20.651.935 19.86.63c-.765-.297-1.636-.499-2.913-.558C15.667.012 15.26 0 12 0zm0 2.16c3.203 0 3.585.016 4.85.071 1.17.055 1.805.249 2.227.415.562.217.96.477 1.382.896.419.42.679.819.896 1.381.164.422.36 1.057.413 2.227.057 1.266.07 1.646.07 4.85s-.015 3.585-.074 4.85c-.061 1.17-.256 1.805-.421 2.227-.224.562-.479.96-.899 1.382-.419.419-.824.679-1.38.896-.42.164-1.065.36-2.235.413-1.274.057-1.649.07-4.859.07-3.211 0-3.586-.015-4.859-.074-1.171-.061-1.816-.256-2.236-.421-.569-.224-.96-.479-1.379-.899-.421-.419-.69-.824-.9-1.38-.165-.42-.359-1.065-.42-2.235-.045-1.26-.061-1.649-.061-4.844 0-3.196.016-3.586.061-4.861.061-1.17.255-1.814.42-2.234.21-.57.479-.96.9-1.381.419-.419.81-.689 1.379-.898.42-.166 1.051-.361 2.221-.421 1.275-.045 1.65-.06 4.859-.06l.045.03zm0 3.678c-3.405 0-6.162 2.76-6.162 6.162 0 3.405 2.76 6.162 6.162 6.162 3.405 0 6.162-2.76 6.162-6.162 0-3.405-2.76-6.162-6.162-6.162zM12 16c-2.21 0-4-1.79-4-4s1.79-4 4-4 4 1.79 4 4-1.79 4-4 4zm7.846-10.405c0 .795-.646 1.44-1.44 1.44-.795 0-1.44-.646-1.44-1.44 0-.794.646-1.439 1.44-1.439.793-.001 1.44.645 1.44 1.439z" />
                            </svg>
                        </a>

                        <a class="h-8 w-8 rounded-full outline-none focus:outline-none" type="button"
                            href={profile.social_links.gitHub}>
                            <svg class="fill-current transition duration-700 ease-in-out text-gray-700 dark:text-gray-400 hover:text-black dark:hover:text-black"
                                role="img" width="24" height="24" viewBox="0 0 24 24"
                                xmlns="http://www.w3.org/2000/svg">
                                <title>Github</title>
                                <path
                                    d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113.82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998.108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0-6.627-5.373-12-12-12" />
                            </svg>
                        </a>
                    </div>
                    <p> 전화번호 : {profile.phone_num}</p>
                    <p> 이메일 : {profile.email}</p>
                    <div className="mb-3">
                        <label htmlFor="profile_msg" className="form-label">자기 소개</label>
                        <textarea name="profile_msg" className="form-control" rows="5" value={profile.profile_msg} />
                    </div>
                </div>

                <div>
                    <h5>보안 탭</h5>
                    <p>회원 탈퇴</p>
                    <p>로그인 기록</p>
                    <p>내 활동 기록</p>
                </div>
            </div>

        </>
    );
}


export default MyProfile;