/* ── Base prices (adult, with permanencia, no familia) ── */
var BASE_PRICES = {
    basic:   29.90,
    premium: 49.90,
    elite:   79.90
};

/* ── Modifiers ── */
var MODIFIERS = {
    sinPermanencia: 1.15,   // +15 % without commitment
    familiaNumerosa: 0.85,  // -15 % family discount
    nino: 0.75              // -25 % youth rate
};

/* ── Current filter state ── */
var filters = {
    permanencia: 'con',
    familia: 'no',
    edad: 'adulto'
};

/* ── Update prices in DOM ── */
function updatePrices() {
    var plans = ['basic', 'premium', 'elite'];
    plans.forEach(function (plan) {
        var price = BASE_PRICES[plan];

        /* Apply age modifier first */
        if (filters.edad === 'nino') {
            price *= MODIFIERS.nino;
        }

        /* Apply permanencia modifier */
        if (filters.permanencia === 'sin') {
            price *= MODIFIERS.sinPermanencia;
        }

        /* Apply familia numerosa modifier */
        if (filters.familia === 'si') {
            price *= MODIFIERS.familiaNumerosa;
        }

        /* Round to 2 decimals */
        price = Math.round(price * 100) / 100;
        var parts = price.toFixed(2).split('.');
        var whole = parts[0];
        var cents = ',' + parts[1];

        /* Animate the price change */
        var priceEl = document.getElementById('price-' + plan);
        var centsEl = document.getElementById('cents-' + plan);

        priceEl.textContent = whole;
        centsEl.textContent = cents;

        priceEl.classList.remove('updated');
        void priceEl.offsetWidth; /* reflow to restart animation */
        priceEl.classList.add('updated');

        /* Permanence note */
        var permEl = document.getElementById('perm-' + plan);
        permEl.textContent = filters.permanencia === 'con'
            ? 'Permanencia 12 meses'
            : 'Sin permanencia · Baja con 30 días preaviso';

        /* Family discount badge */
        var discEl = document.getElementById('discount-' + plan);
        if (filters.familia === 'si') {
            discEl.classList.remove('d-none');
        } else {
            discEl.classList.add('d-none');
        }
    });

    /* Update summary text */
    var summaryParts = [];
    summaryParts.push(filters.permanencia === 'con' ? 'Con permanencia' : 'Sin permanencia');
    summaryParts.push(filters.familia === 'si' ? 'Familia numerosa' : '');
    summaryParts.push(filters.edad === 'nino' ? 'Menor (12–17)' : 'Adulto (+18)');
    var summary = summaryParts.filter(Boolean).join(' · ');

    var summaryEl = document.getElementById('filterSummary');
    if (summaryEl) {
        summaryEl.innerHTML = 'Mostrando: <strong>' + summary + '</strong>';
    }
}

/* ── Filter button click handler ── */
document.querySelectorAll('.filter-btn').forEach(function (btn) {
    btn.addEventListener('click', function () {
        var filterType  = btn.getAttribute('data-filter');
        var filterValue = btn.getAttribute('data-value');

        /* Deactivate siblings in the same group */
        document.querySelectorAll('[data-filter="' + filterType + '"]').forEach(function (b) {
            b.classList.remove('active');
            b.setAttribute('aria-pressed', 'false');
        });

        /* Activate this button */
        btn.classList.add('active');
        btn.setAttribute('aria-pressed', 'true');

        /* Update state */
        filters[filterType] = filterValue;

        /* Recompute prices */
        updatePrices();
    });
});

/* ── Navbar scroll effect ── */
window.addEventListener('scroll', function () {
    var navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.classList.add('navbar-scrolled');
    } else {
        navbar.classList.remove('navbar-scrolled');
    }
});

/* ── Init ── */
updatePrices();
