import React, { useState } from 'react';

function Agreement() {
    const [allAgreed, setAllAgreed] = useState(false)
    const [agreements, setAgreements] = useState({
        termsService : false,
        termsPrivacy : false,
        essential : false,
        selective : false
    })
    const [text, textarea] = useState("약관내용...")

    const handleAgreementChange = (e) => {
        setAgreements({
            ...agreements,
            [e.target.name] : e.target.checked
        })
        const allChecked = Object.values(agreements).every((value)=>value === true)
        setAllAgreed(allChecked)
    }

    const handleAllAgreementChange = (e) => {
        setAgreements((prevAgreements) =>
            Object.fromEntries(
              Object.keys(prevAgreements).map((key) => [key, e.target.checked])
            )
        );
        setAllAgreed(e.target.checked)
    }

    return (
        <div className="container">
            <form action="">
                <div>
                    <label htmlFor="" />
                    <input onChange={handleAllAgreementChange} type="checkbox" name='checkAll' checked={allAgreed} className='checkAll' />
                    <span>전체 동의하기</span>
                </div>
                <div>이용약관, 개인정보 수집 및 이용, xxx(필수), xxx(선택)에 모두 동의합니다.</div>
                <ul>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='termsService' checked={agreements.termsService} />
                            <em>[필수]</em>
                            <span> tripDuo 이용약관</span>
                            <a href="/">전체</a>
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='termsPrivacy' checked={agreements.termsPrivacy} />
                            <em>[필수]</em>
                            <span> 개인정보 수집 및 이용에 대한 안내(필수)</span>
                            <a href="/">전체</a>
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='essential' checked={agreements.essential} />
                            <em>[필수]</em>
                            <span> xxx(필수)</span>
                            <a href="/">전체</a>
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='selective' checked={agreements.selective} />
                            <em>[선택]</em>
                            <span> xxx선택</span>
                            <a href="/">전체</a>
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                </ul>
                <ul>
                    <li><button>비동의</button></li>
                    <li><button>동의</button></li>
                </ul>
            </form>
        </div>
        
    );
}

export default Agreement;