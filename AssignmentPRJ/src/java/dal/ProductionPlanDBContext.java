/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Plans;
import model.Departments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductionPlanDBContext extends DBContext<Plans> {

    @Override
    public void insert(Plans model) {
        try {
            String sql = "INSERT INTO Plans (plname, startdate, enddate, did) VALUES (?, ?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, model.getName());
            stm.setDate(2, model.getStart());
            stm.setDate(3, model.getEnd());
            stm.setInt(4, model.getDepartment().getId());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                model.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductionPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Plans model) {
        try {
            String sql = "UPDATE Plans SET plname = ?, startdate = ?, enddate = ?, did = ? WHERE plid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, model.getName());
            stm.setDate(2, model.getStart());
            stm.setDate(3, model.getEnd());
            stm.setInt(4, model.getDepartment().getId());
            stm.setInt(5, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductionPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Plans model) {
        try {
            String sql = "DELETE FROM Plans WHERE plid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductionPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<Plans> list() {
        ArrayList<Plans> plans = new ArrayList<>();
        try {
            String sql = "SELECT plid, plname, startdate, enddate, did FROM Plans";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Plans plan = new Plans();
                plan.setId(rs.getInt("plid"));
                plan.setName(rs.getString("plname"));
                plan.setStart(rs.getDate("startdate"));
                plan.setEnd(rs.getDate("enddate"));

                Departments dept = new Departments();
                dept.setId(rs.getInt("did"));
                plan.setDepartment(dept);
                plans.add(plan);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductionPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return plans;
    }

    @Override
    public Plans get(int id) {
        Plans plan = null;
        try {
            String sql = """
                         select * from Plans p join Departments d on p.did = d.did
                         where p.plid = ?""";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                plan = new Plans();
                plan.setId(rs.getInt("plid"));
                plan.setName(rs.getString("plname"));
                plan.setStart(rs.getDate("startdate"));
                plan.setEnd(rs.getDate("enddate"));

                Departments dept = new Departments();
                dept.setName(rs.getString("dname"));
                plan.setDepartment(dept);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductionPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return plan;
    }

    public static void main(String[] args) {
        ProductionPlanDBContext db = new ProductionPlanDBContext();
        System.out.println(db.get(1).getDepartment().getName());
    }
}
