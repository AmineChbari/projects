const darkModeButton = document.getElementById('darkModeButton');
const toggleDarkMode = () => {
  if (document.body.classList.contains('dark-mode')) {
    document.body.classList.remove('dark-mode');
  } else {
    document.body.classList.add('dark-mode');
  }
};

darkModeButton.addEventListener('click', toggleDarkMode);
