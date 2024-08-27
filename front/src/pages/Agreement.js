import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import TermServiceModal from '../components/TermServiceModal';
import TermPrivacyModal from '../components/TermPrivacyModal';

function Agreement() {
    const [allAgreed, setAllAgreed] = useState(false)
    const [agreements, setAgreements] = useState({
        termsService: false,
        termsPrivacy: false,
        essential: false,
        selective: false
    })
    const { selective, ...rest } = agreements
    const [text, textarea] = useState("약관내용...")
    const navigate = useNavigate()
    const [isDisabled, setIsDisabled] = useState(true);
    const essentialChecked = Object.values(rest).every((value) => value === true)

    const [isTermServiceModalOpen, setTermServiceModalOpen] = useState(false)
    const openTermServiceModal = (e) => {
        e.preventDefault()
        setTermServiceModalOpen(true);
    }
    const closeTermServiceModal = () => setTermServiceModalOpen(false)

    const [isTermPrivacyModalOpen, setTermPrivacyModalOpen] = useState(false)
    const openTermPrivacyModal = (e) => {
        e.preventDefault()
        setTermPrivacyModalOpen(true)
    }
    const closeTermPrivacyModal = () => setTermPrivacyModalOpen(false)

    useEffect(() => {
        setIsDisabled(!essentialChecked)
    }, [essentialChecked])

    const handleAgreementChange = (e) => {
        const { name, checked } = e.target
        setAgreements(prevAgreements => {
            const updatedAgreements = {
                ...prevAgreements,
                [name]: checked
            }
            const allChecked = Object.values(updatedAgreements).every((value) => value === true)
            setAllAgreed(allChecked)
            return updatedAgreements
        })
    }

    const handleAllAgreementsChange = (e) => {
        setAgreements(prevAgreements => {
            const allChecked = !allAgreed
            const updatedAgreements = Object.keys(prevAgreements).reduce((acc, key) => {
                acc[key] = allChecked
                return acc
            }, {})
            setAllAgreed(allChecked)
            return updatedAgreements
        })
    }

    return (
        <div className="container">
            <form action="">
                <div>
                    <label htmlFor="" />
                    <input onChange={handleAllAgreementsChange} type="checkbox" name='checkAll' checked={allAgreed} className='checkAll' />
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
                            <button onClick={openTermServiceModal} className="inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-inset ring-gray-500/10">전체</button>
                            <TermServiceModal isOpen={isTermServiceModalOpen} onClose={closeTermServiceModal} />
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='termsPrivacy' checked={agreements.termsPrivacy} />
                            <em>[필수]</em>
                            <span> 개인정보 수집 및 이용에 대한 안내</span>
                            <button onClick={openTermPrivacyModal} className="inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-inset ring-gray-500/10">전체</button>
                            <TermPrivacyModal isOpen={isTermPrivacyModalOpen} onClose={closeTermPrivacyModal} />
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='essential' checked={agreements.essential} />
                            <em>[필수]</em>
                            <span> xxx</span>
                            <a href="/">전체</a>
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                    <li>
                        <div>
                            <label htmlFor="a"></label>
                            <input onChange={handleAgreementChange} type="checkbox" name='selective' checked={agreements.selective} />
                            <em>[선택]</em>
                            <span> xxx</span>
                            <a href="/">전체</a>
                        </div>
                        <textarea value={text} readOnly />
                    </li>
                </ul>
                <div>
                    <button onClick={() => { navigate("/signup") }} disabled={isDisabled}>제출</button>
                </div>
            </form>
        </div>

    );
}

export default Agreement;