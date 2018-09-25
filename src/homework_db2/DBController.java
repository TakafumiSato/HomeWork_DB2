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

/**
 *
 * @author 佐藤孝史
 */
public class DBController {
    
    private Connection con = null;
    private PreparedStatement ps = null;
    
    public DBController() {
        
        connectDataBase();
    }
    
    private void connectDataBase() {
        
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mynumberdb?useUnicode=true&characterEncoding=utf8","TakafumiSato","1234567");
            
            System.out.println("MySQLに接続しました。");
        } catch (SQLException se) {
            System.out.println("MySQLに接続できませんでした。");
            System.out.println(se);
        }
    }
    
    public void joinDataBase() {
        
        ResultSet rs = null;
        
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT staffmaster_table.id,name,gender,birth,myNumber,"
                    + "TIMESTAMPDIFF(YEAR, staffmaster_table.birth, CURDATE()) as age "
                    + "FROM staffmaster_table "
                    + "LEFT OUTER JOIN mynumber_table ON staffmaster_table.id = mynumber_table.id "
                    + "ORDER BY staffmaster_table.birth DESC";
            rs = stmt.executeQuery(sql);
            out.println("従業員コード, 名前, 性別, 生年月日, 年齢, 個人番号");
            while (rs.next()) {
                out.println("   " + rs.getString("id") + ",  " + rs.getString("name") + ", " + rs.getString("gender") + ", " + rs.getString("birth") + ",  " + rs.getString("age") + ", " + rs.getString("myNumber"));
            }
            
            // クローズ
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            out.println("SQLException:" + ex.getMessage());
        }
    }
}
