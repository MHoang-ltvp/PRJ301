<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Work Assignment List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 50px;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .table {
            margin-top: 20px;
        }
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }
    </style>
</head>
<body>
<div class="container">
    <h2 class="text-center">Work Assignment List</h2>
    <table class="table table-bordered table-hover">
        <thead class="thead-light">
        <tr>
            <th>EID</th>
            <th>Employee Name</th>
            <th>Product Name</th>
            <th>Date</th>
            <th>Quantity</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="workAssignment" items="${workAssignments}">
            <tr>
                <td>${workAssignment.employees.id}</td>
                <td>${workAssignment.employees.ename}</td>
                <td>${workAssignment.planDetail.planHeader.product.id}</td>
                <td>${workAssignment.planDetail.planHeader.product.name}</td>
                <td>${workAssignment.quantity}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
