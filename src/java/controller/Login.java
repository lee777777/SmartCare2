/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author THINKPAD
 */
public class Login extends SimpleFormController{
               public Login()
           {
            setCommandClass(User.class);
            setCommandName("loginUser"); 
           }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        //return super.onSubmit(request, response, command, errors); //To change body of generated methods, choose Tools | Templates.
    User user=(User)command;
    ModelAndView modelandview;
    HttpSession session=request.getSession(); 
    String type=user.getType();
    String userName=user.getUserName();
    String password=user.getPassword();
    String message="UserName OR Password is Incorrect";
        
                Connection conn=DriverManager.getConnection("jdbc:derby://localhost:1527/smartCareDB","root","123");
                Statement st=conn.createStatement();
                //select the user: admin=1, customer=2, customer=3.......
                if(type.equals("admin")){
                ResultSet rs=st.executeQuery("SELECT * FROM ROOT.users where ROOT.users.uname='"+userName+"' and ROOT.users.passwd='"+password+"' and ROOT.users.role='admin' ");//db query here
                if(rs.next())
                {
                    
                    session.setAttribute("adminName",userName);  
                      
                       // keep track of session with cookies through a small web application...
//
//                       Cookie loginCookie = new Cookie("userC",userName);
//			//setting cookie to expiry in 30 mins
//			loginCookie.setMaxAge(30*60);
//			response.addCookie(loginCookie);
//			response.sendRedirect("main.jsp");
                        
                        return modelandview=new ModelAndView("main","userA",userName);
                }
                else
                {
                    session.setAttribute("message",message); 
                    response.sendRedirect("adminLogin.htm"); // login here is adminLogin 
                    //return modelandview=new ModelAndView("login");
                    return null;
                }
                }
                // if the user is a customer
                else if (type.equals("client")){
                ResultSet rs2=st.executeQuery("SELECT * FROM ROOT.users where ROOT.users.uname='"+userName+"' and ROOT.users.passwd='"+password+"' and ROOT.users.role='client' ");//db query here
                if(rs2.next())
                {
    
                    session.setAttribute("clientUserName",userName);  
                    return modelandview=new ModelAndView("patientGate","userC",userName);
                }
                else
                {
                    session.setAttribute("message",message); 
                    response.sendRedirect("patientLogin.htm"); 
                    //return modelandview=new ModelAndView("login");
                    return null;
                }
                }
                
                // if the user is deiver
                else if(type.equals("doctor")){
                ResultSet rs3=st.executeQuery("SELECT * FROM ROOT.users where ROOT.users.uname='"+userName+"' and ROOT.users.passwd='"+password+"' and ROOT.users.role='doctor' ");//db query here
                if(rs3.next())
                {
                    // get the customer id for session
 
                    session.setAttribute("doctorUserName",userName);  
                    return modelandview=new ModelAndView("doctorGate","userD",userName);
                }
                else
                {
                    session.setAttribute("message",message); 
                    response.sendRedirect("doctorLogin.htm"); 
                    //return modelandview=new ModelAndView("login");
                    return null;
                }
                    
                
                }
               return null;
    
    }    
    
}
