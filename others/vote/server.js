const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const path = require('path');
import * as msg from './messageConstants.js';

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

let adminAlreadyConnected = false;

// Configure static files middleware
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'client')));

// Serve HTML files
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/html/index.html'));
});

app.get('/admin-vote', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/html/admin.html'));
});

app.get('/votant', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/html/voter.html'));
});

app.get('/about', (req, res) => {
    res.sendFile(path.join(__dirname, 'public/html/about.html'));
});

io.on('connection', (socket) => {
    socket.on(msg.ADMIN, () => {
        if (adminAlreadyConnected) {
            socket.emit(msg.ADMIN_REFUSED);
        } else {
            adminAlreadyConnected = true;
            socket.emit(msg.ADMIN_ACCEPTED);
            socket.emit(msg.WELCOME);
        }
    });

    socket.on(msg.ADMIN_DISCONNECTED, () => {
        adminAlreadyConnected = false;
        io.emit(msg.ADMIN_DISCONNECTED);
    });

    socket.on(msg.VOTANT, () => {
        io.emit(msg.VOTANT);
    });

    socket.on(msg.START_VOTE, (data) => {
        io.emit(msg.START_VOTE, data);
    });

    socket.on(msg.END_VOTE, (results) => {
        io.emit(msg.END_VOTE, results);
    });

    socket.on(msg.NEW_VOTE, (vote) => {
        io.emit(msg.NEW_VOTE, vote);
    });
});

const PORT = process.env.PORT || 8080;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});