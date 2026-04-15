// Delete Class Function with Confirmation
// Delete Class Function con Modal Bootstrap personalizado
let rowToDelete = null;

function deleteClass(button) {
    rowToDelete = button.closest('tr');
    const className = rowToDelete.querySelector('.class-name span').textContent;

    // Insertar nombre en el modal y mostrarlo
    document.getElementById('deleteClassName').textContent = `"${className}"`;
    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();

    // Asignar acción al botón de confirmación (evitar duplicados)
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const newConfirmBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

    newConfirmBtn.addEventListener('click', () => {
        modal.hide();

        rowToDelete.style.transition = 'all 0.3s ease';
        rowToDelete.style.opacity = '0';
        rowToDelete.style.transform = 'translateX(-20px)';

        setTimeout(() => {
            const deletedName = rowToDelete.querySelector('.class-name span').textContent;
            rowToDelete.remove();
            rowToDelete = null;
            updateStats();
            showToast('Clase Eliminada', `La clase "${deletedName}" ha sido eliminada correctamente.`, 'success');
        }, 300);
    });
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
