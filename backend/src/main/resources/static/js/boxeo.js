/* Navbar scroll effect */
window.addEventListener('scroll', function () {
    var navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.classList.add('navbar-scrolled');
    } else {
        navbar.classList.remove('navbar-scrolled');
    }
});

/* ── Star picker ── */
document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('#starPicker i');
    const ratingInput = document.getElementById('reviewRating');

    stars.forEach(star => {
        star.addEventListener('click', function () {
            const val = parseInt(this.dataset.value);
            ratingInput.value = val;
            stars.forEach((s, i) => {
                s.className = i < val ? 'bi bi-star-fill active' : 'bi bi-star';
            });
        });

        star.addEventListener('mouseenter', function () {
            const val = parseInt(this.dataset.value);
            stars.forEach((s, i) => {
                s.className = i < val ? 'bi bi-star-fill active' : 'bi bi-star';
            });
        });
    });

    document.getElementById('starPicker').addEventListener('mouseleave', function () {
        const selected = parseInt(ratingInput.value);
        stars.forEach((s, i) => {
            s.className = i < selected ? 'bi bi-star-fill active' : 'bi bi-star';
        });
    });
});

/* ── Submit review ── */
function submitReview(e) {
    e.preventDefault();

    const name    = document.getElementById('reviewName').value.trim();
    const rating  = parseInt(document.getElementById('reviewRating').value);
    const text    = document.getElementById('reviewText').value.trim();

    if (rating === 0) {
        alert('Por favor selecciona una valoración.');
        return;
    }

    const initials = name.split(' ').map(w => w[0]).join('').substring(0, 2).toUpperCase();
    const months   = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
    const now      = new Date();
    const dateStr  = `${months[now.getMonth()]} ${now.getFullYear()}`;

    let starsHtml = '';
    for (let i = 1; i <= 5; i++) {
        starsHtml += i <= rating
            ? '<i class="bi bi-star-fill"></i>'
            : '<i class="bi bi-star"></i>';
    }

    const card = document.createElement('div');
    card.className = 'review-card';
    card.style.opacity = '0';
    card.style.transform = 'translateY(10px)';
    card.innerHTML = `
        <div class="review-header">
            <div class="review-avatar">${initials}</div>
            <div class="review-meta">
                <span class="review-author">${name}</span>
                <span class="review-date">${dateStr}</span>
            </div>
            <div class="review-stars">${starsHtml}</div>
        </div>
        <p class="review-text">${text}</p>
    `;

    const grid = document.getElementById('reviewsGrid');
    grid.appendChild(card);

    requestAnimationFrame(() => {
        card.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
        card.style.opacity = '1';
        card.style.transform = 'translateY(0)';
    });

    card.scrollIntoView({ behavior: 'smooth', block: 'center' });

    document.getElementById('reviewForm').reset();
    document.getElementById('reviewRating').value = '0';
    document.querySelectorAll('#starPicker i').forEach(s => s.className = 'bi bi-star');
}
