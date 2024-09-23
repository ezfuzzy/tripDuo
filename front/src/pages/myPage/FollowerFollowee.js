import { setActive } from "@material-tailwind/react/components/Tabs/TabsContext";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router";

function FollowerFollowee() {
  // follwee id, follwer or followee list
  const { id, ff } = useParams();

  const [followerList, setFollowerList] = useState([]);
  const [followeeList, setFolloweeList] = useState([]);

  const [activeTab, setActiveTab] = useState(ff);

  const [followerFollowee, setFollowerFollowee] = useState(ff);

  useEffect(() => {
    if (followerFollowee === "follower") {
      axios
        .get(`/api/v1/users/${id}/followersInfo`)
        .then((res) => {
          setFollowerList(res.data);
          setFollowerFollowee("follower");
        })
        .catch((error) => console.log(error));
    } else if (followerFollowee === "followee") {
      axios
        .get(`/api/v1/users/${id}/follow/followeesInfo`)
        .then((res) => {
          setFolloweeList(res.data);
          setFollowerFollowee("followee");
        })
        .catch((error) => console.log(error));
    }
  }, [followerFollowee, id, ff]);

  const handleActiveTab = (tab) => {
    setActiveTab(tab);
    setFollowerFollowee(tab);
  };

  return (
    <>
      <div className="text-sm font-medium text-center text-gray-500 border-b border-gray-200 h-12">
        <ul className="flex flex-wrap justify-center">
          <li className="me-2">
            <p
              onClick={() => handleActiveTab("followee")}
              className={`h-3 cursor-pointer text-md font-bold inline-block p-4 border-b-2 border-transparent rounded-t-lg hover:text-gray-700 hover:border-gray-400 ${
                activeTab === "followee" ? "text-blue-700 border-blue-400" : ""
              }`}
            >
              팔로잉
            </p>
          </li>
          <li className="">
            <p
              onClick={() => handleActiveTab("follower")}
              className={`h-3 cursor-pointer text-md font-bold inline-block p-4 border-b-2 border-transparent rounded-t-lg hover:text-gray-700 hover:border-gray-400 ${
                activeTab === "follower" ? "text-blue-700 border-blue-400" : ""
              }`}
            >
              팔로워
            </p>
          </li>
        </ul>
      </div>

      <ul className="divide-y divide-gray-100">
        {followerFollowee === "follower" &&
          followerList.map((follower) => (
            <li key={follower.userId} className="flex justify-between gap-x-6 py-5">
              <div className="flex min-w-0 gap-x-4">
                <img alt="" src={follower.profilePicture} className="h-12 w-12 flex-none rounded-full bg-gray-50" />
                <div className="min-w-0 flex-auto">
                  <p className="text-sm font-semibold leading-6 text-gray-900">{follower.nickname}</p>
                </div>
              </div>
              <div className="hidden shrink-0 sm:flex sm:flex-col sm:items-end">
                <p className="text-sm leading-6 text-gray-900">나이/성별</p>
              </div>
            </li>
          ))}

        {followerFollowee === "followee" &&
          followeeList.map((followee) => (
            <li key={followee.userId} className="flex justify-between gap-x-6 py-5">
              <div className="flex min-w-0 gap-x-4">
                <img alt="" src={followee.profilePicture} className="h-12 w-12 flex-none rounded-full bg-gray-50" />
                <div className="min-w-0 flex-auto">
                  <p className="text-sm font-semibold leading-6 text-gray-900">{followee.nickname}</p>
                </div>
              </div>
              <div className="hidden shrink-0 sm:flex sm:flex-col sm:items-end">
                <p className="text-sm leading-6 text-gray-900">나이/성별</p>
              </div>
            </li>
          ))}
      </ul>
    </>
  );
}

export default FollowerFollowee;
