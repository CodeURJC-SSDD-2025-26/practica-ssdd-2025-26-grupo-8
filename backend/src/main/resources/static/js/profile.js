// Save Profile Function
function saveProfile(event) {
    event.preventDefault();

    const formData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        birthdate: document.getElementById('birthdate').value,
        gender: document.getElementById('gender').value,
        address: document.getElementById('address').value
    };

    console.log('Saving profile:', formData);

    showToast(
        '¡Perfil Actualizado!',
        'Tus datos personales han sido guardados correctamente.',
        'success'
    );

    // Update sidebar name
    const fullName = `${formData.firstName} ${formData.lastName}`;
    document.querySelector('.profile-name').textContent = fullName;

    return false;
}

// Cancel Reservation Function
function cancelReservation(button) {
    const row = button.closest('tr');
    const className = row.querySelector('.class-badge').textContent.trim();

    if (confirm(`¿Estás seguro de que deseas cancelar tu reserva para "${className}"?`)) {
        row.style.transition = 'all 0.3s ease';
        row.style.opacity = '0';
        row.style.transform = 'translateX(-20px)';

        setTimeout(() => {
            row.remove();
            showToast(
                'Reserva Cancelada',
                `Tu reserva para "${className}" ha sido cancelada.`,
                'success'
            );

            // Check if table is empty
            const tbody = document.querySelector('.reservations-table tbody');
            if (tbody.children.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="5" class="text-center py-5">
                            <div class="empty-state">
                                <i class="bi bi-calendar-x"></i>
                                <h5>No tienes reservas activas</h5>
                                <p>¡Explora nuestras clases y reserva tu próxima sesión!</p>
                                <a href="index.html#classes" class="btn btn-warning mt-3">
                                    Ver Clases Disponibles
                                </a>
                            </div>
                        </td>
                    </tr>
                `;
            }
        }, 300);
    }
}

console.log('✅ User Profile Page loaded successfully');
