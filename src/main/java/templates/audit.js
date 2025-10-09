console.log('audit loaded');
(function () {

    const TIME_MODE = 'INSTANT';

    const rowsEl = document.getElementById('auditRows');
    const emptyEl = document.getElementById('emptyState');
    const totalEl = document.getElementById('totalLabel');
    const applyBtn = document.getElementById('applyBtn');
    const resetBtn = document.getElementById('resetBtn');
    const actionEl = document.getElementById('filterAction');
    const fromEl = document.getElementById('filterFrom');
    const toEl = document.getElementById('filterTo');

    function pad2(n) { return n.toString().padStart(2, '0'); }
    function toLocalDateTimeParam(value) {
        // value from <input type="datetime-local"> like "2025-10-09T15:30"
        if (!value) return '';
        return value.length === 16 ? `${value}:00` : value; // ensure seconds
    }
    function toInstantParam(value) {
        if (!value) return '';
        // treat as local time and convert to UTC ISO string (with Z)
        const d = new Date(value);
        if (isNaN(d.getTime())) return '';
        return d.toISOString();
    }
    function buildParams() {
        const q = new URLSearchParams();
        const action = actionEl?.value?.trim();
        const fromRaw = fromEl?.value?.trim();
        const toRaw = toEl?.value?.trim();

        if (action) q.set('action', action);

        if (fromRaw) {
            q.set('from', TIME_MODE === 'INSTANT'
                ? toInstantParam(fromRaw)
                : toLocalDateTimeParam(fromRaw));
        }
        if (toRaw) {
            q.set('to', TIME_MODE === 'INSTANT'
                ? toInstantParam(toRaw)
                : toLocalDateTimeParam(toRaw));
        }
        return q;
    }

    function fmtWhen(iso) {
        if (!iso) return '';
        try {
            const d = new Date(iso);
            if (!isNaN(d.getTime())) {
                // YYYY-MM-DD HH:mm:ss
                return `${d.getFullYear()}-${pad2(d.getMonth()+1)}-${pad2(d.getDate())} ${pad2(d.getHours())}:${pad2(d.getMinutes())}:${pad2(d.getSeconds())}`;
            }
        } catch (_) {}
        return String(iso).replace('T',' ').slice(0,19);
    }

    function render(data) {
        rowsEl.innerHTML = '';
        if (!Array.isArray(data) || data.length === 0) {
            emptyEl.hidden = false;
            totalEl.textContent = 'Total: 0';
            return;
        }
        emptyEl.hidden = true;
        totalEl.textContent = `Total: ${data.length}`;

        const frag = document.createDocumentFragment();
        data.forEach(a => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
        <td>${fmtWhen(a.createdAt)}</td>
        <td class="mono">${a.userName || ('User#' + (a.userId ?? ''))}</td>
        <td><span class="tag">${a.action ?? ''}</span></td>
        <td>${a.resourceType ?? ''}${a.resourceId ? ` <span class="mono">/ ${a.resourceId}</span>` : ''}</td>
        <td class="mono">${a.requestId ?? ''}</td>
      `;
            frag.appendChild(tr);
        });
        rowsEl.appendChild(frag);
    }

    async function loadAudits() {
        const q = buildParams();
        const url = '/api/audits' + (q.toString() ? `?${q.toString()}` : '');
        try {
            const res = await fetch(url, {
                credentials: 'same-origin',
                headers: { 'Accept': 'application/json' }
            });
            const ct = res.headers.get('content-type') || '';
            if (!res.ok || !ct.includes('application/json')) {
                console.error('[audit] Bad response', res.status, ct);
                render([]);
                return;
            }
            const data = await res.json();
            render(data);
        } catch (e) {
            console.error('[audit] fetch error', e);
            render([]);
        }
    }

    function wire() {
        applyBtn?.addEventListener('click', loadAudits);
        resetBtn?.addEventListener('click', () => {
            if (actionEl) actionEl.value = '';
            if (fromEl) fromEl.value = '';
            if (toEl) toEl.value = '';
            loadAudits();
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => { wire(); loadAudits(); });
    } else {
        wire(); loadAudits();
    }
})();