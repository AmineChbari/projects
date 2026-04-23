import Student from '../models/Student.js';
import Group from '../models/Group.js';

/**
 * Récupère tous les étudiants avec leur affectation de groupe
 */
export const getAllStudents = async () => {
    try {
        const students = await Student.find().lean();
        const groups = await Group.find().lean();
        const groupMap = {};
        
        groups.forEach(group => {
            groupMap[group.studentId.toString()] = group.groupNumber;
        });
        
        students.forEach(student => {
            student.groupNumber = groupMap[student._id.toString()] || null;
        });
        return students;
    } catch (error) {
        throw new Error(error.message);
    }
};

/**
 * Crée un nouvel étudiant
 */
export const createStudent = async (req, res) => {
    try {
        const student = new Student(req.body);
        await student.save();
        res.status(201).json({ message: 'Étudiant créé avec succès' });
    } catch (error) {
        if (error.code === 11000) {
            return res.status(400).json({ 
                error: `Duplication: Le numéro étudiant ${req.body.studentNumber} est déjà utilisé dans la base de données.`
            });
        }
        res.status(400).json({ error: error.message });
    }
};

/**
 * Met à jour les informations d'un étudiant
 */
export const updateStudent = async (req, res) => {
    try {
        const { id } = req.params;
        
        if (req.body.studentNumber) {
            const existingStudent = await Student.findOne({ 
                studentNumber: req.body.studentNumber,
                _id: { $ne: id }
            });
            
            if (existingStudent) {
                return res.status(400).json({ 
                    error: `Duplication: Le numéro étudiant ${req.body.studentNumber} est déjà utilisé par un autre étudiant.`
                });
            }
        }
        
        const updatedStudent = await Student.findByIdAndUpdate(
            id, 
            req.body, 
            { new: true, runValidators: true }
        );
        
        if (!updatedStudent) {
            return res.status(404).json({ error: "Étudiant non trouvé" });
        }
        
        res.json({ message: "Étudiant mis à jour avec succès", student: updatedStudent });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

/**
 * Supprime un étudiant et son affectation de groupe si elle existe
 */
export const deleteStudent = async (req, res) => {
    try {
        const { id } = req.params;
        
        const group = await Group.findOneAndDelete({ studentId: id });
        let message = "Étudiant supprimé avec succès";
        
        if (group) {
            message = `Étudiant supprimé avec succès (retiré du groupe ${group.groupNumber})`;
        }
        
        const student = await Student.findByIdAndDelete(id);
        
        if (!student) {
            return res.status(404).json({ error: "Étudiant non trouvé" });
        }
        
        res.json({ message: message });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};