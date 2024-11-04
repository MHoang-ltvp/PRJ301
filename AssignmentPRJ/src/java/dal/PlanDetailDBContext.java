package dal;

import model.PlanDetails;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PlanHeaders;
import java.sql.Date;

public class PlanDetailDBContext extends DBContext<PlanDetails> {

    @Override
    public void insert(PlanDetails model) {
        try {
            String sql = "INSERT INTO PlanDetails (phid, sid, date, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getPlanHeader().getId());
            stm.setInt(2, model.getShiftId());
            stm.setDate(3, model.getDate());
            stm.setInt(4, model.getQuantity());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(PlanDetails model) {
        String sql = "UPDATE PlanDetails SET quantity = ? WHERE phid = ? AND sid = ? AND date = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, model.getQuantity());
            stm.setInt(2, model.getPlanHeader().getId());
            stm.setInt(3, model.getShiftId());
            stm.setDate(4, model.getDate());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(PlanDetails model) {
        // Implement delete logic if needed
    }

    @Override
    public PlanDetails get(int id) {
        // Implement get logic if needed
        return null;
    }

    @Override
    public ArrayList<PlanDetails> list() {
        // Implement list logic if needed
        return null;
    }

    public boolean exists(PlanDetails detail) {
        String sql = "SELECT COUNT(*) AS count FROM PlanDetails WHERE phid = ? AND sid = ? AND date = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, detail.getPlanHeader().getId());
            stm.setInt(2, detail.getShiftId());
            stm.setDate(3, detail.getDate());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int getID(int plid, int sid, Date date, int pid) {
        String sql = """
                     SELECT  [pdid]
                           ,pd.[phid]
                           ,[sid]
                           ,[date]
                           ,pd.[quantity]
                     \t  ,ph.pid
                       FROM PlanDetails pd join PlanHeaders ph on pd.phid = ph.phid 
                     \tjoin Plans p on ph.plid = p.plid
                       where p.plid = ? and sid= ? and  date= ? and pid = ? """;
        int n = -1;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, plid);
            stm.setInt(2,sid );
            stm.setDate(3, date);
            stm.setInt(4, pid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                n = rs.getInt("pdid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
    
    public List<PlanDetails> getPlanDetailsByPlanId(int planId) {
        List<PlanDetails> planDetailsList = new ArrayList<>();
        String sql = "SELECT pd.phid, pd.sid, pd.date, pd.quantity "
                + "FROM PlanDetails pd "
                + "JOIN PlanHeaders ph ON pd.phid = ph.phid "
                + "WHERE ph.plid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, planId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PlanDetails planDetail = new PlanDetails();

                // Set PlanHeader ID
                PlanHeaders planHeader = new PlanHeaders();
                planHeader.setId(rs.getInt("phid"));
                planDetail.setPlanHeader(planHeader);

                // Set shift ID, date, and quantity
                planDetail.setShiftId(rs.getInt("sid"));
                planDetail.setDate(rs.getDate("date"));
                planDetail.setQuantity(rs.getInt("quantity"));

                planDetailsList.add(planDetail);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return planDetailsList;
    }
}
