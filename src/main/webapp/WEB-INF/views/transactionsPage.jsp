<%@ page import="java.util.List" %>
<%@ page import="com.asd.model.Transaction" %>
<html>
<head>
    <title>Transactions</title>
</head>
<body>
<h1>Transaction List</h1>
<ul>
    <%
        List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
        for (Transaction t : transactions) {
    %>
    <li>ID: <%= t.getId() %>, Amount: <%= t.getAmount()%>, Customer: <%= t.getCustomer().getName()%></li>
    <%
        }
    %>
</ul>

<button><label>Export</label></button>
</body>
</html>
