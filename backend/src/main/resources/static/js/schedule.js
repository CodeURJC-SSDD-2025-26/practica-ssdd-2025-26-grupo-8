// ==================== SCHEDULE.JS - Virtus Fitness ====================
// JS específico para la página schedule.html

document.addEventListener('DOMContentLoaded', function () {

    initializeScheduleHighlight();
    initializeActivityTooltips();
    initializeTableRowHover();
    initializeScheduleFilter();
    highlightCurrentDay();

    console.log('✅ schedule.js cargado correctamente');
});


// ==================== RESALTAR DÍA ACTUAL ====================
function highlightCurrentDay() {
    const days = ['', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'];
    const today = new Date().getDay(); // 0=Dom, 1=Lun ... 5=Vie

    if (today < 1 || today > 5) return; // Fin de semana: nada que resaltar

    const headers = document.querySelectorAll('.schedule-table thead th');

    headers.forEach((th, index) => {
        if (th.textContent.trim() === days[today]) {
            th.style.background = 'var(--primary, #f5a623)';
            th.style.color = '#000';
            th.style.fontWeight = '700';

            // Resaltar también las celdas de esa columna
            const rows = document.querySelectorAll('.schedule-table tbody tr');
            rows.forEach(row => {
                const cells = row.querySelectorAll('td');
                if (cells[index]) {
                    cells[index].style.background = 'rgba(245,166,35,0.08)';
                }
            });
        }
    });
}


// ==================== TOOLTIP EN CADA ACTIVIDAD ====================
function initializeActivityTooltips() {
    const activityInfo = {
        'Yoga Flow':        'Nivel: Todos · Instructor: Ana Martínez · Máx. 20 plazas',
        'CrossFit WOD':     'Nivel: Intermedio/Avanzado · Instructor: Carlos Mendoza · Máx. 15 plazas',
        'Indoor Cycling':   'Nivel: Todos · Instructor: Luis García · Máx. 25 plazas',
        'Boxing Pro':       'Nivel: Intermedio · Instructor: Valentina Ortiz · Máx. 12 plazas',
        'Pilates Reformer': 'Nivel: Todos · Instructor: Laura Sánchez · Máx. 10 plazas',
        'Zumba Dance':      'Nivel: Todos · Instructor: Mario Ruiz · Máx. 30 plazas'
    };

    document.querySelectorAll('.activity-slot').forEach(slot => {
        const nameEl = slot.querySelector('.act-name');
        if (!nameEl) return;

        const actName = nameEl.textContent.trim();
        const info = activityInfo[actName];
        if (!info) return;

        // Atributo para Bootstrap tooltip
        slot.setAttribute('data-bs-toggle', 'tooltip');
        slot.setAttribute('data-bs-placement', 'top');
        slot.setAttribute('title', info);
        slot.style.cursor = 'pointer';

        // Inicializar tooltip Bootstrap
        new bootstrap.Tooltip(slot);

        // Click → intentar reserva
        slot.addEventListener('click', function () {
            const roomEl = slot.querySelector('.act-room');
            const room = roomEl ? roomEl.textContent.trim() : '';
            const timeCell = slot.closest('tr')?.querySelector('.time-range');
            const time = timeCell ? timeCell.textContent.trim() : '';

            if (typeof window.reserveClass === 'function') {
                window.reserveClass(actName);
            } else {
                // Fallback si main.js no está cargado aún
                const isLoggedIn = sessionStorage.getItem('isLoggedIn');
                if (!isLoggedIn) {
                    alert(`Para reservar "${actName}" (${time} · ${room}) necesitas iniciar sesión.`);
                } else {
                    alert(`✅ Reserva confirmada: ${actName} · ${time} · ${room}`);
                }
            }
        });
    });
}


// ==================== HOVER VISUAL EN FILAS ====================
function initializeTableRowHover() {
    document.querySelectorAll('.schedule-table tbody tr').forEach(row => {
        row.addEventListener('mouseenter', function () {
            this.style.transition = 'background 0.2s ease';
            this.style.background = 'rgba(255,255,255,0.04)';
        });
        row.addEventListener('mouseleave', function () {
            this.style.background = '';
        });
    });
}


// ==================== RESALTAR COLUMNA AL HACER HOVER ====================
function initializeScheduleHighlight() {
    const table = document.querySelector('.schedule-table');
    if (!table) return;

    table.addEventListener('mouseover', function (e) {
        const td = e.target.closest('td, th');
        if (!td) return;

        const colIndex = td.cellIndex;
        if (colIndex === 0) return; // columna de horas, ignorar

        // Quitar resaltado previo
        table.querySelectorAll('.col-hover').forEach(el => el.classList.remove('col-hover'));

        // Aplicar a toda la columna
        table.querySelectorAll(`tr td:nth-child(${colIndex + 1}), tr th:nth-child(${colIndex + 1})`)
            .forEach(cell => cell.classList.add('col-hover'));
    });

    table.addEventListener('mouseleave', function () {
        table.querySelectorAll('.col-hover').forEach(el => el.classList.remove('col-hover'));
    });

    // Inyectar estilo para col-hover
    const style = document.createElement('style');
    style.textContent = `
        .schedule-table td.col-hover,
        .schedule-table th.col-hover {
            background: rgba(255, 255, 255, 0.05) !important;
        }
    `;
    document.head.appendChild(style);
}


// ==================== FILTRO POR TIPO DE ACTIVIDAD ====================
function initializeScheduleFilter() {
    // Crear barra de filtro dinámicamente encima de la tabla
    const wrapper = document.querySelector('.schedule-wrapper');
    if (!wrapper) return;

    const activities = ['Todas', 'Yoga Flow', 'CrossFit WOD', 'Indoor Cycling', 'Boxing Pro', 'Pilates Reformer', 'Zumba Dance'];

    const filterBar = document.createElement('div');
    filterBar.className = 'd-flex flex-wrap gap-2 mb-4';
    filterBar.id = 'scheduleFilterBar';

    activities.forEach(act => {
        const btn = document.createElement('button');
        btn.className = 'btn btn-sm ' + (act === 'Todas' ? 'btn-warning' : 'btn-outline-secondary');
        btn.textContent = act;
        btn.dataset.filter = act;

        btn.addEventListener('click', function () {
            // Actualizar botones activos
            filterBar.querySelectorAll('button').forEach(b => {
                b.classList.remove('btn-warning');
                b.classList.add('btn-outline-secondary');
            });
            this.classList.remove('btn-outline-secondary');
            this.classList.add('btn-warning');

            applyScheduleFilter(act);
        });

        filterBar.appendChild(btn);
    });

    wrapper.parentNode.insertBefore(filterBar, wrapper);
}

function applyScheduleFilter(activityName) {
    document.querySelectorAll('.activity-slot').forEach(slot => {
        const nameEl = slot.querySelector('.act-name');
        if (!nameEl) return;

        if (activityName === 'Todas' || nameEl.textContent.trim() === activityName) {
            slot.style.opacity = '1';
            slot.style.transform = 'scale(1)';
            slot.style.pointerEvents = 'auto';
        } else {
            slot.style.opacity = '0.15';
            slot.style.transform = 'scale(0.95)';
            slot.style.pointerEvents = 'none';
        }
        slot.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
    });
}
