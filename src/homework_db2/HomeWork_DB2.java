/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework_db2;

/**
 *
 * @author 佐藤孝史
 */
public class HomeWork_DB2 {

    static DBController dbController = new DBController();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // テーブルマージ出力
        dbController.joinTable();
        
        // 性別と年齢で検索
        dbController.search("男", 31);
        
        // 名前の一部から検索
        dbController.searchName("三");
    }
}
