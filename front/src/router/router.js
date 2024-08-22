import { createBrowserRouter } from "react-router-dom"
import App from "../App"
import Mypage from "../pages/Mypage"
import Home from "../pages/Home"
import Sample from "../pages/Sample"
import HomeInternational from "../pages/HomeInternational"
import HomeMate from "../pages/HomeMate"
import MyProfile from "../pages/MyProfile"
import MyProfileForm from "../pages/MyProfileForm"
import Signup from "../pages/Signup"
import Agreement from "../pages/Agreement"
import LoginPage from "../pages/LoginPage"
import LogoutPage from "../pages/LogoutPage"

const routes = [
    {path:"/", element: <Home/>},
    {path:"/home-inter", element: <HomeInternational/>},
    {path:"/home-mate", element: <HomeMate/>},
    {path:"/mypage", element: <Mypage/>},
    {path:"/myProfile", element: <MyProfile/>},
    {path:"/myProfileForm", element: <MyProfileForm/>},
    {path:"/sample", element: <Sample/>},
    {path:"/agreement", element: <Agreement/>},
    {path:"/signup", element: <Signup/>},
    {path:"/login", element: <LoginPage/>},
    {path: 'logout', element: <LogoutPage />}
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