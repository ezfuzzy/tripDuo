import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'keen-slider/keen-slider.min.css';
import { useKeenSlider } from 'keen-slider/react';
import '../css/Home.css';

function HomeAbroad() {
    const navigate = useNavigate();
    const [selectedOption, setSelectedOption] = useState("해외");
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [sliderRef] = useKeenSlider({
        loop: true,
        slides: {
            perView: 3,
            spacing: 0,
        },
        dragSpeed: 0.5,
    });

    const handleSelect = (eventKey) => {
        setSelectedOption(eventKey === "Home" ? "국내" : "해외");
        navigate(eventKey === "Home" ? "/" : "/home-abroad");
        setDropdownOpen(false);
    };

    const navigateToMate = (destination) => {
        navigate(`/posts/mate?di=${destination}`);
    };

    const navigateToPage = (path) => {
        navigate(path);
    };

    const navigateToLogin = () => {
        navigate("/login"); // 로그인 페이지로 이동
    };

    // 토큰 확인 (예시로 'token' 키를 사용)
    const token = localStorage.getItem('token');

    return (
        <div className="container mx-auto px-8 bg-white min-h-screen">
            <div className="flex justify-end pt-4 relative">
                <button
                    className="inline-flex justify-center w-24 rounded-md border border-gray-300 shadow-md px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-100"
                    onClick={() => setDropdownOpen(!dropdownOpen)}
                >
                    {selectedOption}
                </button>
                {dropdownOpen && (
                    <div className="absolute right-0 z-10 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
                        <div className="py-1" role="menu" aria-orientation="vertical">
                            <button onClick={() => handleSelect("Home")} className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem">국내</button>
                            <button onClick={() => handleSelect("international")} className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem">해외</button>
                        </div>
                    </div>
                )}
            </div>

            <div className="my-12">
                <header className="py-8 text-center">
                    <h1 className="text-3xl font-bold text-green-600">해외 여행</h1>
                    <p className="mt-2 text-gray-600">다양한 해외 여행 정보를 만나보세요!</p>
                </header>
                <div ref={sliderRef} className="keen-slider w-full my-6">
                    {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((item) => (
                        <div key={item} className="keen-slider__slide flex justify-center">
                            <div className="min-w-[350px] h-[450px] bg-white shadow-lg rounded-lg overflow-hidden">
                                <img src={`https://picsum.photos/350/450?random=${item}`} alt={`여행지 ${item}`} className="w-full h-full object-cover" />
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="my-12 h-16" />

            <div className="flex items-center justify-between mb-8 p-4 bg-green-300 rounded-lg shadow-lg">
                <h3 className="text-xl font-semibold text-white">여행메이트 찾기</h3>
                <div className="flex space-x-4">
                    <div
                        className="relative min-w-[80px] cursor-pointer"
                        onClick={() => navigateToMate("Domestic")}
                    >
                        <img src="https://picsum.photos/80/80?random=1" alt="국내" className="rounded-full border-2 border-white shadow-md" />
                        <span className="absolute inset-0 flex items-center justify-center text-white text-lg font-bold">국내</span>
                    </div>
                    <div
                        className="relative min-w-[80px] cursor-pointer"
                        onClick={() => navigateToMate("International")}
                    >
                        <img src="https://picsum.photos/80/80?random=2" alt="해외" className="rounded-full border-2 border-white shadow-md" />
                        <span className="absolute inset-0 flex items-center justify-center text-white text-lg font-bold">해외</span>
                    </div>
                </div>
            </div>

            <div className="my-12 h-16" />

            <div className="my-12">
                <h3 className="text-xl font-semibold mb-4 text-green-600">해외 여행 인기 메이트</h3>
                {!token && (
                    <p className="text-gray-600 text-sm text-left mb-4">
                        <span className='cursor-pointer' onClick={navigateToLogin}>로그인</span> 하시면 고객님에게 알맞는 메이트를 추천해드립니다.
                    </p>
                )}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                    {[1, 2, 3].map((mate) => (
                        <div key={mate} className="relative bg-white shadow-lg rounded-lg overflow-hidden flex flex-col transition-transform">
                            <div className="h-32 w-full bg-gray-300" />
                            <div className="flex justify-center -mt-16">
                                <img src={`https://picsum.photos/80/80?random=${mate + 3}`} alt={`메이트 ${mate}`} className="rounded-full border-4 border-white shadow-md" />
                            </div>
                            <div className="p-4 text-center flex-grow">
                                <h4 className="font-bold text-lg">{`메이트 ${mate}`}</h4>
                                <p className="text-gray-600">여행을 좋아하는 메이트입니다.</p>
                                <p className="text-gray-500">좋아요: {Math.floor(Math.random() * 100)}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="my-12 h-16" />

            <div className="my-12">
                <h3 className="text-xl font-semibold mb-4 text-green-600">해외 추천 여행지(임시)</h3>
                {!token && (
                    <p className="text-gray-600 text-sm text-left mb-4">
                        <span className='cursor-pointer' onClick={navigateToLogin}>로그인</span> 하시면, 고객님을 위한 여행지를 추천해드립니다.
                    </p>
                )}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                    {[1, 2, 3, 4].map((product) => (
                        <div key={product} className="bg-white shadow-lg rounded-lg overflow-hidden">
                            <img src={`https://picsum.photos/200/150?random=${product + 5}`} alt={`여행지 ${product}`} className="w-full h-32 object-cover" />
                            <div className="p-4">
                                <h4 className="font-bold">{`여행지 ${product}`}</h4>
                                <p className="text-gray-600">여행지 이름</p>
                                <button className="mt-2 bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-700 transition duration-300">
                                    더 보기
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="my-12 h-16" />

            {/* 추가된 카드들 */}
            <div className="my-12">
                <h3 className="text-xl font-semibold mb-4 text-green-600">여행을 쉽고 간편하게</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                    <div className="bg-white shadow-lg rounded-lg overflow-hidden">
                        <img src="https://picsum.photos/200/150?random=11" alt="여행 체크리스트" className="w-full h-32 object-cover" />
                        <div className="p-4">
                            <h4 className="font-bold text-lg">여행 체크리스트</h4>
                            <button onClick={() => navigateToPage("/checklist")} className="mt-2 bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-700 transition duration-300">
                                작성하기
                            </button>
                        </div>
                    </div>
                    <div className="bg-white shadow-lg rounded-lg overflow-hidden">
                        <img src="https://picsum.photos/200/150?random=12" alt="환율 정보" className="w-full h-32 object-cover" />
                        <div className="p-4">
                            <h4 className="font-bold text-lg">환율 정보</h4>
                            <button onClick={() => navigateToPage("/exchange")} className="mt-2 bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-700 transition duration-300">
                                살펴보기
                            </button>
                        </div>
                    </div>
                    <div className="bg-white shadow-lg rounded-lg overflow-hidden">
                        <img src="https://picsum.photos/200/150?random=13" alt="여행 경비 계산기" className="w-full h-32 object-cover" />
                        <div className="p-4">
                            <h4 className="font-bold text-lg">여행 경비 계산기</h4>
                            <button onClick={() => navigateToPage("/calculator")} className="mt-2 bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-700 transition duration-300">
                                계산하기
                            </button>
                        </div>
                    </div>
                    <div className="bg-white shadow-lg rounded-lg overflow-hidden">
                        <img src="https://picsum.photos/200/150?random=14" alt="여행 플레너" className="w-full h-32 object-cover" />
                        <div className="p-4">
                            <h4 className="font-bold text-lg">여행 플레너</h4>
                            <button onClick={() => navigateToPage("/planner")} className="mt-2 bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-700 transition duration-300">
                                계획짜기
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomeAbroad;
