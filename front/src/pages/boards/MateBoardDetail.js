import React, { useEffect } from 'react';
import { useParams } from 'react-router';
import { Link } from 'react-router-dom';

function MateBoardDetail(props) {

    const {num} = useParams()

    useEffect(()=>{

    },[])
    return (
        <>
            <Link to={"/mateBoard"}>Mate</Link>
            <h3>{num}번 게시물 상세 페이지</h3>
        </>
    );
}

export default MateBoardDetail;