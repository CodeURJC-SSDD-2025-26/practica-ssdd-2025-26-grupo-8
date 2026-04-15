// Profile photo preview
document.getElementById('profilePhoto').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) {
        alert('Por favor selecciona un archivo de imagen válido');
        e.target.value = '';
        return;
    }
    if (file.size > 5 * 1024 * 1024) {
        alert('La imagen es demasiado grande. Tamaño máximo: 5MB');
        e.target.value = '';
        return;
    }
    const reader = new FileReader();
    reader.onload = function(event) {
        const photoPreview = document.getElementById('photoPreview');
        photoPreview.style.backgroundImage = `url(${event.target.result})`;
        photoPreview.innerHTML = '';
        photoPreview.classList.add('has-image');
    };
    reader.readAsDataURL(file);
});

// Password strength indicator
document.getElementById('password').addEventListener('input', function(e) {
    const password = e.target.value;
    const strengthBar = document.getElementById('strengthBar');
    let strength = 0;
    if (password.length >= 6) strength++;
    if (password.length >= 10) strength++;
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[^a-zA-Z0-9]/.test(password)) strength++;
    strengthBar.className = 'strength-bar';
    if (password.length === 0) return;
    if (strength <= 2) strengthBar.classList.add('weak');
    else if (strength <= 4) strengthBar.classList.add('medium');
    else strengthBar.classList.add('strong');
});

// Password confirmation validation
document.getElementById('confirmPassword').addEventListener('input', function(e) {
    const password = document.getElementById('password').value;
    if (e.target.value && password !== e.target.value) {
        e.target.setCustomValidity('Las contraseñas no coinciden');
    } else {
        e.target.setCustomValidity('');
    }
});
