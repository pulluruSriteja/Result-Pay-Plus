package com.servlet.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@WebServlet("/Register")
public class Register extends HttpServlet {

    // Create the query
    private static final String INSERT_QUERY = "INSERT INTO Register(Uname, Email, Password, CPassword) VALUES (?,?,?,?)";
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get PrintWriter
        PrintWriter pw = res.getWriter();
        // Set content type
        res.setContentType("text/html");
        // Read the form values
        String Rollno = req.getParameter("Rollno");
        String emailID = req.getParameter("Email");
        String Password = req.getParameter("Password");
        String ConfirmPassword = req.getParameter("ConfirmPassword");

        // Check if passwords match
       if (!Password.equals(ConfirmPassword)) {
            pw.println("Passwords do not match.");
            return;
        }

        // Load the JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Establish the connection with the database
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Resultpayplus", "root", "au12345");
             PreparedStatement ps = con.prepareStatement(INSERT_QUERY);) {

            // Set the values
        	ps.setString(1, Rollno);
            ps.setString(2, emailID);
            ps.setString(3, Password);
            ps.setString(4, ConfirmPassword);

            // Execute the query
            int count = ps.executeUpdate();
            if (count == 0) {
                pw.println("Registration failed.Try again!!!");
            } else {
            	String redirectURL = "Successful.html";
                res.sendRedirect(redirectURL);
            }

        } catch (SQLException se) {
            pw.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            pw.println(e.getMessage());
            e.printStackTrace();
        }
        // Close the stream
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
  }