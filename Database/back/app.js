var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var createError = require('http-errors');
var http = require('http')
const express = require('express');
const db = require('./modules/db');
const app = express();
const friendsRouter = require('./router/router_friends');
const cors = require('cors');
const corsOptions = {
    origin: "http://localhost:3000",
    credentials: true
}
app.use(cors(corsOptions));

app.get('/api/', (req, res) => {
    res.send('안녕 백엔드');
});

// indexRouter process HTTP request of http://127.0.0.1/~ 
var indexRouter = require('./router/index');

// use router module
var userRouter = require('./router/user');
var chatlistRouter = require('./router/chatlist');

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');
// app.get('/', (req, res) => {
//     res.header("Access-Control-Allow-Origin", "*")
// })
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


app.use('/', indexRouter);
app.use('/user', userRouter);
app.use('/chatlist', chatlistRouter);
app.use('/api/friends/', friendsRouter);

app.listen(3085, () => {
    console.log('백엔드 서버 ${3085}번 포트에서 작동중.');
});

module.exports = app;
// const app = require('express')();
// const server = require('http').createServer(app);
// const io = require('socket.io')(server);

// const users = require('../utils/users')();
// const Message = require('../utils/message')();

// socket.on("createMessage", ({ id, msg }) => {
//     const user = usersDB.getUser(id);
//     if (user) {
//       io.to(user.room).emit("newMessage", new Message(user.name, msg, id));
//     }
//   });