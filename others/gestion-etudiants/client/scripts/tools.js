/**
 * Affiche un message à l'utilisateur (succès ou erreur)
 */
export const displayMessage = (message, isSuccess = false) => {
    const messageDiv = document.getElementById('message');
    
    messageDiv.className = '';
    if (isSuccess) {
        messageDiv.classList.add('success');
    } else {
        messageDiv.classList.add('error');
    }
    
    messageDiv.textContent = message;
    messageDiv.style.display = 'block';
    
    setTimeout(() => {
        messageDiv.textContent = '';
        messageDiv.style.display = 'none';
    }, 5000);
};