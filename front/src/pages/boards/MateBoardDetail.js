import React from 'react';
import { useParams } from 'react-router';

function MateBoardDetail(props) {
    const {num} = useParams()
    return (
        <div>
            <h3>{num}번 게시물 상세 페이지</h3>
        </div>
    );
}

export default MateBoardDetail;