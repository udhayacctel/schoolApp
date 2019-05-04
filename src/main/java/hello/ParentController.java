package hello;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParentController {

	@CrossOrigin()
	@RequestMapping("/getStudentsForParent")
	public String getStudentsForParent(@RequestParam(value="id", defaultValue="0") Long id)
	{
		System.out.println("Hello there starting:" + id.toString());
    	Connection c = null;
    	Statement stmt = null;
    	ResultSet res;
    	String op = "0";
        try {
           Class.forName("org.postgresql.Driver");
           c = DriverManager
              .getConnection("jdbc:postgresql://localhost:5432/postgres",
              "postgres", "odin");
           stmt = c.createStatement();
           /*res = stmt.executeQuery("select * from roles;");
           while (res.next())
           {
        	   System.out.println("Hello id:" + res.getInt("role_id"));
        	   System.out.println("Hello name:" + res.getString("role_name"));
           }*/
           String sql = "select get_students_for_parent(" + id.toString() + ") as st_parent";
           res = stmt.executeQuery(sql);
           
           while (res.next())
           {
        	   if (res.getString("st_parent") != null)
        		   op = res.getString("st_parent");
               System.out.println("Hello there: Opened database successfully:" + op);        	   
           }
	       	res.close();
	        stmt.close();
	        c.close();  
	        
        } catch (Exception e) {
           System.out.println("Hello there:" + e.getClass().getName()+": "+e.getMessage());
        }
        return op;
	}
	
}
