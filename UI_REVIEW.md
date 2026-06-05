# UI/UX Audit — Bank Admin App

Two passes. First: designer tearing apart every visual decision. Second: first-time user clicking through the whole app.

---

## Pass 1 — Designer Review

> *"I've looked at this for 90 seconds and I can already tell this was built by engineers who learned Thymeleaf last semester. The login page is actually decent — someone clearly put effort in. Then you open the app and it falls apart. Here's everything that needs to die."*

---

## Pass 2 — First-Time User Walkthrough

> *"I'm an operations manager at a mid-size bank. My IT team told me to use this to manage accounts. Here's what happened."*

1. **Login page** — Clean. I know what to do. I type my email and password and sign in. Good.
2. **Dashboard** — I see numbers but the charts take a second to load and just show "—". I'm not sure if it crashed. Nobody told me the charts were loading.
3. **I click Customer Accounts** — I see a table of accounts. I want to create a new one. I scroll through the whole table looking for a button. I don't find one. I scroll further down and eventually find a create form — buried below 14 rows of data. I almost left.
4. **I try to freeze an account** — I click "Freeze". No confirmation. It just happened. I don't know if I made a mistake.
5. **I go to Transactions** — There are three separate boxes stacked on the page. I'm not sure if I'm looking at a form, a filter, or a list. The "Process Transfer" box is above the transaction history which feels backwards.
6. **I try to do a transfer** — The dropdown says `ACC-1000456701 - John Smith ($1250.75000000)`. The balance has too many decimal places. This looks broken.
7. **I go to Reports** — Page loads. I don't know what to do. There's no explanation of what this generates.
8. **I try to log out** — I click Logout in the sidebar. It navigates me. Did it work? I'm at the login page. I guess so.
9. **I open the app on my phone** — It's completely broken. The sidebar is squashed. The table overflows off screen. I close the tab.

---

# Issues — Sorted by Priority

---

## 🔴 Critical

### 1. No active state on navbar — user has no idea where they are
**File:** `navbar2.html`, `main.css`

Every nav item looks identical regardless of which page you're on. There is zero visual indication of the current page. This is a basic usability requirement for any multi-page app.

**Fix:** Add an `active` class to the current nav item and style it with a left border + background highlight.

---

### 2. Nav items are `div` + `th:onclick` — not real links
**File:** `navbar2.html`

```html
<!-- Current — broken -->
<div th:onclick="|location.href='/dashboard'|">Dashboard</div>

<!-- Fix -->
<a href="/dashboard" class="nav-item">Dashboard</a>
```

This means: no keyboard navigation, no right-click → open in new tab, no browser history management, no accessibility. Screen readers cannot identify these as navigation. Replace every nav `div` with an `<a>` tag.

---

### 3. No `<meta viewport>` on any page except login — mobile is completely broken
**Files:** `account.html`, `transactionsPage.html`, `dashboard.html`, `users.html`, `audits.html`, `loan.html`, `reports.html`

The login page has it. Every other page is missing it. On any mobile device the entire app overflows and is unusable.

**Fix:** Add to every page's `<head>`:
```html
<meta name="viewport" content="width=device-width, initial-scale=1">
```

---

### 4. `img-placeholder` renders as a grey box in the top bar — looks like broken UI
**Files:** `main.css`, every template

```css
.img-placeholder {
    border: solid #E5E5E5 2px;
    background-color: #E5E5E5; /* grey box */
    border-radius: 16px;
    padding: 13px;
}
```

Every page shows a grey rectangle in the top-right next to the username. It looks like a broken image. Either replace it with a proper avatar/initials component or remove it entirely.

---

### 5. `font-family: 'Lato Semibold'` — this font doesn't exist
**File:** `main.css` line 2

`Lato Semibold` is not a valid CSS font family name. The browser falls back silently to sans-serif. The correct declaration is:
```css
font-family: 'Lato', sans-serif;
font-weight: 600;
```
And Lato isn't even imported from Google Fonts anywhere. The whole app is using the browser default font.

**Fix:** Add to every page `<head>`:
```html
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
```
Then update `main.css` to use `font-family: 'Inter', system-ui, sans-serif`.

---

### 6. Create Account form is buried below the data table
**File:** `account.html`

The primary action (creating an account) is placed at the bottom of the page, after scrolling past an entire table of existing accounts. This inverts the natural flow. New users will not find it.

**Fix:** Move the create form into a modal triggered by a "New Account" button in the page header — same pattern as the "View Details" modal that's already built.

---

### 7. Status values display as raw enum strings in ALL CAPS
**Files:** `transactionsPage.html`, `account.html`, `users.html`

`COMPLETED`, `PENDING`, `FAILED`, `OPEN`, `FROZEN`, `CLOSED`, `READ_ONLY` are all displayed as-is from the enum. This looks unpolished and unfinished.

**Fix:** Use Thymeleaf's string utilities or CSS `text-transform: capitalize` to display `Completed`, `Pending`, `Failed` etc.

---

## 🟡 High Impact

### 8. No loading state while charts render on dashboard
**File:** `dashboard.html`, `dashboard.js`

The metric cards show `—` and the chart canvases are blank for 1-2 seconds while the API call completes. Users think it's broken.

**Fix:** Add a skeleton shimmer on each card and canvas. When data loads, fade it in. Alternatively, add `Loading...` text that gets replaced.

---

### 9. Transfer dropdown shows raw BigDecimal with excess decimal places
**File:** `transactionsPage.html`

```
ACC-1000456701 - John Smith ($1250.75000000)
```

The `$` + raw `account.balance` outputs unformatted BigDecimal. Looks broken.

**Fix:**
```html
th:text="${account.accountNumber + ' — ' + account.customerName + ' ($' + #numbers.formatDecimal(account.balance, 1, 2) + ')'}"
```

---

### 10. "Time Created" and "Date Created" are two separate columns
**File:** `transactionsPage.html`

Splitting a timestamp into two columns wastes horizontal space and is confusing. Every other system shows one datetime column.

**Fix:** Merge into a single `Created At` column: `21 Mar 2025, 09:10`.

---

### 11. No confirmation on Freeze and Close — only Delete has one
**File:** `account.html`

Freezing or closing an account is a significant action. Only the Delete button uses `onsubmit="return confirm(...)"`. Freeze and Close fire immediately with no warning.

**Fix:** Add `onsubmit="return confirm('Freeze this account?')"` to Freeze and `return confirm('Close this account? This cannot be undone.')` to Close.

---

### 12. Logout is a nav item sitting alongside Dashboard, Users, Transactions
**File:** `navbar2.html`

Logout is a destructive action. Placing it in the primary navigation alongside feature links increases the chance of accidental clicks and makes the nav feel cluttered. In every professional tool (Linear, Vercel, Notion) logout is hidden in a profile menu or at the very bottom of the sidebar with visual separation.

**Fix:** Move logout to the very bottom of the sidebar with a separator line and different colour treatment (e.g., muted red on hover).

---

### 13. Three stacked sections on the Transactions page with no visual hierarchy
**File:** `transactionsPage.html`

The page has: Search & Filter → Process Transfer → Transactions List, all stacked with equal visual weight. The transfer form doesn't belong in the main flow — it's an action, not a filter or a list.

**Fix:** Move the Transfer form behind a "New Transfer" button that opens a modal. This cleans up the page to just: header with actions → filter bar → table.

---

### 14. Empty 100px footer on every page
**File:** `main.css`

```css
grid-template-rows: 60px 5fr 100px;
```

There is a 100px footer on every page that renders as completely empty white space. It adds visual dead weight and makes every page look unfinished.

**Fix:** Remove the footer row or reduce it to `24px` and add a subtle copyright line.

---

### 15. Top bar only shows username — no utility whatsoever
**Files:** Every template

The top bar (60px tall, full width, baby blue) shows: username, role, grey box. That's it. 60px of prime real estate doing nothing. In every SaaS tool this is where you put breadcrumbs, page title, or global search.

**Fix:** Add a `<h1>` page title on the left side of the top bar, and move the user info + logout to the right side as a proper profile dropdown.

---

### 16. Audit item in navbar is missing `<span>` wrapper
**File:** `navbar2.html`

```html
<!-- Missing span like every other item -->
<div th:onclick="|location.href='/audit'|"><img ...>Audits</div>
```

The text "Audits" sits directly in the div without a `<span>`, so it doesn't get the `margin-left: 5px` spacing and looks misaligned compared to every other nav item.

---

### 17. Same icon used for both "Users" and "Customer Accounts"
**File:** `navbar2.html`

```html
<div ...><img th:src="@{/icons/accounts.svg}">Users</div>
<div ...><img th:src="@{/icons/accounts.svg}">Customer Accounts</div>
```

Both items use `accounts.svg`. Visually they look identical. A user scanning the nav cannot distinguish between them.

**Fix:** Use a `person.svg` for Users and `bank.svg` or `wallet.svg` for Customer Accounts.

---

## 🟢 Nice to Have

### 18. No page titles in `<title>` tag on most pages
Several pages are missing meaningful `<title>` tags. Browser tab shows generic names. Add: `<title>Transactions — Bank Admin</title>`, `<title>Accounts — Bank Admin</title>` etc.

---

### 19. Inline `style=""` attributes scattered throughout
`account.html` has `style="display:inline;"`, `style="text-align:center; color: gray;"`, `style="margin-top: 2rem;"` etc. Dozens of inline styles. Extract to CSS classes.

---

### 20. `confirm()` dialogs for destructive actions look 1995
The browser's native `confirm()` dialog is unstyled, blocks the main thread, and looks broken in modern browsers. Replace with an inline confirmation pattern ("Are you sure? [Cancel] [Delete]") or a small modal.

---

### 21. No empty state design for tables
When no accounts/transactions are found, the table shows:
```
No accounts found  (grey centered text)
```
This is fine but a proper empty state with an icon, a heading, and a call-to-action ("No accounts yet. Create the first one →") converts significantly better.

---

### 22. Thymeleaf deprecated fragment syntax still in some templates
`"fragments/navbar2 :: navbar2"` (unwrapped) still appears in `loan.html` and others. Should be `~{fragments/navbar2 :: navbar2}`. Spring Boot logs warnings for this on every page load.

---

### 23. Top bar background (#C4E0F9) is the same as login page's left panel
The brand panel on the login page and the application top bar are the exact same colour. It makes the logged-in state feel like you never actually entered the app. The top bar should be white or dark to signal "you're inside the product now."

---

### 24. No favicon
Browser tab shows the browser's default icon. Add a `favicon.ico` to `/static/`.

---

## Summary Table

| # | Issue | Priority | Files |
|---|---|---|---|
| 1 | No active nav state | 🔴 Critical | `navbar2.html`, `main.css` |
| 2 | Nav items are divs not links | 🔴 Critical | `navbar2.html` |
| 3 | No viewport meta tag | 🔴 Critical | All templates |
| 4 | Grey placeholder box looks broken | 🔴 Critical | All templates |
| 5 | Invalid font family declaration | 🔴 Critical | `main.css` |
| 6 | Create form buried below table | 🔴 Critical | `account.html` |
| 7 | Enum values in ALL CAPS | 🔴 Critical | `transactionsPage.html`, `account.html` |
| 8 | No loading state on dashboard | 🟡 High | `dashboard.html`, `dashboard.js` |
| 9 | BigDecimal formatting in dropdown | 🟡 High | `transactionsPage.html` |
| 10 | Two columns for one timestamp | 🟡 High | `transactionsPage.html` |
| 11 | No confirmation on Freeze/Close | 🟡 High | `account.html` |
| 12 | Logout in primary nav | 🟡 High | `navbar2.html` |
| 13 | Transfer form clutters transactions page | 🟡 High | `transactionsPage.html` |
| 14 | Empty 100px footer | 🟡 High | `main.css` |
| 15 | Top bar has no utility | 🟡 High | All templates |
| 16 | Audit nav item missing span | 🟡 High | `navbar2.html` |
| 17 | Duplicate icon for Users & Accounts | 🟡 High | `navbar2.html` |
| 18 | Missing page titles | 🟢 Nice | All templates |
| 19 | Inline styles everywhere | 🟢 Nice | Multiple |
| 20 | Native confirm() dialogs | 🟢 Nice | `account.html` |
| 21 | No empty state design | 🟢 Nice | All tables |
| 22 | Deprecated Thymeleaf fragment syntax | 🟢 Nice | `loan.html`, others |
| 23 | Top bar same colour as login branding | 🟢 Nice | `main.css`, `login.css` |
| 24 | No favicon | 🟢 Nice | `static/` |
