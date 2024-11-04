package dal;

import model.PlanHeaders;
import model.Plans;
import model.Departments;
import model.Products;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanHeaderDBContext extends DBContext<PlanHeaders> {

    @Override
    public void insert(PlanHeaders model) {
        try {
            String sql = "INSERT INTO PlanHeaders (plid, pid, quantity, estimatedeffort) VALUES (?, ?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setInt(1, model.getPlan().getId());
            stm.setInt(2, model.getProduct().getId());
            stm.setInt(3, model.getQuantity());
            stm.setFloat(4, model.getEstimatedEffort());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                model.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(PlanHeaders model) {
        try {
            String sql = "UPDATE PlanHeaders SET plid = ?, pid = ?, quantity = ?, estimatedeffort = ? WHERE phid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getPlan().getId());
            stm.setInt(2, model.getProduct().getId());
            stm.setInt(3, model.getQuantity());
            stm.setFloat(4, model.getEstimatedEffort());
            stm.setInt(5, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(PlanHeaders model) {
        try {
            String sql = "DELETE FROM PlanHeaders WHERE phid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<PlanHeaders> list() {
        ArrayList<PlanHeaders> headers = new ArrayList<>();
        try {
            String sql = "SELECT phid, plid, pid, quantity, estimatedeffort FROM PlanHeaders";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PlanHeaders header = new PlanHeaders();
                header.setId(rs.getInt("phid"));

                Plans plan = new Plans();
                plan.setId(rs.getInt("plid"));
                header.setPlan(plan);

                Products product = new Products();
                product.setId(rs.getInt("pid"));
                header.setProduct(product);

                header.setQuantity(rs.getInt("quantity"));
                header.setEstimatedEffort(rs.getFloat("estimatedeffort"));
                headers.add(header);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return headers;
    }

    @Override
    public PlanHeaders get(int id) {
        PlanHeaders header = null;
        try {
            String sql = "SELECT phid, plid, pid, quantity, estimatedeffort FROM PlanHeaders WHERE phid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                header = new PlanHeaders();
                header.setId(rs.getInt("phid"));

                Plans plan = new Plans();
                plan.setId(rs.getInt("plid"));
                header.setPlan(plan);

                Products product = new Products();
                product.setId(rs.getInt("pid"));
                header.setProduct(product);

                header.setQuantity(rs.getInt("quantity"));
                header.setEstimatedEffort(rs.getFloat("estimatedeffort"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return header;
    }

    public List<PlanHeaders> getPlanHeadersByPlanId(int planId) {
        List<PlanHeaders> planHeaders = new ArrayList<>();
        try {
            String sql = "SELECT ph.phid, ph.plid, ph.pid, ph.quantity, ph.estimatedeffort, "
                    + "p.plname, p.startdate, p.enddate, p.did "
                    + "FROM PlanHeaders ph "
                    + "JOIN Plans p ON ph.plid = p.plid "
                    + "WHERE ph.plid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, planId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PlanHeaders header = new PlanHeaders();
                header.setId(rs.getInt("phid"));
                
                //set plan into header
                Plans plan = new Plans();
                plan.setId(rs.getInt("plid"));
                plan.setName(rs.getString("plname"));
                plan.setStart(rs.getDate("startdate"));
                plan.setEnd(rs.getDate("enddate"));
                Departments dept = new Departments();
                dept.setId(rs.getInt("did"));
                
                //set product into 
                ProductDBContext productdb = new ProductDBContext();
                header.setPlan(plan);
                Products product = productdb.get(rs.getInt("pid"));
                header.setProduct(product);

                header.setQuantity(rs.getInt("quantity"));
                header.setEstimatedEffort(rs.getFloat("estimatedeffort"));
                planHeaders.add(header);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return planHeaders;
    }

//    public Plans getPlanById(int planId) {
//        Plans plan = null;
//        try {
//            String sql = "SELECT plid, plname, startdate, enddate, did FROM Plans WHERE plid = ?";
//            PreparedStatement stm = connection.prepareStatement(sql);
//            stm.setInt(1, planId);
//            ResultSet rs = stm.executeQuery();
//            if (rs.next()) {
//                plan = new Plans();
//                plan.setId(rs.getInt("plid"));
//                plan.setName(rs.getString("plname"));
//                plan.setStart(rs.getDate("startdate"));
//                plan.setEnd(rs.getDate("enddate"));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return plan;
//    }
//    
//    public static void main(String[] args) {
//        PlanHeaderDBContext db = new PlanHeaderDBContext();
//        Plans p = db.getPlanById(1);
//        System.out.println(p.getEnd());
//    }
}
