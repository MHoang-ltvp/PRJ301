/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author hoang
 */
package dal;

import model.Products;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDBContext extends DBContext<Products> {

    @Override
    public void insert(Products model) {
        try {
            String sql = "INSERT INTO Products (pname, description) VALUES (?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, model.getName());
            stm.setString(2, model.getDescription());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                model.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Products model) {
        try {
            String sql = "UPDATE Products SET pname = ?, description = ? WHERE pid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, model.getName());
            stm.setString(2, model.getDescription());
            stm.setInt(3, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Products model) {
        try {
            String sql = "DELETE FROM Products WHERE pid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<Products> list() {
        ArrayList<Products> products = new ArrayList<>();
        try {
            String sql = "SELECT pid, pname, description FROM Products";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Products p = new Products();
                p.setId(rs.getInt("pid"));
                p.setName(rs.getString("pname"));
                p.setDescription(rs.getString("description"));
                products.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return products;
    }

    @Override
    public Products get(int id) {
        Products product = null;
        try {
            String sql = "SELECT pid, pname, description FROM Products WHERE pid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                product = new Products();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDescription(rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return product;
    }
}
