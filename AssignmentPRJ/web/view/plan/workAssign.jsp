<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Work Assignment</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
                margin: 0;
            }
            .container {
                background: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                max-width: 100%;
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
                text-align: center;
                padding: 4px; /* Giảm padding của từng cell */
                width: 100px; /* Đặt chiều rộng mặc định cho các cell */
            }
            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
            }
            .work-assignment-table {
                margin: 0 auto; /* Căn giữa bảng */
                width: 70%; /* Thu nhỏ độ rộng bảng */
                max-width: 800px;
            }
            .form-control-small {
                width: 50px; /* Giảm chiều rộng của các ô nhập liệu */
                padding: 2px; /* Giảm padding của các ô nhập liệu */
                text-align: center;
                margin: auto;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <h2 class="text-center mb-4">Assigned Product Plan for: ${plan.name}</h2>
            <form action="workassignment" method="POST">

                <!-- Product Detail Table -->
                <h3 class="mb-3">Product Detail</h3>
                <table class="table table-bordered">
                    <thead class="thead-light">
                        <tr>
                            <th scope="col">Product</th>
                            <c:forEach items="${dateList}" var="date">
                                <th scope="col" colspan="${fn:length(shifts)}">${date}</th>
                            </c:forEach>
                        </tr>
                        <tr>
                            <th></th>
                            <c:forEach items="${dateList}" var="date">
                                <c:forEach items="${shifts}" var="shift">
                                    <th scope="col">${shift}</th>
                                </c:forEach>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${planHeaders}" var="planHeader">
                            <tr>
                                <td>${planHeader.product.name}
                                    <input type="hidden" name="phid" value="${planHeader.id}" />
                                </td>
                                <c:forEach items="${dateList}" var="date">
                                    <c:forEach items="${shifts}" var="shift">
                                        <td>
                                            <c:set var="currentValue" value="" />
                                            <c:forEach items="${planDetailsList}" var="planDetail">
                                                <c:set var="shiftId" value="-1" />
                                                <c:choose>
                                                    <c:when test="${shift == 'k1'}">
                                                        <c:set var="shiftId" value="1" />
                                                    </c:when>
                                                    <c:when test="${shift == 'k2'}">
                                                        <c:set var="shiftId" value="2" />
                                                    </c:when>
                                                    <c:when test="${shift == 'k3'}">
                                                        <c:set var="shiftId" value="3" />
                                                    </c:when>
                                                </c:choose>
                                                <c:if test="${planDetail.planHeader.id == planHeader.id &&
                                                              planDetail.date == date &&
                                                              planDetail.shiftId == shiftId}">
                                                    <c:set var="currentValue" value="${planDetail.quantity}" />
                                                </c:if>
                                            </c:forEach>
                                            <input type="hidden" name="date_${planHeader.id}_${date}_${shift}" value="${date}" />
                                            <input type="hidden" name="shiftId_${planHeader.id}_${date}_${shift}" value="${shift}" />
                                            <input type="number" class="form-control form-control-small" name="quantity_${planHeader.id}_${date}_${shift}" value="${currentValue}" min="0" readonly />
                                        </td>
                                    </c:forEach>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Work Assignment Table -->
                <h3 class="mb-3 text-center">Work Assignment Detail</h3>
                <div style="overflow-x: auto; display: flex; justify-content: center;">
                    <table class="table table-bordered work-assignment-table" id="workAssignmentTable">
                        <thead class="thead-light">
                            <tr>
                                <th scope="col">Date</th>
                                <th scope="col">Employee ID</th>
                                <th scope="col">Shift ID</th>
                                <th scope="col">Product ID</th>
                                <th scope="col">Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach begin="1" end="100" var="index" varStatus="status">
                                <tr>
                                    <td>
                                        <input type="date" class="form-control" name="date_${status.index}" />
                                    </td>
                                    <td>
                                        <input type="number" class="form-control" name="employeeId_${status.index}" min="1" />
                                    </td>
                                    <td>
                                        <input type="number" class="form-control" name="shiftId_${status.index}" min="0" />
                                    </td>
                                    <td>
                                        <input type="number" class="form-control" name="productId_${status.index}" min="0" />
                                    </td>
                                    <td>
                                        <input type="number" class="form-control" name="quantity_${status.index}" min="0" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Submit button -->
                <div class="text-center">
                    <button type="submit" class="btn btn-primary btn-lg">Save</button>
                </div>
                <!-- track total row and plid -->
                <input type="hidden" id="totalRows" name="totalRows" value="0">
                <input type="hidden" name="plid" value="${plan.id}">
            </form>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
