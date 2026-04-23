import Group from '../models/Group.js';
import Student from '../models/Student.js';

/**
 * Récupère tous les étudiants d'un groupe ou de tous les groupes
 */
export const getStudentsByGroup = async (req, res) => {
    try {
        const { groupNumber } = req.params;
        const query = groupNumber ? { groupNumber: parseInt(groupNumber) } : {};
        
        const groups = await Group.find(query).populate({
            path: 'studentId',
            select: 'studentNumber name surnames'
        });

        res.json(groups);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

/**
 * Assigne un étudiant à un groupe
 */
export const assignStudentToGroup = async (req, res) => {
    try {
        const { studentId, groupNumber } = req.body;

        const student = await Student.findById(studentId);
        if (!student) {
            return res.status(404).json({ error: 'Student not found' });
        }

        const existingGroup = await Group.findOne({ studentId });
        if (existingGroup) {
            return res.status(400).json({ error: 'Student is already assigned to a group' });
        }

        const group = new Group({ studentId, groupNumber });
        await group.save();
        res.status(201).json({ message: 'Student assigned to group successfully' });
    } catch (error) {
        res.status(500).json({ error: 'An unexpected error occurred.' });
    }
};

/**
 * Retire un étudiant d'un groupe
 */
export const removeStudentFromGroup = async (req, res) => {
    try {
        const { id } = req.params;
        const group = await Group.findByIdAndDelete(id);
        
        if (!group) {
            return res.status(404).json({ error: 'Group not found' });
        }
        
        res.json({ message: 'Student removed from group successfully' });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

/**
 * Distribue automatiquement les étudiants non assignés dans les groupes
 */
export const autoAssignStudents = async (req, res) => {
    try {
        const allStudents = await Student.find().lean();
        const existingGroups = await Group.find().lean();
        const assignedStudentIds = existingGroups.map(group => group.studentId.toString());
        
        const unassignedStudents = allStudents.filter(
            student => !assignedStudentIds.includes(student._id.toString())
        );
        
        if (unassignedStudents.length === 0) {
            return res.status(200).json({ 
                message: 'Aucun étudiant disponible à distribuer',
                assignedCount: 0
            });
        }
        
        const groupCounts = {};
        for (let i = 1; i <= 6; i++) {
            groupCounts[i] = 0;
        }
        
        existingGroups.forEach(group => {
            groupCounts[group.groupNumber]++;
        });
        
        const newAssignments = [];
        
        for (const student of unassignedStudents) {
            let minGroup = 1;
            let minCount = groupCounts[1];
            
            for (let i = 2; i <= 6; i++) {
                if (groupCounts[i] < minCount) {
                    minGroup = i;
                    minCount = groupCounts[i];
                }
            }
            
            const newGroup = new Group({
                studentId: student._id,
                groupNumber: minGroup
            });
            
            await newGroup.save();
            newAssignments.push(newGroup);
            groupCounts[minGroup]++;
        }
        
        return res.status(200).json({
            message: `${newAssignments.length} étudiant(s) ont été distribués automatiquement dans les groupes`,
            assignedCount: newAssignments.length
        });
    } catch (error) {
        return res.status(500).json({ error: 'Une erreur est survenue lors de la distribution automatique' });
    }
};

/**
 * Vide tous les groupes en supprimant toutes les affectations
 */
export const emptyAllGroups = async (req, res) => {
    try {
        const result = await Group.deleteMany({});
        
        return res.status(200).json({
            message: `Tous les groupes ont été vidés (${result.deletedCount} étudiants désassignés)`,
            deletedCount: result.deletedCount
        });
    } catch (error) {
        return res.status(500).json({ error: 'Une erreur est survenue lors de la suppression des groupes' });
    }
};