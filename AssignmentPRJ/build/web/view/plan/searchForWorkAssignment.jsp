<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Work Assignment </title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h2 class="text-center mt-5">Search by Plan ID </h2>
        <form action="workassignment" method="GET" class="form-inline justify-content-center mt-4">
            <div class="form-group mb-2">
                <label for="planId" class="sr-only">Plan ID:</label>
                <input type="text" class="form-control" id="planId" name="plid" placeholder="Enter Plan ID" required>
            </div>
            <button type="submit" class="btn btn-primary mb-2 ml-2">Search</button>
        </form>
    </div>
</body>
</html>
