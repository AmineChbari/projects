import express from 'express';
import mongoose from 'mongoose';
import path from 'path';
import studentsRouter from './routes/students.js';
import groupsRouter from './routes/groups.js';
import config from './config/index.js';

const app = express();
const PORT = config.SERVER_PORT;

// Middleware
app.use(express.json());
app.use(express.static('public'));

// Set Pug as the view engine
app.set('view engine', 'pug');
app.set('views', path.join(path.resolve(), 'views'));

// API Routes
app.use('/groups', groupsRouter);
app.use('/students', studentsRouter);

// Page Routes
app.get('/groups', (req, res) => {
    res.render('groups');
});

app.get('/', (req, res) => {
    res.render('index');
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ error: 'Something went wrong!' });
});

mongoose.connect(config.DB_URI)
    .then(() => console.log(`Connected to MongoDB at ${config.DB_URI}`))
    .catch(err => console.error('Could not connect to MongoDB:', err));

app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));