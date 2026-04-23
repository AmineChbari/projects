import { displayMessage } from "./tools.js";

/**
 * Récupère et affiche les étudiants par groupe
 */
const fetchGroupStudents = async () => {
    try {
        const response = await fetch('/groups?json=true');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const groups = await response.json();

        for (let i = 1; i <= 6; i++) {
            const groupList = document.getElementById(`group-${i}-students`);
            if (groupList) {
                groupList.innerHTML = '';
            }
        }

        groups.forEach(group => {
            if (group.studentId && group.groupNumber) {
                const groupList = document.getElementById(`group-${group.groupNumber}-students`);
                if (groupList) {
                    const li = document.createElement('li');
                    li.textContent = `${group.studentId.studentNumber} - ${group.studentId.name} (${group.studentId.surnames})`;
                    const removeButton = document.createElement('button');
                    removeButton.textContent = 'Retirer';
                    removeButton.classList.add('remove');
                    removeButton.onclick = () => removeStudentFromGroup(group._id);
                    li.appendChild(removeButton);
                    groupList.appendChild(li);
                }
            }
        });
    } catch (error) {
        displayMessage('Une erreur est survenue lors du chargement des groupes');
    }
};

/**
 * Récupère la liste des étudiants disponibles pour l'assignation
 */
const fetchStudents = async () => {
    try {
        const response = await fetch('/students?json=true');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const students = await response.json();

        const groupsResponse = await fetch('/groups?json=true');
        const groups = await groupsResponse.json();
        const assignedStudentIds = groups.map(group => group.studentId._id);

        const studentSelector = document.getElementById('student-selector');
        if (studentSelector) {
            studentSelector.innerHTML = '<option value="">Sélectionner un étudiant</option>';
            students.forEach(student => {
                if (!assignedStudentIds.includes(student._id)) {
                    const option = document.createElement('option');
                    option.value = student._id;
                    option.textContent = `${student.studentNumber} - ${student.name}`;
                    studentSelector.appendChild(option);
                }
            });
        }

        const statusMessage = document.getElementById('assignment-status');
        if (statusMessage) {
            const availableCount = students.length - assignedStudentIds.length;
            statusMessage.textContent = `${availableCount} étudiant(s) disponible(s) pour assignment`;
        }
    } catch (error) {
        displayMessage('Une erreur est survenue lors du chargement des étudiants');
    }
};

/**
 * Assigne un étudiant à un groupe
 */
const assignStudentToGroup = async () => {
    const studentId = document.getElementById('student-selector').value;
    const groupNumber = document.getElementById('group-number-selector').value;

    if (!studentId || !groupNumber) {
        displayMessage('Veuillez sélectionner un étudiant et un groupe.', false);
        return;
    }

    try {
        const response = await fetch('/groups/assign', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                studentId,
                groupNumber: parseInt(groupNumber)
            })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Failed to assign student');
        }
        const result = await response.json();

        displayMessage(result.message, true);
        await Promise.all([fetchGroupStudents(), fetchStudents()]);
    } catch (error) {
        displayMessage(error.message || 'Une erreur est survenue', false);
    }
};

/**
 * Retire un étudiant d'un groupe
 */
const removeStudentFromGroup = async (groupId) => {
    try {
        const response = await fetch(`/groups/${groupId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Failed to remove student');
        }
        const result = await response.json();

        displayMessage(result.message, true);
        await Promise.all([fetchGroupStudents(), fetchStudents()]);
    } catch (error) {
        displayMessage(error.message || 'Une erreur est survenue', false);
    }
};

/**
 * Distribue automatiquement les étudiants non assignés
 */
const autoAssignStudents = async () => {
    try {
        const response = await fetch('/groups/auto-assign', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Erreur lors de la distribution automatique');
        }

        const result = await response.json();
        displayMessage(result.message, true);
        await Promise.all([fetchGroupStudents(), fetchStudents()]);
    } catch (error) {
        displayMessage(error.message || 'Une erreur est survenue lors de la distribution automatique', false);
    }
};

/**
 * Vide tous les groupes
 */
const emptyAllGroups = async () => {
    try {
        if (!confirm('Êtes-vous sûr de vouloir vider tous les groupes ? Cette action est irréversible.')) {
            return;
        }
        
        const response = await fetch('/groups/empty-all', {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Erreur lors de la suppression des groupes');
        }

        const result = await response.json();
        displayMessage(result.message, true);
        await Promise.all([fetchGroupStudents(), fetchStudents()]);
    } catch (error) {
        displayMessage(error.message || 'Une erreur est survenue lors de la suppression des groupes', false);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    const addButton = document.getElementById('add-student-btn');
    if (addButton) {
        addButton.addEventListener('click', (e) => {
            e.preventDefault();
            assignStudentToGroup();
        });
    }
    
    const autoAssignButton = document.getElementById('auto-assign-btn');
    if (autoAssignButton) {
        autoAssignButton.addEventListener('click', autoAssignStudents);
    }
    
    const emptyGroupsButton = document.getElementById('empty-groups-btn');
    if (emptyGroupsButton) {
        emptyGroupsButton.addEventListener('click', emptyAllGroups);
    }
    
    fetchGroupStudents();
    fetchStudents();
});