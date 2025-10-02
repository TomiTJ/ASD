async function loadMetrics() {
    try {
        const response = await fetch('/api/dashboard/metrics');
        const data = await response.json();

        document.getElementById("totalUsers").textContent = data.totalUsers;
        document.getElementById("totalAccounts").textContent = data.totalAccounts;
        document.getElementById("totalTransactions").textContent = data.totalTransactions;
    } catch (error) {
        console.error("Error loading metrics:", error);
    }
}

// Load on page start
window.onload = loadMetrics;