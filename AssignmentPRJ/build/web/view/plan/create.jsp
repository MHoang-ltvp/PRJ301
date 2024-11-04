<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Production Plan</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
                margin: 20px;
            }
            .container {
                background: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                max-width: 800px;
                margin: auto;
            }
            h2 {
                color: #007bff;
            }
            .form-group label {
                font-weight: bold;
            }
            .table th, .table td {
                vertical-align: middle;
            }
            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2 class="text-center mb-4">Create Production Plan</h2>
            <form action="create" method="POST">
                <!-- Plan information -->
                <div class="form-group">
                    <label for="planName">Plan Name:</label>
                    <input type="text" class="form-control" id="planName" name="planName" required />
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="startDate">From:</label>
                        <input type="date" class="form-control" id="startDate" name="startDate" required />
                    </div>
                    <div class="form-group col-md-6">
                        <label for="endDate">To:</label>
                        <input type="date" class="form-control" id="endDate" name="endDate" required />
                    </div>
                </div>
                <div class="form-group">
                    <label for="departmentId">Workshop:</label>
                    <select class="form-control" id="departmentId" name="departmentId" required>
                        <c:forEach items="${workshops}" var="workshop">
                            <option value="${workshop.id}">${workshop.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Product information -->
                <h3 class="mb-3">Product Details</h3>
                <table class="table table-bordered">
                    <thead class="thead-light">
                        <tr>
                            <th scope="col">Product</th>
                            <th scope="col">Quantity</th>
                            <th scope="col">Estimated Effort</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${products}" var="product" varStatus="status">
                            <tr>
                                <td>
                                    ${product.name}
                                    <input type="hidden" name="productId" value="${product.id}" />
                                </td>
                                <td><input type="number" class="form-control" name="quantity" min="0" /></td>
                                <td><input type="text" class="form-control" name="estimatedEffort" /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Submit button -->
                <div class="text-center">
                    <button type="submit" class="btn btn-primary btn-lg"><i class="fas fa-save"></i> Save</button>
                </div>
            </form>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>