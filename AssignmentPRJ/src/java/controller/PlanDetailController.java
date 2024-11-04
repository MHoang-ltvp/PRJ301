package controller;

import dal.PlanDetailDBContext;
import dal.PlanHeaderDBContext;
import dal.ProductionPlanDBContext;
import model.PlanDetails;
import model.PlanHeaders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Plans;

public class PlanDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin plan header từ DB
            String rawPlanId = request.getParameter("plid");
            if (rawPlanId == null || rawPlanId.isEmpty()) {
                request.getRequestDispatcher("/view/plan/searchForDetail.jsp").forward(request, response);
                return;
            }
            
    
            int planId = Integer.parseInt(rawPlanId);

            PlanHeaderDBContext planHeaderDB = new PlanHeaderDBContext();
            ProductionPlanDBContext PlanDB = new ProductionPlanDBContext();
            PlanDetailDBContext planDetailDB = new PlanDetailDBContext();

            List<PlanHeaders> planHeaders = planHeaderDB.getPlanHeadersByPlanId(planId);
            Plans plan = PlanDB.get(planId);

            if (plan == null) {
                response.sendRedirect(request.getContextPath() + "/productionplan/detail?error=PlanNotFound");
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
            request.getRequestDispatcher("../view/plan/detail.jsp").forward(request, response);
        } catch (ServletException | IOException | NumberFormatException e) {
            Logger.getLogger(PlanDetailController.class.getName()).log(Level.SEVERE, null, e);
            response.sendRedirect(request.getContextPath() + "/productionplan/detail?error=true");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PlanDetailDBContext planDetailDB = new PlanDetailDBContext();

            // Lấy tất cả các tham số từ form
            Enumeration<String> parameterNames = request.getParameterNames();
            List<PlanDetails> detailsList = new ArrayList<>();

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();

                // Kiểm tra nếu parameter là quantity và nó không trống
                if (paramName.startsWith("quantity_")) {
                    String value = request.getParameter(paramName);

                    // Nếu người dùng không nhập giá trị, tiếp tục với vòng lặp tiếp theo
                    if (value == null || value.isEmpty()) {
                        continue;
                    }

                    // Tách tên parameter để lấy ra các thông tin cần thiết
                    String[] parts = paramName.split("_");
                    int planHeaderId = Integer.parseInt(parts[1]);
                    String date = parts[2];
                    String shift = parts[3];

                    // Tạo đối tượng PlanDetails để lưu vào database
                    PlanDetails detail = new PlanDetails();
                    PlanHeaders header = new PlanHeaders();
                    header.setId(planHeaderId);
                    detail.setPlanHeader(header);
                    detail.setShiftId(getShiftIdFromShiftName(shift)); // Phương thức ánh xạ shift name thành shift ID
                    detail.setDate(Date.valueOf(date));
                    detail.setQuantity(Integer.parseInt(value));

                    detailsList.add(detail);
                }
            }

            // Lưu tất cả các chi tiết vào cơ sở dữ liệu (Insert hoặc Update)
            for (PlanDetails detail : detailsList) {
                if (planDetailDB.exists(detail)) {
                    planDetailDB.update(detail);
                } else {
                    planDetailDB.insert(detail);
                }
            }

//            // Chuyển hướng sau khi lưu thành công
//            response.sendRedirect(request.getContextPath() + "/productionplan/detail?success=true");

            // Chuyển hướng sau khi lưu thành công
            response.sendRedirect(request.getContextPath() + "/productionplan/detail");

        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(PlanDetailController.class.getName()).log(Level.SEVERE, null, e);
            response.sendRedirect(request.getContextPath() + "/productionplan/detail?error=true");
        }
    }

    public static int getShiftIdFromShiftName(String shift) {
        return switch (shift) {
            case "k1" ->
                1;
            case "k2" ->
                2;
            case "k3" ->
                3;
            default ->
                -1;
        }; // Giá trị không hợp lệ
    }

}
