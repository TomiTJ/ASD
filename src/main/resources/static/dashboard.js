const COLORS = {
    green:  '#22c55e',
    blue:   '#3b82f6',
    yellow: '#f59e0b',
    red:    '#ef4444',
    purple: '#8b5cf6',
    teal:   '#14b8a6',
    orange: '#f97316',
    gray:   '#94a3b8',
};

function donut(canvasId, labels, values, colors) {
    const el = document.getElementById(canvasId);
    if (!el) return;
    new Chart(el.getContext('2d'), {
        type: 'doughnut',
        data: {
            labels,
            datasets: [{ data: values, backgroundColor: colors, borderWidth: 2 }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom', labels: { padding: 12, font: { size: 12 } } }
            },
            cutout: '62%'
        }
    });
}

function bar(canvasId, labels, values, colors, horizontal = false) {
    const el = document.getElementById(canvasId);
    if (!el) return;
    new Chart(el.getContext('2d'), {
        type: horizontal ? 'bar' : 'bar',
        data: {
            labels,
            datasets: [{
                data: values,
                backgroundColor: colors,
                borderRadius: 6,
                borderSkipped: false,
            }]
        },
        options: {
            indexAxis: horizontal ? 'y' : 'x',
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, ticks: { precision: 0 } },
                x: { grid: { display: false } }
            }
        }
    });
}

async function loadDashboard() {
    try {
        const res = await fetch('/api/dashboard/metrics');
        if (!res.ok) throw new Error('metrics fetch failed');
        const d = await res.json();

        // ── Metric cards ──────────────────────────────────────
        function setCard(id, value) {
            const el = document.getElementById(id);
            el.textContent = value;
            el.classList.remove('skeleton');
        }

        setCard('totalUsers',        d.totalUsers ?? 0);
        setCard('totalAccounts',     d.totalAccounts ?? 0);
        setCard('totalTransactions', d.totalTransactions ?? 0);

        const bal = d.totalOpenBalance ?? 0;
        setCard('totalBalance',
            '$' + Number(bal).toLocaleString('en-AU', { minimumFractionDigits: 2, maximumFractionDigits: 2 }));

        // ── Transaction Status — Donut ────────────────────────
        const txStatus = d.transactionsByStatus ?? {};
        donut('txStatusChart',
            Object.keys(txStatus),
            Object.values(txStatus),
            [COLORS.green, COLORS.yellow, COLORS.red, COLORS.orange]
        );

        // ── Transaction Types — Bar ───────────────────────────
        const txType = d.transactionsByType ?? {};
        bar('txTypeChart',
            Object.keys(txType),
            Object.values(txType),
            [COLORS.blue, COLORS.purple, COLORS.teal]
        );

        // ── Account Types — Donut ─────────────────────────────
        const accType = d.accountsByType ?? {};
        donut('accountTypeChart',
            Object.keys(accType),
            Object.values(accType),
            [COLORS.blue, COLORS.green, COLORS.orange, COLORS.purple]
        );

        // ── Account Status — Bar ──────────────────────────────
        const accStatus = d.accountsByStatus ?? {};
        bar('accountStatusChart',
            Object.keys(accStatus),
            Object.values(accStatus),
            [COLORS.green, COLORS.yellow, COLORS.gray]
        );

        // ── Loan Pipeline — Donut ─────────────────────────────
        const loans = d.loansByStatus ?? {};
        donut('loanChart',
            Object.keys(loans),
            Object.values(loans),
            [COLORS.yellow, COLORS.green, COLORS.red]
        );

    } catch (e) {
        console.error('Dashboard error:', e);
        ['totalUsers', 'totalAccounts', 'totalTransactions', 'totalBalance']
            .forEach(id => {
                const el = document.getElementById(id);
                if (el && el.textContent === '—') el.textContent = '—';
            });
    }
}

window.addEventListener('load', loadDashboard);
