import axios from "axios"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router";
import SavedPlacesKakaoMapComponent from "../../components/SavedPlacesKakaoMapComponent";
import { shallowEqual, useSelector } from "react-redux";
import LoadingAnimation from "../../components/LoadingAnimation";
import KakaoSaveLocationPage from "./KakaoSaveLocationPage";

function MyPlace() {
    //로딩 상태 추가
    const [loading, setLoading] = useState(false)
    // 로그인된 user 정보
    const loggedInUserId = useSelector((state) => state.userData.id, shallowEqual)
    // 저장된 장소 목록의 상태값
    const [placesInfo, setPlacesInfo] = useState([])
    // 기본 좌표는 서울
    const [kakaoMapCenterLocation, setKakaoMapCenterLocation] = useState({ Ma: 37.5665, La: 126.978 });
    // SaveLocationPage 렌더링 여부를 관리
    const [showPlaceSearch, setShowPlaceSearch] = useState(false)
    // 선택된 장소 메모
    const [selectedPlaceMemo, setSelectedPlaceMemo] = useState("")
    // Kakao map 객체를 저장할 상태값
    const [map, setMap] = useState(null);
    //infowWindow 상태값 관리
    const [currentInfoWindow, setCurrentInfoWindow] = useState(null)

    const navigate = useNavigate()

    // 저장된 장소 목록 불러오기
    useEffect(() => {
        // 로딩 애니메이션을 0.7초 동안만 표시
        setLoading(true)
        setTimeout(() => {
            setLoading(false)
        }, 700)
        axios.get(`/api/v1/users/${loggedInUserId}/trips/saved-places`, {
            params: {
                di: "Domestic"
            }
        })
            .then(res => {
                console.log(res.data)
                const savedPlacesList = res.data
                if (savedPlacesList.length > 0) {
                    setPlacesInfo(savedPlacesList)
                    setKakaoMapCenterLocation({
                        Ma: savedPlacesList[0].place.latitude,
                        La: savedPlacesList[0].place.longitude
                    })
                } else {
                    // 장소가 없을 때 기본 상태 유지
                    setPlacesInfo([]);
                }
            })
            .catch(error => {
                alert("저장된 장소를 불러오지 못했습니다.")
                console.log(error)
            })
    }, [])

    // placesInfo에 있는 데이터를 SavedPlacesKakaoMapComponent에 전달하기 적절하게 변환
    const transformedData = placesInfo.map(item => {
        return {
            id: item.place.mapPlaceId,
            phone: item.place.phone,
            dayIndex: 0,  // 주어진 데이터에서는 dayIndex가 없으므로, 기본값으로 0을 설정합니다.
            position: {
                La: item.place.longitude,
                Ma: item.place.latitude
            },
            placeMemo: item.userMemo || "",  // userMemo를 placeMemo로 매핑하고, 없을 경우 빈 문자열로 설정합니다.
            place_url: item.place.placeUrl,
            placeIndex: item.place.id,
            place_name: item.place.placeName,
            address_name: item.place.addressName,
            category_name: item.place.categoryName,
            road_address_name: item.place.roadAddressName,
            category_group_code: item.place.categoryGroupCode,
            category_group_name: item.place.categoryGroupName
        };
    });

    // 장소 검색 버튼 클릭 시 장소 검색 컴포넌트를 열기 위한 핸들러
    const handleOpenPlaceSearch = () => {
        setShowPlaceSearch(true);
    };

    // 장소 저장 후 호출되는 함수
    const handleSavePlace = (place) => {
        const placeInfo = {
            addressName: place.address_name,
            categoryGroupCode: place.category_group_code,
            categoryGroupName: place.category_group_name,
            categoryName: place.category_name,
            mapPlaceId: place.id,
            phone: place.phone,
            placeName: place.place_name,
            placeUrl: place.place_url,
            roadAddressName: place.road_address_name,
            latitude: parseFloat(place.y),  // y를 latitude로 변환
            longitude: parseFloat(place.x), // x를 longitude로 변환
            userId: loggedInUserId,
            userMemo: place.placeMemo || "",
            di: "Domestic"
        }

        axios.post(`/api/v1/users/${loggedInUserId}/trips/saved-places`, placeInfo)
            .then(res => {

                // 장소 저장 후 placesInfo 상태 업데이트
                setPlacesInfo(prevPlacesInfo => [
                    ...prevPlacesInfo,
                    res.data
                ])

                // 저장된 장소의 좌표로 지도 중심 이동
                setKakaoMapCenterLocation({ Ma: placeInfo.latitude, La: placeInfo.longitude })

                // 메모 저장 시 해당 장소 메모도 업데이트
                setSelectedPlaceMemo(placesInfo.userMemo)

                // 장소 저장 후 검색 컴포넌트를 닫습니다.
                setShowPlaceSearch(false)
            }).catch(error => {
                alert("장소를 저장하지 못했습니다.")
                console.log(error)
            })
    };

    // 장소 클릭 시 맵 중심 이동, 장소 메모 표시, 인포윈도우 표시
    const handlePlaceClick = (placeItem) => {
        if (placeItem.place.latitude && placeItem.place.longitude) {
            setKakaoMapCenterLocation({ Ma: placeItem.place.latitude, La: placeItem.place.longitude });

            // 기존의 열린 infoWindow가 있다면 닫기
            if (currentInfoWindow) {
                currentInfoWindow.close();
            }

            // 새로운 infoWindow 생성
            const infoWindow = new window.kakao.maps.InfoWindow({
                content: `
                    <div style="padding:10px;font-size:12px;display:flex;flex-direction:column;align-items:flex-start;width:100%;max-width:600px;height:100%;">
                        <div style="margin-bottom: 8px; display: flex; justify-content: space-between; width: 100%;">
                            <strong>${placeItem.place.placeName}</strong>
                        </div>
                        <div style="margin-bottom: 8px;">${placeItem.place.addressName}</div>
                        <div style="margin-bottom: 8px;">전화번호: ${placeItem.place.phone || '정보 없음'}</div>
                        <div style="margin-bottom: 16px;"><a href="${placeItem.place.placeUrl}" target="_blank">장소 링크</a></div>
                    </div>
                `,
            });

            // 새로운 infoWindow 열기
            const markerPosition = new window.kakao.maps.LatLng(placeItem.place.latitude, placeItem.place.longitude);
            infoWindow.open(map, new window.kakao.maps.Marker({ position: markerPosition, map: map }));

            // 새로운 infoWindow를 상태로 저장
            setCurrentInfoWindow(infoWindow);
        }

        setSelectedPlaceMemo(placeItem.userMemo || "메모가 없습니다.");
    };

    return (
        <div className="container mx-auto p-4 max-w-[1024px] bg-gradient-to-r from-green-100 to-white">
            {/* 로딩 애니메이션 */}
            {loading && <LoadingAnimation />}
            <div className="flex flex-col items-center min-h-screen bg-white rounded-md">
                <div className="w-full max-w-screen-xl mx-auto p-4">
                    <div className="flex justify-between">
                        <button
                            onClick={() => navigate(`/users/${loggedInUserId}`)}
                            className="text-tripDuoGreen border border-tripDuoGreen hover:bg-tripDuoMint focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-full text-sm px-4 py-2.5 mr-4 text-center">
                            MyPage
                        </button>
                        <button
                            onClick={() => navigate("/private/myPlace2")}
                            className="bg-tripDuoMint font-bold text-white px-4 py-2 text-sm rounded-md shadow-md hover:bg-tripDuoGreen transition-all duration-300 flex items-center">
                            해외로
                        </button>
                    </div>
                    {/* 상단 헤더 */}
                    <h1 className="text-3xl font-bold mb-4 text-center">마이 플레이스(국내)</h1>

                    {/* 장소 검색 버튼 */}
                    <div className="flex justify-start mb-2">
                        <button
                            onClick={handleOpenPlaceSearch}
                            className="border border-blue-900 hover:bg-blue-100 text-blue-900 px-2 py-1 rounded-md shadow-lg transition duration-300 text-sm font-bold"
                        >
                            장소 추가
                        </button>
                    </div>

                    {/* 장소 목록 및 지도, 선택된 장소 메모 */}
                    <div className="flex space-x-4">
                        {/* 장소 목록 영역 */}
                        <div className="w-1/4 bg-white p-4 rounded-lg shadow-md max-h-[500px] overflow-y-auto top-0">
                            <h2 className="font-bold mb-4">저장된 장소 목록</h2>
                            <ul>
                                {placesInfo.map((placeItem, index) => (
                                    <li
                                        key={index}
                                        className="mb-2 cursor-pointer hover:text-blue-600"
                                        onClick={() => {
                                            handlePlaceClick(placeItem)
                                        }}
                                    >
                                        {index + 1}. {placeItem.place.placeName}
                                    </li>
                                ))}
                            </ul>
                        </div>

                        <div className="w-3/4 flex flex-col space-y-4">
                            {/* 지도 영역 */}
                            <div className="flex-grow bg-white rounded-lg shadow-md">
                                <SavedPlacesKakaoMapComponent
                                    savedPlaces={transformedData}
                                    centerLocation={kakaoMapCenterLocation}
                                    onMapReady={setMap}
                                />
                            </div>

                            {/* 선택된 장소 메모 표시 */}
                            {selectedPlaceMemo && (
                                <div className="p-4 bg-white rounded-lg shadow-md">
                                    <h3 className="font-bold mb-2">장소 메모</h3>
                                    <p>{selectedPlaceMemo}</p>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* KakaoSaveLocationPage 모달 */}
                    {showPlaceSearch && (
                        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                            <div className="bg-white p-4 rounded-lg shadow-lg w-full max-w-xl">
                            <div className="flex justify-end">
                                    <button
                                        onClick={() => setShowPlaceSearch(false)}
                                        className="text-red-600 hover:text-red-800 text-3xl font-bold"
                                        aria-label="Close">
                                        &times;
                                    </button>
                                </div>
                                <KakaoSaveLocationPage onSave={handleSavePlace} />
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default MyPlace;