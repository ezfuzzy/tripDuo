import { createBrowserRouter } from "react-router-dom"
import App from "../App"

import Home from "../pages/Home"
import Sample from "../pages/Sample"
import HomeInternational from "../pages/HomeInternational"
import HomeMate from "../pages/HomeMate"
import Signup from "../pages/Signup"
import Agreement from "../pages/Agreement"
import LoginPage from "../pages/LoginPage"
import LogoutPage from "../pages/LogoutPage"
import MyPage from "../pages/myPage/MyPage"
import MyProfile from "../pages/myPage/MyProfile"
import MyProfileForm from "../pages/myPage/MyProfileForm"
import CompletedSignup from "../pages/CompletedSignup"
import KakaoRedirect from "../components/KakaoRedirect"
import ChangePassword from "../pages/myPage/ChangePassword"
import MateBoard from "../pages/boards/MateBoard"
import MateBoardForm from "../pages/boards/MateBoardForm"
import MateBoardEditForm from "../pages/boards/MateBoardEditForm"
import MateBoardDetail from "../pages/boards/MateBoardDetail"
import TravelChecklist from "../pages/Utilities/TravelChecklist"
import TravelSafetyInfo from "../pages/Utilities/TravelSafetyInfo"
import TravelCostCalculator from "../pages/Utilities/TravelCostCalculator"
import TravelPlanner from "../pages/Utilities/TravelPlaner"
import LocationRecommendations from "../pages/Utilities/LocationRecommendations"
import ExchangeInfo from "../pages/Utilities/ExchangeInfo"
import TravelDiary from "../pages/Utilities/TravelDiary"
import LanguageCultureTips from "../pages/Utilities/LanguageCultureTips"
import ExtraPage from "../pages/Utilities/ExtraPage"
import Course from "../pages/boards/Course"
import CourseForm from "../pages/boards/CourseForm"
import CourseDetail from "../pages/boards/CourseDetail"
import CourseUpdateForm from "../pages/boards/CourseUpdateForm"

const routes = [
    // /users/:id
    // /users/:id/setting
    // /posts/:mate, course, photo, review }/:id
    // 
    {path:"/", element: <Home/>},
    {path:"/home-inter", element: <HomeInternational/>},
    {path:"/home-mate", element: <HomeMate/>},
    {path:"/users/:id", element: <MyPage/>},
    {path:"/users/:id/profile", element:<MyProfile/>},
    {path:"/users/:id/setting", element: <MyProfileForm/>},
    {path:"/sample", element: <Sample/>},
    {path:"/agreement", element: <Agreement/>},
    {path:"/signup", element: <Signup/>},
    {path:"/login", element: <LoginPage/>},
    {path:"/logout", element: <LogoutPage /> },
    {path:"/completedSignup", element:<CompletedSignup/>},
    {path:"/api/v1/auth/kakao/accessTokenCallback", element:<KakaoRedirect/>},
    {path:"/:id/changePassword", element:<ChangePassword/>},
    {path:"/mateBoard", element:<MateBoard/>},
    {path:"/mateBoard/new", element:<MateBoardForm/>},
    {path:"/mateBoard/:num/edit", element:<MateBoardEditForm/>},
    {path:"/mateBoard/:num/detail", element:<MateBoardDetail/>},
    {path:"/checklist", element:<TravelChecklist/>},
    {path:"/exchange", element: <ExchangeInfo/>},
    {path:"/safetyInfo", element: <TravelSafetyInfo/>},
    {path:"/calculator", element: <TravelCostCalculator/>},
    {path:"/planner", element: <TravelPlanner/>},
    {path:"/recommendations", element: <LocationRecommendations/>},
    {path:"/diary", element: <TravelDiary/>},
    {path:"/languageTip", element: <LanguageCultureTips/>},
    {path:"/extra", element: <ExtraPage/>},
    {path:"/course", element: <Course/>},
    {path:"/course/new", element:<CourseForm/>},
    {path:"/course/detail", element:<CourseDetail/>},
    {path:"/course/:id/edit", element:<CourseUpdateForm/>}

]

const router = createBrowserRouter([{
    path:"/",
    element: <App/>,
    children: routes.map((route)=>{
        return {
            index: route.path ==="/",
            path: route.path === "/" ? undefined : route.path,
            element: route.element
        }

    })
}])

export default router