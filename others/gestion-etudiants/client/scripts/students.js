import { displayMessage } from "./tools.js";

let currentEditingStudentId = null;

/**
 * Vide le formulaire et réinitialise le mode d'édition
 */
const clearForm = () => {
    document.getElementById("studentNumber").value = "";
    document.getElementById("name").value = "";
    document.getElementById("surnames").value = "";
    
    currentEditingStudentId = null;
    document.getElementById("add-student-btn").textContent = "Ajouter";
};

/**
 * Remplit le formulaire avec les informations d'un étudiant pour modification
 */
const fillFormForEdit = (student) => {
    document.getElementById("studentNumber").value = student.studentNumber;
    document.getElementById("name").value = student.name;
    document.getElementById("surnames").value = student.surnames;
    
    currentEditingStudentId = student._id;
    document.getElementById("add-student-btn").textContent = "Mettre à jour";
    document.querySelector('.form-section').scrollIntoView({ behavior: 'smooth' });
};

/**
 * Enregistre ou met à jour un étudiant
 */
const saveStudent = async () => {
    try {
        const studentNumber = document.getElementById("studentNumber").value.trim();
        const name = document.getElementById("name").value.trim();
        const surnames = document.getElementById("surnames").value.trim();

        if (!studentNumber || !name || !surnames) {
            displayMessage("Tous les champs sont obligatoires", false);
            return;
        }

        const student = {
            studentNumber: studentNumber,
            name: name,
            surnames: surnames.split(",").map(s => s.trim())
        };

        let url = "/students";
        let method = "POST";
        
        if (currentEditingStudentId) {
            url = `/students/${currentEditingStudentId}`;
            method = "PUT";
        }

        const response = await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(student),
        });

        const result = await response.json();
        
        if (!response.ok) {
            if (response.status === 400 && 
                (result.error.includes("duplicate key") || 
                result.error.includes("duplicated") || 
                result.error.includes("déjà utilisé") ||
                result.error.includes("E11000"))) {
                displayMessage(`⚠️ Erreur: Le numéro étudiant "${studentNumber}" est déjà utilisé. Veuillez en choisir un autre.`, false);
            } else {
                displayMessage(result.error || "Une erreur est survenue", false);
            }
            return;
        }

        displayMessage(currentEditingStudentId ? "Étudiant mis à jour avec succès" : "Étudiant ajouté avec succès", true);
        clearForm();
        fetchStudents();
        
    } catch (error) {
        displayMessage("Une erreur est survenue lors de l'enregistrement de l'étudiant", false);
    }
};

/**
 * Supprime un étudiant
 */
const deleteStudent = async (studentId) => {
    try {
        const response = await fetch(`/students/${studentId}`, {
            method: "DELETE",
        });

        const result = await response.json();
        
        if (!response.ok) {
            displayMessage(result.error || "Une erreur est survenue lors de la suppression", false);
            return;
        }

        displayMessage("Étudiant supprimé avec succès", true);
        fetchStudents();
        
    } catch (error) {
        displayMessage("Une erreur est survenue lors de la suppression de l'étudiant", false);
    }
};

/**
 * Récupère et affiche tous les étudiants
 */
const fetchStudents = async () => {
    try {
        const response = await fetch("/students?json=true");
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const students = await response.json();

        const studentsList = document.getElementById("students-list");
        if (studentsList) {
            studentsList.innerHTML = "";
            students.forEach((student) => {
                const li = document.createElement("li");
                li.classList.add("student-item");

                const studentInfo = document.createElement("div");
                studentInfo.classList.add("student-info");
                studentInfo.innerHTML = `
                    <strong>${student.studentNumber}</strong> - ${student.name} (${student.surnames})
                `;

                const status = document.createElement("span");
                status.classList.add("student-status");
                status.textContent = student.groupNumber
                    ? `Groupe ${student.groupNumber}`
                    : "Pas de groupe !";
                
                const actionsContainer = document.createElement("div");
                actionsContainer.classList.add("student-actions");
                actionsContainer.appendChild(status);
                
                const editButton = document.createElement("button");
                editButton.textContent = "Modifier";
                editButton.classList.add("edit");
                editButton.onclick = () => fillFormForEdit(student);
                actionsContainer.appendChild(editButton);
                
                const deleteButton = document.createElement("button");
                deleteButton.textContent = "Supprimer";
                deleteButton.classList.add("remove");
                deleteButton.onclick = () => deleteStudent(student._id);
                actionsContainer.appendChild(deleteButton);

                li.appendChild(studentInfo);
                li.appendChild(actionsContainer);
                studentsList.appendChild(li);
            });
        }
    } catch (error) {
        displayMessage("Une erreur est survenue lors du chargement des étudiants");
    }
};

document.addEventListener("DOMContentLoaded", () => {
    const addButton = document.getElementById("add-student-btn");
    if (addButton) {
        addButton.addEventListener("click", saveStudent);
    }
    
    const clearButton = document.getElementById("clear-form-btn");
    if (clearButton) {
        clearButton.addEventListener("click", clearForm);
    }
    
    fetchStudents();
});
