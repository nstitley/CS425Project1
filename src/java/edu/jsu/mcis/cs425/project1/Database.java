package edu.jsu.mcis.cs425.project1;

import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.simple.JSONObject;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Database {
        
        String id;
    
    private Connection getConn() {
        
        Connection conn = null;
        
        try {
            
            Context envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource source = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = source.getConnection();
            
        }   
        
        catch (SQLException | NamingException e) {}
        
        return conn;

    }
    
    public String getResults(String id) {
        
        this.id = id;
        ResultSetMetaData metadata = null;
        
        String key;
        String tableheading;
        StringBuilder dataTb = new StringBuilder();
        String query;
        
        Connection conn = getConn();
        
        query = "SELECT * FROM registrations r WHERE sessionid = ?;";
        
        try {
            
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            
            boolean hasResults = statement.execute();
            
            if (hasResults) {
                
                ResultSet resultset = statement.getResultSet();           
                metadata = resultset.getMetaData();
                
                int numberOfColumns = metadata.getColumnCount();
            
                tableheading = "<tr>";               
                dataTb.append("<table>");
                
                for (int i = 1; i <= numberOfColumns; i++) {
            
                    key = metadata.getColumnLabel(i);               
                    tableheading += "<th>" + key + "</th>";
            
                }   
            
                dataTb.append(tableheading);
                
                while (resultset.next()) {
                    
                    dataTb.append("<tr>");
                    dataTb.append("<td>").append(resultset.getString("id")).append("</td>");
                    dataTb.append("<td>").append(resultset.getString("firstname")).append("</td>");
                    dataTb.append("<td>").append(resultset.getString("lastname")).append("</td>");
                    dataTb.append("<td>").append(resultset.getString("displayname")).append("</td>");
                    dataTb.append("<td>").append(id).append("</td>");
                    dataTb.append("</tr>");
                    
                }
                
                dataTb.append("</table>");
            }
        }
        
        catch(SQLException e){System.err.println(e);}
        
        return (dataTb.toString());
        
    }
    
    public String addReg(String first, String last, String dpname, String id) {
        
        ResultSet list;
        int stat; 
        String query;
        String name;
        String sessionid = "";
        String code;
        
        JSONObject results = new JSONObject();
        
        Connection conn = getConn();
        
        query = ("INSERT INTO registrations (firstname, lastname, displayname, sessionid) values (?, ?, ?, ?);");
        
        try {
            
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, first);
            statement.setString(2, last);
            statement.setString(3, dpname);
            statement.setString(4, id);
            
            stat = statement.executeUpdate();
            
            if (stat == 1) {
                
                list = statement.getGeneratedKeys();
                
                if (list.next()) {
                    
                    sessionid = list.getString(1);
                    
                }
                
            }
            
            results.put("displayname", dpname);
            
            code = String.format("R" + "%0" + (6-sessionid.length()) + "d%s", 0, sessionid);
            results.put("code", code);           
        }
        
        catch(SQLException e){}
        
        return results.toJSONString();
    }
    
}
