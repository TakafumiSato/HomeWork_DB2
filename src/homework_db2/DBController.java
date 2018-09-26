/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework_db2;

import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 佐藤孝史
 */
public class DBController {
    
    String servername     = "localhost:3306";
    String databasename   = "mynumberdb";
    String user = "TakafumiSato";
    String password = "1234567";
    String serverencoding = "UTF-8";
    String url =  "jdbc:mysql://"+servername+"/" + databasename;
    String sql_SELECT_staffMyNumber = "SELECT staffmaster_table.id,name,gender,DATE_FORMAT(staffmaster_table.birth,'%Y/%m/%d') as birth,IFNULL(myNumber,'') as myNumber,"
                    + "TIMESTAMPDIFF(YEAR, staffmaster_table.birth, CURDATE()) as age "
                    + "FROM staffmaster_table "
                    + "LEFT OUTER JOIN mynumber_table ON staffmaster_table.id = mynumber_table.id "
                    + "ORDER BY staffmaster_table.birth DESC";
    
   // private PreparedStatement ps = null;
    
    public DBController() {
        
    }
    
    public Connection connectDataBase() {
        
        Connection con = null;
        
        try {
            con = DriverManager.getConnection(url+"?useUnicode=true&characterEncoding="+serverencoding,user,password);
            
            System.out.println("MySQLに接続しました。");
        } catch (SQLException se) {
            System.out.println("MySQLに接続できませんでした。");
            System.out.println(se.getMessage());
            
            closeConnection(con);
        }
        
        return con;
    }
    
    public void joinTable() {
        
        Statement stmt = null;
        ResultSet rs = null;
        
        Connection con = connectDataBase();
        
        try {
            stmt = con.createStatement();
            String sql = sql_SELECT_staffMyNumber;
            rs = stmt.executeQuery(sql);
            out.println("従業員コード, 名前, 性別, 生年月日, 年齢, 個人番号");
            while (rs.next()) {
                out.println("   " + rs.getString("id") 
                        + ",  " + rs.getString("name") 
                        + ", " + rs.getString("gender") 
                        + ", " + rs.getString("birth") 
                        + ", " + rs.getString("age") 
                        + ", " + rs.getString("myNumber"));
            }

        } catch (SQLException ex) {
            out.println("SQLException:" + ex.getMessage());
        } finally {
            // クローズ
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(con);
        }
    }
    
    public void search(String gender, int age) {
        
        out.println("性別と年齢で検索("+gender+","+age+"歳以上)");
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // 接続
        Connection con = connectDataBase();
                
        try {
            
            ps = con.prepareStatement("SELECT * FROM (" + sql_SELECT_staffMyNumber + ") as staffMyNumber"
                    + " WHERE staffMyNumber.gender=? AND staffMyNumber.age>=? "
                    + "ORDER BY staffMyNumber.age ASC");

            ps.setString(1,gender);
            ps.setString(2, String.valueOf(age));
            rs = ps.executeQuery();
            
            while (rs.next()) {
                out.println("   " + rs.getString("id") 
                        + ",  " + rs.getString("name") 
                        + ", " + rs.getString("gender") 
                        + ", " + rs.getString("birth") 
                        + ", " + rs.getString("age") 
                        + ", " + rs.getString("myNumber"));
            }
            
        } catch (SQLException ex) {  
            out.println("SQLException:" + ex.getMessage());
        } finally {
            // クローズ
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(con);
        }
    }
    
    public void searchName(String text) {

        out.println("名前の一部からで検索(\""+text+"\"の文字列)");
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // 接続
        Connection con = connectDataBase();
                
        try {
            
            ps = con.prepareStatement("SELECT * FROM (" + sql_SELECT_staffMyNumber + ") as staffMyNumber"
                    + " WHERE staffMyNumber.name LIKE ? "
                    + "ORDER BY staffMyNumber.age ASC");

            ps.setString(1,"%"+text+"%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                out.println("   " + rs.getString("id") 
                        + ",  " + rs.getString("name") 
                        + ", " + rs.getString("gender") 
                        + ", " + rs.getString("birth") 
                        + ", " + rs.getString("age") 
                        + ", " + rs.getString("myNumber"));
            }
            
        } catch (SQLException ex) {  
            out.println("SQLException:" + ex.getMessage());
        } finally {
            // クローズ
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(con);
        }
    }
    
    private void closeConnection(Connection con) {
        
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }
    
    private void closeStatement(Statement stmt) {
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }
    
    private void closePreparedStatement(PreparedStatement ps) {
        
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }
    
    private void closeResultSet(ResultSet rs) {
        
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                out.println(ex.getMessage());
            }
        }
    }
}
