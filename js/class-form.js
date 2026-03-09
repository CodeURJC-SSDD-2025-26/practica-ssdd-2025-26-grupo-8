// Check if editing existing class
window.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const editId = urlParams.get('edit');

    if (editId) {
        // Change title to Edit mode
        document.getElementById('formTitle').innerHTML = '<i class="bi bi-pencil-square"></i><span>Editar Clase</span>';
        document.getElementById('breadcrumbTitle').textContent = 'Editar Clase';

        // Load existing data (simulated)
        loadClassData(editId);
    }
});

// Simulate loading existing class data
function loadClassData(classId) {
    // In a real application, this would fetch data from a backend
    const sampleData = {
        className: 'CrossFit Avanzado',
        capacity: 25,
        description: 'Clase de alta intensidad que combina fuerza, resistencia y acondicionamiento funcional.',
        instructor: 'Carlos Mendoza',
        duration: '60',
        startTime: '18:00',
        endTime: '19:00',
        difficulty: 'advanced',
        price: '20.00',
        isActive: true
    };

    // Populate form fields
    document.getElementById('className').value = sampleData.className;
    document.getElementById('capacity').value = sampleData.capacity;
    document.getElementById('description').value = sampleData.description;
    document.getElementById('instructor').value = sampleData.instructor;
    document.getElementById('duration').value = sampleData.duration;
    document.getElementById('startTime').value = sampleData.startTime;
    document.getElementById('endTime').value = sampleData.endTime;
    document.getElementById('difficulty').value = sampleData.difficulty;
    document.getElementById('price').value = sampleData.price;
    document.getElementById('isActive').checked = sampleData.isActive;

    // Check some days
    document.getElementById('monday').checked = true;
    document.getElementById('wednesday').checked = true;
    document.getElementById('friday').checked = true;
}

// Handle image upload
function handleImageUpload(event) {
    const file = event.target.files[0];
    if (file) {
        // Validate file type
        if (!file.type.startsWith('image/')) {
            showToast('Error', 'Por favor, selecciona un archivo de imagen válido', 'error');
            return;
        }

        // Validate file size (5MB max)
        if (file.size > 5 * 1024 * 1024) {
            showToast('Error', 'La imagen no debe superar los 5MB', 'error');
            return;
        }

        // Show preview
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('previewImage').src = e.target.result;
            document.getElementById('imagePreview').style.display = 'block';
            document.getElementById('uploadZone').style.display = 'none';
        };
        reader.readAsDataURL(file);

        showToast('Éxito', 'Imagen cargada correctamente', 'success');
    }
}

// Remove uploaded image
function removeImage() {
    document.getElementById('imageInput').value = '';
    document.getElementById('imagePreview').style.display = 'none';
    document.getElementById('uploadZone').style.display = 'block';
}

// Drag and drop handlers
const uploadZone = document.getElementById('uploadZone');

uploadZone.addEventListener('dragover', function(e) {
    e.preventDefault();
    this.classList.add('dragover');
});

uploadZone.addEventListener('dragleave', function(e) {
    e.preventDefault();
    this.classList.remove('dragover');
});

uploadZone.addEventListener('drop', function(e) {
    e.preventDefault();
    this.classList.remove('dragover');

    const file = e.dataTransfer.files[0];
    if (file) {
        const fakeEvent = { target: { files: [file] } };
        handleImageUpload(fakeEvent);
    }
});

// Handle form submission
function handleSubmit(event) {
    event.preventDefault();

    // Validate at least one day is selected
    const selectedDays = document.querySelectorAll('input[name="days"]:checked');
    if (selectedDays.length === 0) {
        showToast('Error', 'Debes seleccionar al menos un día de la semana', 'error');
        return false;
    }

    // Validate times
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;
    if (startTime >= endTime) {
        showToast('Error', 'La hora de fin debe ser posterior a la hora de inicio', 'error');
        return false;
    }

    // Get form data
    const formData = new FormData(event.target);
    const classData = {
        className: formData.get('className'),
        capacity: formData.get('capacity'),
        description: formData.get('description'),
        instructor: formData.get('instructor'),
        duration: formData.get('duration'),
        days: Array.from(selectedDays).map(cb => cb.value),
        startTime: formData.get('startTime'),
        endTime: formData.get('endTime'),
        difficulty: formData.get('difficulty'),
        price: formData.get('price'),
        isActive: formData.get('isActive') === 'on'
    };

    // Check if editing or creating
    const urlParams = new URLSearchParams(window.location.search);
    const isEditing = urlParams.has('edit');

    // Simulate API call
    console.log('Class Data:', classData);

    showToast(
        isEditing ? '¡Clase Actualizada!' : '¡Clase Creada!',
        `La clase "${classData.className}" ha sido ${isEditing ? 'actualizada' : 'creada'} correctamente.`,
        'success'
    );

    // Redirect after 1.5 seconds
    setTimeout(() => {
        window.location.href = 'admin-classes.html';
    }, 1500);

    return false;
}

console.log('✅ Class Form loaded successfully');
