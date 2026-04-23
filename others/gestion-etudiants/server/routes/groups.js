import express from 'express';
import { getStudentsByGroup, assignStudentToGroup, removeStudentFromGroup, autoAssignStudents, emptyAllGroups } from '../controllers/groupsController.js';

const router = express.Router();

router.get('/', async (req, res) => {
    try {
        if (req.query.json === 'true') {
            return getStudentsByGroup(req, res);
        } else {
            return res.render('groups');
        }
    } catch (error) {
        if (req.query.json === 'true') {
            return res.status(500).json({ error: error.message });
        } else {
            return res.status(500).send('An error occurred while loading the groups page.');
        }
    }
});

router.post('/auto-assign', autoAssignStudents);
router.delete('/empty-all', emptyAllGroups);
router.post('/assign', assignStudentToGroup);
router.delete('/:id', removeStudentFromGroup);

export default router;