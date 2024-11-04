<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Plan Detail</title>
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
            }
            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <h2 class="text-center mb-4">Plan Detail for Plan: ${plan.name}</h2>
            <form action="detail" method="POST">
                <!-- Plan Information -->
                <div class="form-group">
                    <label>Plan Name:</label>
                    <input type="text" class="form-control" value="${plan.name}" readonly />
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label>Start Date:</label>
                        <input type="text" class="form-control" value="${plan.start}" readonly />
                    </div>
                    <div class="form-group col-md-6">
                        <label>End Date:</label>
                        <input type="text" class="form-control" value="${plan.end}" readonly />
                    </div>
                </div>
                <div class="form-group">
                    <label>Workshop:</label>
                    <input type="text" class="form-control" value="${plan.department.name}" readonly />
                </div>

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
                                            <input type="hidden" name="date" value="${date}" />
                                            <input type="hidden" name="shiftId" value="${shift}" />
                                            <input type="number" class="form-control" style="width: 80px;" name="quantity_${planHeader.id}_${date}_${shift}" value="${currentValue}" min="0" />
                                        </td>
                                    </c:forEach>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Submit button -->
                <div class="text-center">
                    <button type="submit" class="btn btn-primary btn-lg">Save</button>
                </div>
            </form>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
