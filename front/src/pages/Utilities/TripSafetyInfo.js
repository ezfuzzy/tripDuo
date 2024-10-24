import React, { useState, useEffect } from "react"
import LoadingAnimation from "../../components/LoadingAnimation"
import axios from "axios"

const axiosInstanceForSafetyInfo = axios.create({
  baseURL: "",
})

function TripSafetyInfo() {
  const [safetyInfo, setSafetyInfo] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [selectedItem, setSelectedItem] = useState(null)
  const [modalOpen, setModalOpen] = useState(false)
  const [searchTerm, setSearchTerm] = useState("")

  useEffect(() => {
    setTimeout(() => {
      setLoading(false)
    }, 700)

    const fetchSafetyInfo = async () => {
      try {
        const url = "https://apis.data.go.kr/1262000/TravelWarningService/getTravelWarningList"
        const queryParams =
          `?${encodeURIComponent("serviceKey")}=${process.env.REACT_APP_GONGGONG_DATA_API_KEY_ENCODING}` +
          `&${encodeURIComponent("numOfRows")}=${encodeURIComponent("141")}` +
          `&${encodeURIComponent("pageNo")}=${encodeURIComponent("1")}`

        const response = await axiosInstanceForSafetyInfo.get(url + queryParams, { responseType: "text" })

        const jsonResponse = JSON.parse(response.data)
        const items = jsonResponse.response.body.items.item

        setSafetyInfo(items)
      } catch (error) {
        console.error("Error fetching data: ", error)
        setError(error.message)
      }
    }

    fetchSafetyInfo()
  }, [])

  const handleCardClick = (item) => {
    setSelectedItem(item)
    setModalOpen(true)
  }

  const closeModal = () => {
    setModalOpen(false)
    setSelectedItem(null)
  }

  const filteredSafetyInfo = safetyInfo.filter((item) =>
    item.countryName.toLowerCase().includes(searchTerm.toLowerCase())
  )

  if (error) {
    return <div className="text-center text-red-500">{error}</div>
  }

  return (
    <div className="p-4">
      <input
        type="text"
        placeholder="나라 이름 입력 ..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="mb-4 p-2 border rounded w-full"
      />
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {loading && <LoadingAnimation />}
        {filteredSafetyInfo.map((item) => (
          <div
            key={item.id}
            className={`rounded-lg shadow-lg p-4 flex items-center cursor-pointer transition-transform transform hover:scale-105 ${
              item.attention || item.attentionPartial
                ? "bg-yellow-300 text-black"
                : item.control || item.controlPartial
                ? "bg-orange-300 text-black"
                : item.banYna || item.banYnPartial
                ? "bg-red-300 text-white"
                : item.limita || item.limitaPartial
                ? "bg-black text-white"
                : "bg-white text-black"
            }`}
            onClick={() => handleCardClick(item)}>
            <img src={item.imgUrl} alt={item.countryName} className="w-16 h-16 object-cover rounded-full mr-4" />
            <div className="flex-1">
              <h2 className="text-lg font-semibold">{item.countryName}</h2>
              <p className="text-gray-600">
                {item.control ||
                  item.controlPartial ||
                  item.banYna ||
                  item.banYnPartial ||
                  item.attention ||
                  item.attentionPartial ||
                  item.limita ||
                  item.limitaPartial ||
                  "정보 없음"}
              </p>
            </div>
          </div>
        ))}
        {modalOpen && selectedItem && (
          <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div className="bg-white rounded-lg p-6 max-w-md w-full">
              <h2 className="text-xl font-semibold">{selectedItem.countryName}</h2>
              <img src={selectedItem.imgUrl} alt={selectedItem.countryName} className="w-auto h-auto rounded my-4" />
              <img src={selectedItem.imgUrl2} alt={selectedItem.countryName} className="w-full h-auto rounded mb-4" />
              {(selectedItem.control || selectedItem.controlPartial) && (
                <div className="mb-4">
                  <h3 className="text-lg font-semibold">여행 유의 정보</h3>
                  {selectedItem.controlNote && (
                    <p>
                      <strong>Note:</strong> {selectedItem.controlNote}
                    </p>
                  )}
                </div>
              )}
              {(selectedItem.attention || selectedItem.attentionPartial) && (
                <div className="mb-4">
                  <h3 className="text-lg font-semibold">여행 자제 정보</h3>
                  {selectedItem.attentionNote && (
                    <p>
                      <strong>Note:</strong> {selectedItem.attentionNote}
                    </p>
                  )}
                </div>
              )}
              {(selectedItem.banYna || selectedItem.banPartial) && (
                <div className="mb-4">
                  <h3 className="text-lg font-semibold">여행 금지 정보</h3>
                  {selectedItem.banNote && (
                    <p>
                      <strong>Note:</strong> {selectedItem.banNote}
                    </p>
                  )}
                </div>
              )}
              {(selectedItem.limita || selectedItem.limitaPartial) && (
                <div className="mb-4">
                  <h3 className="text-lg font-semibold">철수 권고 정보</h3>
                  {selectedItem.limitaNote && (
                    <p>
                      <strong>Note:</strong> {selectedItem.limitaNote}
                    </p>
                  )}
                </div>
              )}
              {selectedItem.wrtDt && (
                <p>
                  <strong>공지일:</strong> {selectedItem.wrtDt}
                </p>
              )}
              {selectedItem.otherInfo && (
                <p>
                  <strong>기타 정보:</strong> {selectedItem.otherInfo}
                </p>
              )}
              <div className="flex justify-end">
                <button className="mt-4 bg-tripDuoGreen text-white rounded px-4 py-2" onClick={closeModal}>
                  닫기
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default TripSafetyInfo
