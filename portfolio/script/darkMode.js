const darkModeButton = document.getElementById("darkModeButton");
const body = document.body;

darkModeButton.addEventListener("click", () => {
body.classList.toggle("dark-mode");
});