// Virtus Fitness - Main JavaScript File
// Practice 1 - Distributed Systems

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    
    // Initialize tooltips
    initializeTooltips();
    
    // Smooth scrolling for anchor links
    initializeSmoothScroll();
    
    // Navbar scroll effect
    initializeNavbarScroll();
    
    // Animation on scroll
    initializeScrollAnimations();
    
    console.log('Virtus Fitness - Website loaded successfully');
});

// Initialize Bootstrap tooltips
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Smooth scroll for anchor links
function initializeSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                e.preventDefault();
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Navbar background change on scroll
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

// Animate elements on scroll
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
    document.querySelectorAll('.service-card, .class-card, .testimonial-card, .stat-card').forEach(element => {
        observer.observe(element);
    });
}

// Alert function for buttons (simulated functionality)
function showAlert(planName) {
    alert('Funcionalidad de "' + planName + '" - Esta es una simulación estática.\nEn la versión completa, aquí se procesaría la selección del plan.');
}

// Form validation simulation
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            alert('Formulario enviado (simulación). En la versión completa, aquí se procesarían los datos.');
            return false;
        });
    }
}

// Simulate login
function simulateLogin() {
    alert('Inicio de sesión (simulación).\nEn la versión completa, aquí se autenticaría al usuario.');
    return false;
}

// Simulate registration
function simulateRegister() {
    alert('Registro (simulación).\nEn la versión completa, aquí se registraría al nuevo usuario.');
    return false;
}

// Mobile menu close on link click
document.querySelectorAll('.navbar-nav .nav-link').forEach(link => {
    link.addEventListener('click', function() {
        const navbarCollapse = document.querySelector('.navbar-collapse');
        if (navbarCollapse.classList.contains('show')) {
            const bsCollapse = new bootstrap.Collapse(navbarCollapse);
            bsCollapse.hide();
        }
    });
});
