import React, { useState } from "react"

const EXCHANGE_API_KEY = "fddfb68fc5e46faf142e2e15" // API 키

const fetchExchangeRate = async (currencyCode) => {
  const url = `https://v6.exchangerate-api.com/v6/${EXCHANGE_API_KEY}/latest/${currencyCode}`
  try {
    const response = await fetch(url)
    const data = await response.json()
    return data.conversion_rates // 환율 데이터 반환
  } catch (error) {
    console.error("환율 정보를 가져오는 중 오류 발생:", error)
    return null // 오류 발생 시 null 반환
  }
}

function ExchangeInfo() {
  const [days, setDays] = useState([]) // 날짜와 항목을 저장하는 상태
  const [selectedCurrency, setSelectedCurrency] = useState("USD") // 기본 화폐 설정

  const addDay = () => {
    setDays([...days, { date: "", items: [] }]) // 새로운 날짜 카드 추가
  }

  const addItem = async (dayIndex, item) => {
    const rates = await fetchExchangeRate(selectedCurrency) // 선택된 화폐의 환율 가져오기
    if (rates) {
      const convertedAmount = Math.round(item.amount * rates["KRW"]) // 원화로 변환 및 반올림
      const newItem = { name: item.name, amount: convertedAmount, currency: "KRW" } // 원화로 변환된 항목
      const newDays = [...days]
      newDays[dayIndex].items.push(newItem) // 해당 날짜에 항목 추가
      setDays(newDays)
    }
  }

  const calculateTotalForDay = (day) => {
    return day.items.reduce((acc, item) => acc + item.amount, 0) // 해당 날짜의 총 금액 계산
  }

  const calculateOverallTotal = () => {
    return days.reduce((acc, day) => acc + calculateTotalForDay(day), 0) // 전체 총 금액 계산
  }

  return (
    <div className="max-w-6xl mx-auto p-6 rounded-lg shadow-md border border-tripDuoGreen bg-white">
      <h1 className="text-3xl font-bold text-center mb-4 text-tripDuoGreen">여행 비용 관리</h1>
      <div className="flex mb-4">
        <select
          value={selectedCurrency}
          onChange={(e) => setSelectedCurrency(e.target.value)}
          className="mr-2 p-2 border border-tripDuoGreen rounded w-full md:w-auto focus:ring focus:ring-tripDuoMint">
          <option value="USD">달러 (USD)</option>
          <option value="EUR">유로 (EUR)</option>
          <option value="JPY">엔 (JPY)</option>
          <option value="GBP">파운드 (GBP)</option>
          <option value="AUD">호주 달러 (AUD)</option>
          <option value="CAD">캐나다 달러 (CAD)</option>
          <option value="CHF">스위스 프랑 (CHF)</option>
          <option value="CNY">중국 위안 (CNY)</option>
          <option value="HKD">홍콩 달러 (HKD)</option>
          <option value="NZD">뉴질랜드 달러 (NZD)</option>
          <option value="SGD">싱가포르 달러 (SGD)</option>
          <option value="SEK">스웨덴 크로나 (SEK)</option>
          <option value="NOK">노르웨이 크로네 (NOK)</option>
          <option value="MXN">멕시코 페소 (MXN)</option>
          <option value="RUB">러시아 루블 (RUB)</option>
          <option value="INR">인도 루피 (INR)</option>
          <option value="BRL">브라질 레알 (BRL)</option>
          <option value="ZAR">남아프리카 랜드 (ZAR)</option>
        </select>
        <button
          onClick={addDay}
          className="p-2 bg-tripDuoMint text-white rounded hover:bg-tripDuoGreen transition duration-300">
          날짜 카드 추가
        </button>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {days.map((day, dayIndex) => (
          <div key={dayIndex} className="p-4 border rounded-lg shadow-md bg-gray-100">
            <input
              type="date"
              value={day.date}
              onChange={(e) => {
                const newDays = [...days]
                newDays[dayIndex].date = e.target.value
                setDays(newDays)
              }}
              className="mb-2 p-2 border border-tripDuoGreen rounded w-full focus:ring focus:ring-tripDuoMint"
            />
            <input
              type="text"
              placeholder="항목 (ex: 의류)"
              className="mb-2 p-2 border border-tripDuoGreen rounded w-full focus:ring focus:ring-tripDuoMint"
              id={`item-${dayIndex}`}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  const itemName = e.target.value
                  const amount = Number(document.getElementById(`amount-${dayIndex}`).value)
                  const item = { name: itemName, amount: amount, currency: selectedCurrency }
                  addItem(dayIndex, item) // 항목 추가
                  e.target.value = "" // 항목 입력 필드 초기화
                  document.getElementById(`amount-${dayIndex}`).value = "" // 금액 입력 필드 초기화
                }
              }}
            />
            <input
              type="number"
              placeholder="금액"
              className="mb-2 p-2 border border-tripDuoGreen rounded w-full focus:ring focus:ring-tripDuoMint"
              id={`amount-${dayIndex}`}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  const itemName = document.getElementById(`item-${dayIndex}`).value
                  const amount = Number(e.target.value)
                  const item = { name: itemName, amount: amount, currency: selectedCurrency }
                  addItem(dayIndex, item) // 항목 추가
                  document.getElementById(`item-${dayIndex}`).value = "" // 항목 입력 필드 초기화
                  e.target.value = "" // 금액 입력 필드 초기화
                }
              }}
            />
            <button
              onClick={() => {
                const itemName = document.getElementById(`item-${dayIndex}`).value
                const amount = Number(document.getElementById(`amount-${dayIndex}`).value)
                const item = { name: itemName, amount: amount, currency: selectedCurrency }
                addItem(dayIndex, item) // 항목 추가
                document.getElementById(`item-${dayIndex}`).value = "" // 항목 입력 필드 초기화
                document.getElementById(`amount-${dayIndex}`).value = "" // 금액 입력 필드 초기화
              }}
              className="p-2 bg-tripDuoMint text-white rounded w-full hover:bg-tripDuoGreen transition duration-300">
              항목 추가
            </button>
            <div className="mt-2 max-h-40 overflow-y-auto border-t pt-2">
              {day.items.map((item, index) => (
                <div key={index} className="p-2 border-b">
                  {item.name} | {Math.round(item.amount)} 원
                </div>
              ))}
            </div>
            <h4 className="mt-2 text-lg font-semibold">당일 총 금액: {Math.round(calculateTotalForDay(day))} 원</h4>{" "}
            {/* 당일 총 금액 표시 */}
          </div>
        ))}
      </div>
      <h2 className="mt-4 text-xl font-semibold text-tripDuoGreen">
        전체 총 금액: {Math.round(calculateOverallTotal())} 원
      </h2>{" "}
      {/* 전체 총 금액 표시 */}
    </div>
  )
}

export default ExchangeInfo
