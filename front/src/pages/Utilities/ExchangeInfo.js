import React, { useState, useEffect } from 'react';

function ExchangeInfo() {
    const [fromCurrency, setFromCurrency] = useState('USD');
    const [toCurrency, setToCurrency] = useState('EUR');
    const [exchangeRate, setExchangeRate] = useState(null);

    useEffect(() => {
        // API로부터 환율 정보 불러오기 (실제 API 필요)
        const fetchExchangeRate = async () => {
            // 여기서는 실제 API 호출 대신 임시 데이터를 사용합니다.
            const mockExchangeRate = 0.85; // 예시로 USD -> EUR 환율
            setExchangeRate(mockExchangeRate);
        };
        fetchExchangeRate();
    }, [fromCurrency, toCurrency]);

    return (
        <div>
            <h1>Exchange Information</h1>
            <label>
                From:
                <input value={fromCurrency} onChange={(e) => setFromCurrency(e.target.value)} />
            </label>
            <br />
            <label>
                To:
                <input value={toCurrency} onChange={(e) => setToCurrency(e.target.value)} />
            </label>
            <br />
            <p>Exchange Rate: {exchangeRate ? `${exchangeRate}` : 'Loading...'}</p>
        </div>
    );
}

export default ExchangeInfo;
