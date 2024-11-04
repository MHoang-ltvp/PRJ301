package controller;

import dal.DepartmentDBContext;
import dal.PlanHeaderDBContext;
import dal.ProductDBContext;
import dal.ProductionPlanDBContext;
import model.Departments;
import model.PlanHeaders;
import model.Plans;
import model.Products;

import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PlanCreationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy danh sách phòng ban sản xuất và sản phẩm
        DepartmentDBContext departmentDB = new DepartmentDBContext();
        ProductDBContext productDB = new ProductDBContext();

        request.setAttribute("workshops", departmentDB.getProductionWorkshops());
        request.setAttribute("products", productDB.list());

        // Chuyển tiếp đến trang JSP để hiển thị form
        request.getRequestDispatcher("../view/plan/create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin kế hoạch từ form
            String planName = request.getParameter("planName");
            String rawStartDate = request.getParameter("startDate");
            String rawEndDate = request.getParameter("endDate");
            String rawDepartmentId = request.getParameter("departmentId");

            // Chuyển đổi dữ liệu từ form
            Date startDate = Date.valueOf(rawStartDate);
            Date endDate = Date.valueOf(rawEndDate);
            int departmentId = Integer.parseInt(rawDepartmentId);

            // Tạo đối tượng kế hoạch
            Plans plan = new Plans();
            plan.setName(planName);
            plan.setStart(startDate);
            plan.setEnd(endDate);

            Departments department = new Departments();
            department.setId(departmentId);
            plan.setDepartment(department);

            // Chèn kế hoạch vào cơ sở dữ liệu
            ProductionPlanDBContext planDB = new ProductionPlanDBContext();
            planDB.insert(plan);

            // Lấy thông tin sản phẩm từ form
            String[] productIds = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");
            String[] estimatedEfforts = request.getParameterValues("estimatedEffort");

            // Chèn các PlanHeader cho mỗi sản phẩm
            PlanHeaderDBContext headerDB = new PlanHeaderDBContext();
            for (int i = 0; i < productIds.length; i++) {
                if (!productIds[i].isEmpty() && !quantities[i].isEmpty() && !estimatedEfforts[i].isEmpty()) {
                    int productId = Integer.parseInt(productIds[i]);
                    int quantity = Integer.parseInt(quantities[i]);
                    float estimatedEffort = Float.parseFloat(estimatedEfforts[i]);

                    // Tạo đối tượng PlanHeader
                    PlanHeaders header = new PlanHeaders();
                    header.setPlan(plan);

                    Products product = new Products();
                    product.setId(productId);
                    header.setProduct(product);

                    header.setQuantity(quantity);
                    header.setEstimatedEffort(estimatedEffort);

                    // Chèn PlanHeader vào cơ sở dữ liệu
                    headerDB.insert(header);
                }
            }

            // Sau khi xử lý xong, chuyển hướng về trang tạo kế hoạch
            response.sendRedirect(request.getContextPath() + "/productionplan/create?success=true");

        } catch (IllegalArgumentException e) {
            Logger.getLogger(PlanCreationController.class.getName()).log(Level.SEVERE, "Error processing request: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/productionplan/create?error=true");
        }
    }
}
