/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PlanDetailDBContext;
import dal.PlanHeaderDBContext;
import dal.ProductionPlanDBContext;
import dal.WorkAssignmentDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PlanDetails;
import model.PlanHeaders;
import model.Plans;
import java.sql.Date;
import model.Employees;
import model.WorkAssignments;

/**
 *
 * @author hoang
 */
public class WorkAssignmentController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet WorkAssignmentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet WorkAssignmentController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin plan header từ DB
            String rawPlanId = request.getParameter("plid");
            if (rawPlanId == null || rawPlanId.isEmpty()) {
                request.getRequestDispatcher("/view/plan/searchForWorkAssignment.jsp").forward(request, response);
//                return;
            }

            int planId = Integer.parseInt(rawPlanId);

            PlanHeaderDBContext planHeaderDB = new PlanHeaderDBContext();
            ProductionPlanDBContext PlanDB = new ProductionPlanDBContext();
            PlanDetailDBContext planDetailDB = new PlanDetailDBContext();

            List<PlanHeaders> planHeaders = planHeaderDB.getPlanHeadersByPlanId(planId);
            Plans plan = PlanDB.get(planId);

            if (plan == null) {
                response.sendRedirect(request.getContextPath() + "/productionplan/workassignment?error=PlanNotFound");
                return;
            }

            // Tính toán danh sách các ngày từ startDate đến endDate
            List<java.sql.Date> dateList = new ArrayList<>();
            java.sql.Date startDate = plan.getStart();
            java.sql.Date endDate = plan.getEnd();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            //tiếp tục duyệt cho đến khi date vượt quá endDate
            while (!calendar.getTime().after(endDate)) {
                dateList.add(new java.sql.Date(calendar.getTimeInMillis()));
                calendar.add(Calendar.DATE, 1);
            }

            // Thêm thông tin về ca làm việc (shift)
            List<String> shifts = new ArrayList<>();
            shifts.add("k1");
            shifts.add("k2");
            shifts.add("k3");
            // Lấy thông tin plan details từ DB
            List<PlanDetails> planDetailsList = planDetailDB.getPlanDetailsByPlanId(planId);

            // Gửi danh sách plan headers, sản phẩm, danh sách ngày, và danh sách ca làm việc đến JSP
            request.setAttribute("planHeaders", planHeaders);
            request.setAttribute("plan", plan);
            request.setAttribute("dateList", dateList);
            request.setAttribute("shifts", shifts);
            request.setAttribute("planDetailsList", planDetailsList);

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("../view/plan/workAssign.jsp").forward(request, response);
        } catch (ServletException | IOException | NumberFormatException e) {
            Logger.getLogger(PlanDetailController.class.getName()).log(Level.SEVERE, null, e);
            response.sendRedirect(request.getContextPath() + "/productionplan/workassignment?error=true");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
//            int totalRows = Integer.parseInt(request.getParameter("totalRows"));
            
            PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
            WorkAssignmentDBContext workAssignmentDB = new WorkAssignmentDBContext();
            String plid = request.getParameter("plid");
            
            //fix cứng code
            for (int i = 1; i <= 50; i++) {
                String dateStr = request.getParameter("date_" + i);
                String employeeIdStr = request.getParameter("employeeId_" + i);
                String shiftIdStr = request.getParameter("shiftId_" + i);
                String productIdStr = request.getParameter("productId_" + i);
                String quantityStr = request.getParameter("quantity_" + i);

                if (dateStr == null || employeeIdStr == null || shiftIdStr == null || productIdStr == null || quantityStr == null
                        || dateStr.isEmpty() || employeeIdStr.isEmpty() || shiftIdStr.isEmpty() || productIdStr.isEmpty() || quantityStr.isEmpty()) {
                    continue;
                }

                Date date = Date.valueOf(dateStr);
                int employeeId = Integer.parseInt(employeeIdStr);
                int shiftId = Integer.parseInt(shiftIdStr);
                int productId = Integer.parseInt(productIdStr);
                int quantity = Integer.parseInt(quantityStr);

                int pdid = planDetailDB.getID(Integer.parseInt(plid), shiftId, date, productId);
                PlanDetails pd = new PlanDetails();
                pd.setId(pdid);

                Employees emp = new Employees();
                emp.setId(employeeId);

                WorkAssignments workAssignment = new WorkAssignments();
                workAssignment.setPlanDetail(pd);
                workAssignment.setEmployees(emp);
                workAssignment.setQuantity(quantity);
                workAssignmentDB.insert(workAssignment);

            }

            // Chuyển hướng sau khi lưu thành công
            response.sendRedirect(request.getContextPath() + "/productionplan/workassignment");
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(PlanDetailController.class.getName()).log(Level.SEVERE, null, e);
            response.sendRedirect(request.getContextPath() + "/productionplan/workassignment?error=true");
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
