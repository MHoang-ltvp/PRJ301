/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import controller.WorkAssigmentList;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.WorkAssignments;
import java.sql.*;
import model.Employees;
import model.PlanDetails;
import model.PlanHeaders;
import model.Products;

/**
 *
 * @author hoang
 */
public class WorkAssignmentDBContext extends DBContext<WorkAssignments> {

    @Override
    public void insert(WorkAssignments model) {
        String sql = """
                     INSERT INTO [WorkAssignments]
                                ([pdid]
                                ,[eid]
                                ,[quantity])
                          VALUES
                                (?
                                ,?
                                ,?)""";
        try {
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setInt(1, model.getPlanDetail().getId());
            stm.setInt(2, model.getEmployees().getId());
            stm.setInt(3, model.getQuantity());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(WorkAssignments model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(WorkAssignments model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ArrayList<WorkAssignments> list(int plid) {
        String sql = """
                     SELECT  e.eid,e.ename,p.pname,p.pid,pd.[sid],w.quantity
                       FROM WorkAssignments w join Employees e on w.eid = e.eid
                     \t\tjoin PlanDetails pd on pd.pdid = w.pdid
                     \t\tjoin PlanHeaders ph on pd.phid = ph.phid
                     \t\tjoin Products p on p.pid = ph  .pid
                     where plid = ?""";
        ArrayList<WorkAssignments> list = new ArrayList<>();
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, plid);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                WorkAssignments w = new WorkAssignments();
                Employees e = new Employees();
                PlanDetails pd = new PlanDetails();
                PlanHeaders ph = new PlanHeaders();
                Products p = new Products();
                e.setId(rs.getInt("eid"));
                e.setEname(rs.getString("ename"));
              
                pd.setShiftId(rs.getInt("sid"));
                
                p.setId(rs.getInt("pid"));
                p.setName(rs.getString("pname")); 
                
                ph.setProduct(p);
                pd.setPlanHeader(ph);
                w.setQuantity(rs.getInt("quantity"));
                w.setEmployees(e);
                w.setPlanDetail(pd);
                list.add(w);
            }
        } catch (SQLException sQLException) {
        }
        return list;
    }
    
    public static void main(String[] args) {
        WorkAssignmentDBContext db = new WorkAssignmentDBContext();
        ArrayList<WorkAssignments> list = db.list(1);
        System.out.println(list.get(0).getEmployees().getEname());
    }
    @Override
    public WorkAssignments get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<WorkAssignments> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
