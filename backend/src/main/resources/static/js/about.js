// ==================== ABOUT.JS - Virtus Fitness ====================
// JS específico para la página about.html

document.addEventListener('DOMContentLoaded', function () {

    initializeTrainerCards();
    initializeStoryAnimations();
    initializeValueCounters();

    console.log('✅ about.js cargado correctamente');
});


// ==================== TRAINER CARDS - HOVER & MODAL INFO ====================
function initializeTrainerCards() {
    const trainerCards = document.querySelectorAll('.trainer-card');

    trainerCards.forEach(card => {
        // Efecto de elevación al pasar el ratón
        card.addEventListener('mouseenter', function () {
            this.style.transform = 'translateY(-10px)';
            this.style.transition = 'transform 0.3s ease';
        });

        card.addEventListener('mouseleave', function () {
            this.style.transform = 'translateY(0)';
        });
    });
}


// ==================== STORY SECTION - SCROLL ANIMATIONS ====================
function initializeStoryAnimations() {
    const animatedElements = document.querySelectorAll(
        '.story-border, .story-border .d-flex, .hero-about h1, .hero-about p'
    );

    const observer = new IntersectionObserver(function (entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, {
        threshold: 0.15,
        rootMargin: '0px 0px -40px 0px'
    });

    animatedElements.forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(el);
    });

    // Cuando IntersectionObserver añada la clase, aplicar los estilos visibles
    const styleEl = document.createElement('style');
    styleEl.textContent = `
        .fade-in {
            opacity: 1 !important;
            transform: translateY(0) !important;
        }
    `;
    document.head.appendChild(styleEl);
}


// ==================== VALUE ITEMS - COUNTER / STAGGER ANIMATION ====================
function initializeValueCounters() {
    const valueItems = document.querySelectorAll('.story-border .d-flex');

    const observer = new IntersectionObserver(function (entries) {
        entries.forEach((entry, index) => {
            if (entry.isIntersecting) {
                // Entrada escalonada: cada ítem aparece con un pequeño retraso
                setTimeout(() => {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateX(0)';
                }, index * 150);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.2 });

    valueItems.forEach(item => {
        item.style.opacity = '0';
        item.style.transform = 'translateX(-20px)';
        item.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        observer.observe(item);
    });
}


// ==================== TRAINER SECTION - STAGGER ENTRANCE ====================
(function initializeTrainerEntrance() {
    const trainerCols = document.querySelectorAll('.row .col-md-4');

    const observer = new IntersectionObserver(function (entries) {
        entries.forEach((entry, index) => {
            if (entry.isIntersecting) {
                setTimeout(() => {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }, index * 200);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });

    trainerCols.forEach(col => {
        col.style.opacity = '0';
        col.style.transform = 'translateY(30px)';
        col.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(col);
    });
})();
