// Virtus Fitness - Enhanced JavaScript File
// Practice 1 - Distributed Systems

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    
    // Initialize all features
    initializeTooltips();
    initializeSmoothScroll();
    initializeNavbarScroll();
    initializeScrollAnimations();
    initializeStatsCounter();
    initializeBackToTop();
    initializeNewsletterForm();
    
    console.log('✅ Virtus Fitness - Website loaded successfully');
});

// ==================== TOOLTIP INITIALIZATION ====================
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// ==================== SMOOTH SCROLL ====================
function initializeSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                e.preventDefault();
                const navbarHeight = document.querySelector('.navbar').offsetHeight;
                const targetPosition = target.offsetTop - navbarHeight;
                
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
                
                // Close mobile menu if open
                const navbarCollapse = document.querySelector('.navbar-collapse');
                if (navbarCollapse && navbarCollapse.classList.contains('show')) {
                    const bsCollapse = new bootstrap.Collapse(navbarCollapse);
                    bsCollapse.hide();
                }
            }
        });
    });
}

// ==================== NAVBAR SCROLL EFFECT ====================
function initializeNavbarScroll() {
    const navbar = document.querySelector('.navbar');
    
    window.addEventListener('scroll', function() {
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
    });
}

// ==================== SCROLL ANIMATIONS ====================
function initializeScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    // Observe all cards and sections
    document.querySelectorAll('.service-card, .class-card, .testimonial-card, .stat-item').forEach(element => {
        observer.observe(element);
    });
}

// ==================== STATS COUNTER ANIMATION ====================
function initializeStatsCounter() {
    const stats = document.querySelectorAll('.stat-item h2');
    let animated = false;
    
    const animateCounter = (element) => {
        const target = parseInt(element.getAttribute('data-count'));
        const duration = 2000; // 2 seconds
        const steps = 60;
        const increment = target / steps;
        let current = 0;
        
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                element.textContent = target;
                clearInterval(timer);
            } else {
                element.textContent = Math.floor(current);
            }
        }, duration / steps);
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting && !animated) {
                stats.forEach(stat => animateCounter(stat));
                animated = true;
            }
        });
    }, { threshold: 0.5 });
    
    const statsSection = document.querySelector('.stats-section');
    if (statsSection) {
        observer.observe(statsSection);
    }
}

// ==================== BACK TO TOP BUTTON ====================
function initializeBackToTop() {
    const backToTopButton = document.getElementById('backToTop');
    
    if (!backToTopButton) return;
    
    window.addEventListener('scroll', function() {
        if (window.scrollY > 300) {
            backToTopButton.classList.add('show');
        } else {
            backToTopButton.classList.remove('show');
        }
    });
    
    backToTopButton.addEventListener('click', function() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
}

// ==================== NEWSLETTER FORM ====================
function initializeNewsletterForm() {
    const form = document.getElementById('newsletterForm');
    
    if (!form) return;
    
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const emailInput = this.querySelector('input[type="email"]');
        const email = emailInput.value;
        
        // Simulate API call
        showToast('¡Éxito!', `Te has suscrito con ${email}`, 'success');
        
        // Reset form
        emailInput.value = '';
    });
}

// ==================== CLASS RESERVATION SYSTEM ====================
function reserveClass(className) {
    // Check if user is logged in (simulation)
    const isLoggedIn = sessionStorage.getItem('isLoggedIn');
    
    if (!isLoggedIn) {
        showToast(
            'Inicio de sesión requerido',
            'Por favor, inicia sesión para reservar una clase.',
            'warning'
        );
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        return;
    }
    
    // Simulate reservation
    const reservationData = {
        class: className,
        date: new Date().toLocaleDateString('es-ES'),
        time: '10:00',
        instructor: getInstructorForClass(className)
    };
    
    // Store in sessionStorage (in production, this would be an API call)
    let reservations = JSON.parse(sessionStorage.getItem('reservations') || '[]');
    reservations.push(reservationData);
    sessionStorage.setItem('reservations', JSON.stringify(reservations));
    
    showToast(
        '¡Reserva Confirmada!',
        `Tu plaza en ${className} ha sido reservada para ${reservationData.date} a las ${reservationData.time}`,
        'success'
    );
}

// Helper function to get instructor for class
function getInstructorForClass(className) {
    const instructors = {
        'CrossFit': 'Carlos Mendoza',
        'Yoga': 'Ana Martínez',
        'Cycling': 'Luis García',
        'Boxing': 'Miguel Torres',
        'Pilates': 'Laura Sánchez'
    };
    return instructors[className] || 'Instructor Profesional';
}

// ==================== TOAST NOTIFICATIONS ====================
function showToast(title, message, type = 'info') {
    // Create toast container if it doesn't exist
    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        toastContainer.style.zIndex = '9999';
        document.body.appendChild(toastContainer);
    }
    
    // Define colors based on type
    const colors = {
        'success': 'bg-success',
        'warning': 'bg-warning',
        'error': 'bg-danger',
        'info': 'bg-info'
    };
    
    const icons = {
        'success': '✓',
        'warning': '⚠',
        'error': '✗',
        'info': 'ℹ'
    };
    
    // Create toast element
    const toastId = 'toast-' + Date.now();
    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center text-white ${colors[type]} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    <strong>${icons[type]} ${title}</strong><br>
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;
    
    toastContainer.insertAdjacentHTML('beforeend', toastHTML);
    
    // Initialize and show toast
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, {
        autohide: true,
        delay: 4000
    });
    toast.show();
    
    // Remove from DOM after hidden
    toastElement.addEventListener('hidden.bs.toast', function() {
        this.remove();
    });
}

// ==================== FORM VALIDATION ====================
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return;
    
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Get all required fields
        const requiredFields = form.querySelectorAll('[required]');
        let isValid = true;
        
        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                isValid = false;
                field.classList.add('is-invalid');
            } else {
                field.classList.remove('is-invalid');
                field.classList.add('is-valid');
            }
        });
        
        if (isValid) {
            showToast('Formulario Enviado', 'Tus datos han sido enviados correctamente', 'success');
            form.reset();
            requiredFields.forEach(field => field.classList.remove('is-valid'));
        } else {
            showToast('Error', 'Por favor, completa todos los campos requeridos', 'error');
        }
        
        return false;
    });
}

// ==================== SIMULATE LOGIN ====================
function simulateLogin(event) {
    if (event) event.preventDefault();
    
    const username = document.getElementById('username')?.value;
    const password = document.getElementById('password')?.value;
    
    if (!username || !password) {
        showToast('Error', 'Por favor, completa todos los campos', 'error');
        return false;
    }
    
    // Simulate successful login
    sessionStorage.setItem('isLoggedIn', 'true');
    sessionStorage.setItem('username', username);
    
    showToast('¡Bienvenido!', `Has iniciado sesión como ${username}`, 'success');
    
    setTimeout(() => {
        window.location.href = 'index.html';
    }, 1500);
    
    return false;
}

// ==================== SIMULATE REGISTRATION ====================
function simulateRegister(event) {
    if (event) event.preventDefault();
    
    const name = document.getElementById('name')?.value;
    const email = document.getElementById('email')?.value;
    const password = document.getElementById('password')?.value;
    const confirmPassword = document.getElementById('confirmPassword')?.value;
    
    if (!name || !email || !password || !confirmPassword) {
        showToast('Error', 'Por favor, completa todos los campos', 'error');
        return false;
    }
    
    if (password !== confirmPassword) {
        showToast('Error', 'Las contraseñas no coinciden', 'error');
        return false;
    }
    
    // Simulate successful registration
    showToast('¡Registro Exitoso!', `Bienvenido ${name}. Tu cuenta ha sido creada.`, 'success');
    
    setTimeout(() => {
        window.location.href = 'login.html';
    }, 2000);
    
    return false;
}

// ==================== LOGOUT ====================
function logout() {
    sessionStorage.removeItem('isLoggedIn');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('reservations');
    
    showToast('Sesión Cerrada', 'Has cerrado sesión correctamente', 'info');
    
    setTimeout(() => {
        window.location.href = 'index.html';
    }, 1500);
}

// ==================== CHECK LOGIN STATUS ====================
function checkLoginStatus() {
    const isLoggedIn = sessionStorage.getItem('isLoggedIn');
    const username = sessionStorage.getItem('username');
    
    const loginLink = document.querySelector('a[href="login.html"]');
    
    if (isLoggedIn && loginLink) {
        loginLink.textContent = `Hola, ${username}`;
        loginLink.href = '#';
        loginLink.onclick = function(e) {
            e.preventDefault();
            if (confirm('¿Deseas cerrar sesión?')) {
                logout();
            }
        };
    }
}

// Check login status on page load
checkLoginStatus();

// ==================== MOBILE MENU AUTO-CLOSE ====================
document.querySelectorAll('.navbar-nav .nav-link').forEach(link => {
    link.addEventListener('click', function() {
        const navbarCollapse = document.querySelector('.navbar-collapse');
        if (navbarCollapse && navbarCollapse.classList.contains('show')) {
            const bsCollapse = new bootstrap.Collapse(navbarCollapse);
            bsCollapse.hide();
        }
    });
});

// ==================== LOADING SPINNER ====================
window.addEventListener('load', function() {
    const loader = document.getElementById('loader');
    if (loader) {
        loader.style.display = 'none';
    }
});

// ==================== EXPORT FUNCTIONS FOR GLOBAL USE ====================
window.reserveClass = reserveClass;
window.simulateLogin = simulateLogin;
window.simulateRegister = simulateRegister;
window.logout = logout;
window.validateForm = validateForm;
window.showToast = showToast;

console.log('📦 All JavaScript modules loaded successfully');
