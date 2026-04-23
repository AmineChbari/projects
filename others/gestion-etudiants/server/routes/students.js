import express from 'express';
import { getAllStudents, createStudent, updateStudent, deleteStudent } from '../controllers/studentsController.js';

const router = express.Router();

router.get('/', async (req, res) => {
    try {
        const students = await getAllStudents();
        if (req.query.json === 'true') {
            res.json(students);
        } else {
            res.render('students', { students });
        }
    } catch (error) {
        if (req.query.json === 'true') {
            res.status(500).json({ error: error.message });
        } else {
            res.status(500).send('An error occurred while loading the students page.');
        }
    }
});

router.post('/', createStudent);
router.put('/:id', updateStudent);
router.delete('/:id', deleteStudent);

export default router;