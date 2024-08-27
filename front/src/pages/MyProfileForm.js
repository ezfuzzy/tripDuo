import axios from 'axios';
import React, { useEffect, useRef, useState } from 'react';
import { Button, Col, Form } from 'react-bootstrap';
import { useParams } from 'react-router';

function MyProfileForm(props) {

    const profileImage = useRef();
    const inputImage = useRef();

    const [profile, setProfile] = useState({})

    const {id} = useParams()
    

    useEffect(()=>{
        axios.get(`/api/v1/users/${id}`)
        .then(res=>{
            console.log(res)
            setProfile(res.data)
        })
        .catch(error=>console.log(error))

    }, [id])

    // 이벤트 관리부
    const handleChange = (e) => {
        setProfile({
            ...profile,
            [e.target.name]: e.target.value
        })
    }

    const handleInputImage = (e)=>{
        const file = e.target.files[0]

        const reg=/image/

        if(!reg.test(file.type)){ 
            alert("이미지 파일이 아닙니다")
            return
        }
        
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload=(e)=>{
            const data = e.target.result
            profileImage.current.src = data           
        }

        
    }

    const handleSave = () => {

    }
    // form 에서 전송되는 데이터 : profile_pics, profile_msg ,(email),(phone_num)
    return (
        <>
            <h3>Profile Update Form</h3>
            <div className="mt-20 flex justify-center h-screen">

                <form className="space-y-4" >

                <div className='m-3 flex justify-center'>

                    <input onChange={handleInputImage} ref={inputImage} className="hidden" type="file" name="profile_pics" accept="image/" multiple />

                    {
                        profile.profile_pics != null
                            ?
                            <img ref={profileImage} onClick={()=>inputImage.current.click()} src={profile.profile_pics[0]} className='w-[150px] h-[150px] rounded-full mb-4' />
                            :   
                            <svg  xmlns="http://www.w3.org/2000/svg" width="150" height="150" fill="currentColor" className="bi bi-person-circle mb-4" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                                <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z" />
                            </svg>
                    }

                </div>

                    <div className="flex space-x-4 bg-gray-200 rounded">
                        <div className="mb-3 flex-1">
                            <label htmlFor="signin_id" className="block text-sm font-medium mb-1">ID</label>
                            <input type="text" name="signin_id" value={profile.username} className="block w-full p-2 border border-gray-300 rounded-md" readOnly/>
                        </div> 
                        <div className="mb-3 flex-1">
                            <label htmlFor="password" className="block text-sm font-medium mb-1">Password</label>
                            <input type="button" name="password" className="block w-full p-2 border border-gray-300 rounded-md" value="To Update Password" readOnly/>
                        </div>
                    </div>

                    <div className="flex space-x-4 bg-gray-200 rounded">
                        <div className="mb-3 flex-1">
                            <label htmlFor="name" className="block text-sm font-medium mb-1">Nickname</label>
                            <input type="text" name="name" value={profile.nickname} className="block w-full p-2 border border-gray-300 rounded-md" readOnly/>
                        </div>
                        <div className="mb-3 flex-1">
                            <label htmlFor="age" className="block text-sm font-medium mb-1">age</label>
                            <input type="text" name="age" value={profile.age} className="block w-full p-2 border border-gray-300 rounded-md" readOnly/>
                        </div>
                        <div className="mb-3 flex-1">
                            <label htmlFor="gender" className="block text-sm font-medium mb-1">gender</label>
                            <input type="text" name="gender" value={profile.gender} className="block w-full p-2 border border-gray-300 rounded-md"  readOnly/>
                        </div>
                    </div>

                    <div className="flex space-x-4">
                        <div className="mb-3 flex-1">
                            <label htmlFor="phone_num" className="block text-sm font-medium mb-1">Mobile Number</label>
                            <input onChange={handleChange} type="text" name="phone_num" value={profile.phone_num} className="block w-full p-2 border border-gray-300 rounded-md" />
                        </div>
                        <div className="mb-3 flex-1">
                            <label htmlFor="email" className="block text-sm font-medium mb-1">Email</label>
                            <input onChange={handleChange} type="text" name="email" value={profile.email} className="block w-full p-2 border border-gray-300 rounded-md" />
                        </div>
                    </div>

                    <div className="mb-3">
                        <label htmlFor="profile_msg" className="block text-sm font-medium mb-1">Prifile Message</label>
                        <textarea onChange={handleChange} name="profile_msg" className="form-control w-full h-auto resize-none overflow-y-auto" rows="5" value={profile.profile_msg} />
                    </div>
                    <div>
                        <button
                            type="button"
                            onClick={handleSave}
                            className="w-20 py-2 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600"
                        >
                            Save
                        </button>
                    </div>
                </form>
            </div>
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
            <br />
        </>
    );
}

export default MyProfileForm;