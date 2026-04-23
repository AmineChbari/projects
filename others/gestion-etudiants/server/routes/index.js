import express from 'express';
import studentsRouter from './students.js';
import groupsRouter from './groups.js';
import errorRouter from './error.js';

const router = express.Router();

router.use('/students', studentsRouter);
router.use('/groups', groupsRouter);
router.use('*', errorRouter);

export default router;