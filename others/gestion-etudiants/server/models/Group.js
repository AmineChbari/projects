import mongoose from 'mongoose';

const groupSchema = new mongoose.Schema({
    studentId: { type: mongoose.Schema.Types.ObjectId, ref: 'Student', required: true },
    groupNumber: { type: Number, required: true, min: 1, max: 6 }
});

export default mongoose.model('Group', groupSchema);