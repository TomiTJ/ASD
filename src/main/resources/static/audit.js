async function loadAudits(params = {}) {
    const q = new URLSearchParams();
    if (params.action) q.set("action", params.action);
    if (params.from) q.set("from", params.from);
    if (params.to) q.set("to", params.to);

    const res = await fetch("/api/audits" + (q.toString() ? `?${q}` : ""));
    const data = res.ok ? await res.json() : [];

    const tbody = document.getElementById("auditRows");
    tbody.innerHTML = "";

    data.forEach(a => {
        const row = document.createElement("tr");
        row.innerHTML = `
      <td>${a.createdAt?.replace('T',' ').slice(0,19)}</td>
      <td>${a.userName || "Unknown"}</td>
      <td>${a.action}</td>
      <td>${a.resourceType}</td>
      <td>${a.requestId}</td>
    `;
        tbody.appendChild(row);
    });
}

document.getElementById("applyBtn").onclick = () => {
    const action = document.getElementById("filterAction").value;
    const from = document.getElementById("filterFrom").value;
    const to = document.getElementById("filterTo").value;
    loadAudits({ action, from, to });
};

document.getElementById("resetBtn").onclick = () => {
    document.getElementById("filterAction").value = "";
    document.getElementById("filterFrom").value = "";
    document.getElementById("filterTo").value = "";
    loadAudits();
};

loadAudits();