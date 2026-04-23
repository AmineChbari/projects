import mongoose from 'mongoose';

const studentSchema = new mongoose.Schema({
    studentNumber: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    name: {
        type: String,
        required: true,
        uppercase: true,
        trim: true
    },
    surnames: {
        type: [String],
        required: true,
        set: (names) => names.map(name => name.trim().charAt(0).toUpperCase() + name.trim().slice(1).toLowerCase()),
        get: (names) => names.join(', ')
    }
}, { toJSON: { getters: true } });

export default mongoose.model('Student', studentSchema);