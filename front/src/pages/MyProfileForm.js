import React, { useState } from 'react';
import { Button, Col, Form, Row } from 'react-bootstrap';

function MyProfileForm(props) {

    const [profile, setProfile] = useState({
        id: 0,
        signin_id: "MY ID",
        userNickname: "userNick-000789",
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
            [e.target.name] : e.target.value
        })
    }

    const handleSave = () => {

    }

    return (
        <>
            <h3>Profile Update Form</h3>
            <form className="space-y-4">
                <div className="mb-3">
                    <label htmlFor="name" className="block text-sm font-medium mb-1">Name</label>
                    <input
                        type="text"
                        name="userNickname"
                        onChange={handleChange}
                        value={profile.userNickname}
                        className="block w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="addr" className="block text-sm font-medium mb-1">Address</label>
                    <input
                        type="text"
                        name="addr"
                        placeholder="Input Address"
                        onChange={handleChange}
                        value={profile.addr}
                        className="block w-full p-2 border border-gray-300 rounded-md"
                    />
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
        </>
    );
}

export default MyProfileForm;