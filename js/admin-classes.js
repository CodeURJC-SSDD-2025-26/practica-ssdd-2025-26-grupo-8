// Delete Class Function with Confirmation
function deleteClass(button) {
    const row = button.closest('tr');
    const className = row.querySelector('.class-name span').textContent;

    // Show confirmation dialog
    if (confirm(`¿Estás seguro de que deseas eliminar la clase "${className}"?\n\nEsta acción no se puede deshacer.`)) {
        // Add fade out animation
        row.style.transition = 'all 0.3s ease';
        row.style.opacity = '0';
        row.style.transform = 'translateX(-20px)';

        // Remove row after animation
        setTimeout(() => {
            row.remove();
            updateStats();
            showToast('Clase Eliminada', `La clase "${className}" ha sido eliminada correctamente.`, 'success');
        }, 300);
    }
}

// Edit Class Function
function editClass(button) {
    const row = button.closest('tr');
    const classId = row.querySelector('.class-id').textContent;
    const className = row.querySelector('.class-name span').textContent;

    // In a real application, this would redirect to an edit form with the class data
    showToast('Editar Clase', `Redirigiendo a editar la clase "${className}" (${classId})...`, 'info');

    // Simulate redirect
    setTimeout(() => {
        window.location.href = `class-form.html?edit=${classId}`;
    }, 1500);
}

// Search Functionality
document.getElementById('searchInput').addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    const rows = document.querySelectorAll('#classesTableBody tr');

    rows.forEach(row => {
        const className = row.querySelector('.class-name span').textContent.toLowerCase();
        const instructor = row.querySelector('.instructor-name').textContent.toLowerCase();
        const schedule = row.querySelector('.schedule-badge').textContent.toLowerCase();

        if (className.includes(searchTerm) || instructor.includes(searchTerm) || schedule.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
});

// Update Statistics
function updateStats() {
    const totalClasses = document.querySelectorAll('#classesTableBody tr').length;
    document.querySelector('.stat-card.total h3').textContent = totalClasses;
    document.querySelector('.stat-card.active h3').textContent = totalClasses;
}

// Initialize
console.log('✅ Admin Panel - Classes Management loaded successfully');
