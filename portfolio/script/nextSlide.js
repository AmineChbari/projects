const projectsContainer = document.getElementById('projects-container');
const nextProjectButton = document.getElementById('next-project-button');
const visibleProject = undefined;

const showNextProject = () => {
  const currentIndex = projectsContainer.children.indexOf(visibleProject);
  const nextIndex = (currentIndex + 1) % projectsContainer.children.length;

  // Hide the current project
  visibleProject.classList.add('hidden');

  // Show the next project
  visibleProject = projectsContainer.children[nextIndex];
  visibleProject.classList.remove('hidden');
};

nextProjectButton.addEventListener('click', showNextProject);

// Add click event listeners to each project to allow swipe navigation
projectsContainer.addEventListener('click', (event) => {
  if (event.target.classList.contains('project')) {
    const project = event.target;
    if (visibleProject === project) {
      // Show the next project
      showNextProject();
    } else {
      // Hide the current project
      visibleProject.classList.add('hidden');

      // Show the clicked project
      visibleProject = project;
      visibleProject.classList.remove('hidden');
    }
  }
});

