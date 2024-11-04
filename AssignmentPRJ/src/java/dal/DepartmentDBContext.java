/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Departments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentDBContext extends DBContext<Departments> {

    @Override
    public void insert(Departments model) {
        try {
            String sql = "INSERT INTO Departments (dname, type) VALUES (?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, model.getName());
            stm.setString(2, model.getType());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                model.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Departments model) {
        try {
            String sql = "UPDATE Departments SET dname = ?, type = ? WHERE did = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, model.getName());
            stm.setString(2, model.getType());
            stm.setInt(3, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Departments model) {
        try {
            String sql = "DELETE FROM Departments WHERE did = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<Departments> list() {
        ArrayList<Departments> departments = new ArrayList<>();
        try {
            String sql = "SELECT did, dname, type FROM Departments";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Departments d = new Departments();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                d.setType(rs.getString("type"));
                departments.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return departments;
    }

    @Override
    public Departments get(int id) {
        Departments department = null;
        try {
            String sql = "SELECT did, dname, type FROM Departments WHERE did = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                department = new Departments();
                department.setId(rs.getInt("did"));
                department.setName(rs.getString("dname"));
                department.setType(rs.getString("type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return department;
    }

    public ArrayList<Departments> getProductionWorkshops() {
        ArrayList<Departments> workshops = new ArrayList<>();
        try {
            String sql = "SELECT did, dname, type FROM Departments WHERE type = 'Production'";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Departments d = new Departments();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                d.setType(rs.getString("type"));
                workshops.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return workshops;
    }
}
