async function loadMetrics() {
    try {
        const response = await fetch('/api/dashboard/metrics');
        const data = await response.json();

        document.getElementById("totalUsers").textContent = data.totalUsers;
        document.getElementById("totalAccounts").textContent = data.totalAccounts;
        document.getElementById("totalTransactions").textContent = data.totalTransactions;

        // Demo graph (mock growth over 7 days)
        const ctx = document.getElementById('transactionsChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    label: 'Transactions per Day',
                    data: [2, 4, 6, 10, 15, 20, data.totalTransactions], // last point uses live total
                    borderColor: 'blue',
                    backgroundColor: 'rgba(0, 0, 255, 0.1)',
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

    } catch (error) {
        console.error("Error loading metrics:", error);
    }
}

// Load on page start
window.onload = loadMetrics;