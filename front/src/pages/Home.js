import React, { useState } from 'react';
import { Container, Row, Col, Button, Card, Form, ListGroup } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import '../css/Home.css';

function Home() {
    const navigate = useNavigate();

    // 상태 관리
    const [searchParams, setSearchParams] = useState({
        location: '',
        dateRange: '',
        travelStyle: '',
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setSearchParams((prev) => ({ ...prev, [name]: value }));
    };

    const handleSearch = () => {
        // 검색 로직을 여기에 추가하세요.
        console.log('Searching with params:', searchParams);
        navigate('/search-results', { state: searchParams }); // 검색 결과 페이지로 이동
    };

    return (
        <Container>
            {/* 메인 검색 바 */}
            <Row className="my-4">
                <Col md={8} className="mx-auto">
                    <h2>여행 메이트 찾기</h2>
                    <Form>
                        <Form.Group controlId="formLocation">
                            <Form.Label>여행지</Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder="여행지를 입력하세요..." 
                                name="location"
                                value={searchParams.location}
                                onChange={handleInputChange}
                            />
                        </Form.Group>
                        <Form.Group controlId="formDateRange" className="mt-3">
                            <Form.Label>여행 기간</Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder="여행 기간을 입력하세요..." 
                                name="dateRange"
                                value={searchParams.dateRange}
                                onChange={handleInputChange}
                            />
                        </Form.Group>
                        <Form.Group controlId="formTravelStyle" className="mt-3">
                            <Form.Label>여행 스타일</Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder="여행 스타일을 입력하세요..." 
                                name="travelStyle"
                                value={searchParams.travelStyle}
                                onChange={handleInputChange}
                            />
                        </Form.Group>
                        <Button variant="primary" className="mt-3" onClick={handleSearch}>
                            검색
                        </Button>
                    </Form>
                </Col>
            </Row>

            {/* 추천 여행 메이트 */}
            <Row className="my-4">
                <Col>
                    <h3>추천 여행 메이트</h3>
                    <Row>
                        {/* 예시로 3명의 여행 메이트 카드 표시 */}
                        {[1, 2, 3].map((mate) => (
                            <Col md={4} key={mate} className="mb-3">
                                <Card>
                                    <Card.Img variant="top" src={`https://picsum.photos/200/150?random=${mate}`} />
                                    <Card.Body>
                                        <Card.Title>여행 메이트 {mate}</Card.Title>
                                        <Card.Text>
                                            이 메이트는 {mate}번 여행 스타일을 선호합니다.
                                        </Card.Text>
                                        <Button variant="primary">프로필 보기</Button>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Col>
            </Row>

            {/* 인기 여행지 섹션 */}
            <Row className="my-4">
                <Col>
                    <h3>인기 여행지</h3>
                    <Row>
                        {[1, 2, 3].map((dest) => (
                            <Col md={4} key={dest} className="mb-3">
                                <Card>
                                    <Card.Img variant="top" src={`https://picsum.photos/200/150?random=${dest + 3}`} />
                                    <Card.Body>
                                        <Card.Title>여행지 {dest}</Card.Title>
                                        <Card.Text>
                                            이 여행지는 {dest}번 여행 스타일과 잘 어울립니다.
                                        </Card.Text>
                                        <Button variant="primary">더 알아보기</Button>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Col>
            </Row>

            {/* 사용자 리뷰 및 성공 사례 */}
            <Row className="my-4">
                <Col>
                    <h3>사용자 리뷰 및 성공 사례</h3>
                    <ListGroup>
                        <ListGroup.Item>리뷰 1: 이 사이트를 통해 최고의 여행 메이트를 만났어요!</ListGroup.Item>
                        <ListGroup.Item>리뷰 2: 함께한 여행이 잊을 수 없는 추억이 되었어요.</ListGroup.Item>
                        <ListGroup.Item>리뷰 3: 다음 여행도 여기서 메이트를 구할 거예요!</ListGroup.Item>
                    </ListGroup>
                </Col>
            </Row>

            {/* 뉴스레터 가입 */}
            <Row className="my-4">
                <Col md={8} className="mx-auto">
                    <h3>뉴스레터 가입</h3>
                    <Form>
                        <Form.Group controlId="formNewsletter">
                            <Form.Label>이메일 주소</Form.Label>
                            <Form.Control type="email" placeholder="이메일을 입력하세요..." />
                        </Form.Group>
                        <Button variant="primary" className="mt-3">가입하기</Button>
                    </Form>
                </Col>
            </Row>

            {/* 회원가입 및 로그인 섹션 */}
            <Row className="my-4">
                <Col>
                    <h3>회원가입하지 않으셨다면?</h3>
                    <Button variant="success" onClick={() => navigate('/signup')}>회원가입</Button>
                    <Button variant="secondary" className="ml-2" onClick={() => navigate('/loginlist')}>로그인</Button>
                </Col>
            </Row>
        </Container>
    );
}

export default Home;
