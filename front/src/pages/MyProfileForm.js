import React, { useRef, useState } from 'react';
import { Button, Col, Form } from 'react-bootstrap';

function MyProfileForm(props) {
    const inputImage = useRef();
    const [profile, setProfile] = useState({
        id: 0,
        signin_id: "MY ID",
        userNickname: "userNick-000789",
        name: "MY NAME",
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

    const handleChange = (e) => {
        setProfile({
            ...profile,
            [e.target.name]: e.target.value
        })
    }

    const handleInputImage = ()=>{
        alert("gd")
    }

    const handleSave = () => {

    }

    return (
        <>
            <h3>Profile Update Form</h3>
            <div className="mt-20 flex justify-center h-screen">

                <form className="space-y-4" >

                <div className='m-3 flex justify-center'>

                    <input onChange={handleInputImage} className="hidden" type="file" ref={inputImage} name="profile_pics" accept="image/" multiple />

                    {
                        profile.profile_pics != null
                            ?
                            <img onClick={()=>inputImage.current.click()} src={profile.profile_pics[0]} className='w-[150px] h-[150px] rounded-full mb-4' />
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
                            <input type="text" name="signin_id" value={profile.signin_id} className="block w-full p-2 border border-gray-300 rounded-md" />
                        </div>
                        <div className="mb-3 flex-1">
                            <label htmlFor="password" className="block text-sm font-medium mb-1">Password</label>
                            <input type="button" name="password" className="block w-full p-2 border border-gray-300 rounded-md" value="To Update Password" />
                        </div>
                    </div>

                    <div className="flex space-x-4 bg-gray-200 rounded">
                        <div className="mb-3 flex-1">
                            <label htmlFor="name" className="block text-sm font-medium mb-1">Name</label>
                            <input type="text" name="name" value={profile.name} className="block w-full p-2 border border-gray-300 rounded-md" />
                        </div>
                        <div className="mb-3 flex-1">
                            <label htmlFor="age" className="block text-sm font-medium mb-1">age</label>
                            <input type="text" name="age" value={profile.age} className="block w-full p-2 border border-gray-300 rounded-md" />
                        </div>
                        <div className="mb-3 flex-1">
                            <label htmlFor="gender" className="block text-sm font-medium mb-1">gender</label>
                            <input type="text" name="gender" className="block w-full p-2 border border-gray-300 rounded-md" value={profile.gender} />
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