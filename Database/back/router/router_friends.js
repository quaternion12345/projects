var express = require('express');
var router = express.Router();
router.use(express.json());

// db.js에서 쿼리 불러오기
const { query } = require('../modules/db'); 

// 유저의 친구목록 조회 or 친구 검색
router.get('/', async (req,res) => {
    const  { My_id, Friend_id } = req.query;
    console.log(`${My_id}, ${Friend_id}`)

    if (Friend_id) {
        // 쿼리로 해당 유저의 특정 친구 반환
        const queryString = `SELECT * from friends where My_id = '${My_id}' AND Friend_id = '${Friend_id}';`;
        const my_friends = await query(queryString);

        if (my_friends.length > 0) {
            res.send(my_friends);
        } else {
            res.send("No that friend");
        }
    } else {
        // 쿼리로 해당 유저의 친구정보 리스트 반환
        const queryString = `SELECT * from friends where My_id = '${My_id}';`;
        const my_friends = await query(queryString);

        if (my_friends.length > 0) {
            res.send(my_friends);
        } else {
            res.send("No friends");
        }
    }
});

// 친구 추가
// post로 friends에 새로운 값 추가
router.post('/', async (req,res) => {
    const { My_id, Friend_id } = req.body;
    console.log(`${My_id}, ${Friend_id}`)

    // 쿼리로 나와 친구의 존재여부 확인
    const isIn = await query(`SELECT * from user where ID = '${My_id}' OR ID = '${Friend_id}';`)
    console.log(isIn);

    if (isIn.length === 2) {
        // 쿼리로 해당 유저의 특정 친구 반환
        const queryString = `INSERT INTO friends VALUES(null, '${My_id}', '${Friend_id}', 1);`;
        await query(queryString);
        res.send(`${My_id} - add friend : ${Friend_id}`);
    } else {
        res.send("No that person");
    }

});

// 변경
// put으로 특정 관계의 f_state의 값 변경
router.put('/:My_id/:Friend_id/:f_state', async (req, res) => {
    const  { My_id, Friend_id, f_state } = req.params;
    const isIn = await query(`SELECT * from friends where My_id = '${My_id}' AND Friend_id = '${Friend_id}';`);
    
    if (isIn.length > 0) {
        // 해당 state 변경
        await query(`UPDATE friends SET f_state = '${f_state}' where My_id = '${My_id}' AND Friend_id = '${Friend_id}';`);
        res.send({ message: `Changed : ${My_id} - ${Friend_id} state : ${f_state}` });
    } else {
        res.status(404).send({ message: `No F_id!! : ${F_id}` });
    }
});

// 특정 친구관계 제거
// delete로 특정 유저-친구 관계 삭제(쿼리)
router.delete('/:My_id/:Friend_id', async (req, res) => {
    const  { My_id, Friend_id } = req.params;
    const isIn = await query(`SELECT * from friends where My_id = '${My_id}' AND Friend_id = '${Friend_id}';`);

    if (isIn.length > 0) {
        // 해당 id의 멤버 삭제
        await query(`DELETE from friends where My_id = '${My_id}' AND Friend_id = '${Friend_id}';`);
        res.send({ message: `Deleted : ${My_id} - ${Friend_id}` });
    } else {
        res.status(404).send({ message: `NOT FOUND : Me(${My_id}) - Friend(${Friend_id})` });
    }
});

module.exports = router;