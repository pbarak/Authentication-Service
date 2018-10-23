package authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import crypto.Encryption;
import zookeeper.Configuration;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	public static String getMyIdentifier() {
		return Configuration.getMyIdentifier();
	}
	private static final long serialVersionUID = 1L;
	 static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";    
	    static final String DB_URL = Database.getURL();

	    //  Database credentials
	    static final String USER = Database.getUsername();
	    static final String PASS = Database.getPassword();
	
	Connection conn=null;
	PreparedStatement stmt=null;
	

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		
		try {
			;

				String systemIdentifier = request.getParameter("system");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String callback = request.getParameter("callback");
				
				
				if (systemIdentifier == null  || systemIdentifier.equals("") ) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Missing system");
					response.sendRedirect("login.jsp");
					return;
				}
				

				
				if ( username == null || password == null ||username.equals("") || password.equals("") ) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Missing username or password");
					response.sendRedirect("login.jsp");
					return;
				}
				
				Map system = Systems.getSystem(systemIdentifier);
				if (system == null) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Unknown system");
					response.sendRedirect("login.jsp?");
					return;
				}
				
				Map userData = Validation.validateUser(username, password);
				if (userData == null) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Wrong username or password");
					response.sendRedirect("login.jsp?system="+systemIdentifier);
					return;
				}
				
				
				JSONObject dataJsonObj = new JSONObject();
				
				dataJsonObj.put("AUTHID", this.getMyIdentifier());
				dataJsonObj.put("SID", systemIdentifier);
				dataJsonObj.put("userid", username+"@"+this.getMyIdentifier());
				dataJsonObj.put("validtill", (int)(System.currentTimeMillis()/1000)) ;
				JSONObject usermeta = new JSONObject();
				usermeta.put("name", userData.get("name").toString()) ;
				usermeta.put("nick", userData.get("nick").toString()) ;
				dataJsonObj.put("usermeta", usermeta);

				String sharedKeyBase64 = system.get("keybase64").toString(); 
				byte[] sharedKey = Base64.getDecoder().decode(sharedKeyBase64);
				String cipher = "AES/CBC/PKCS5Padding";

				String crypted = Encryption.encrypt(dataJsonObj.toString(), sharedKey, cipher);
				
				JSONObject tokenJsonObj = new JSONObject();
				tokenJsonObj.put("error", "");
				tokenJsonObj.put("issuer", this.getMyIdentifier());
				tokenJsonObj.put("crypted", crypted);

				String token = tokenJsonObj.toString();
				

				
				String finalResponse = token.toString();
			
				if (callback == null || callback.equals("")) {
					response.setContentType("text/plain;charset=UTF-8");
					String url = "login.jsp"+"?token="+URLEncoder.encode(finalResponse,"UTF-8");
					response.sendRedirect(url);;
				}
				else {		
						response.setContentType("text/plain;charset=UTF-8");
						String url = callback+"?token="+URLEncoder.encode(finalResponse,"UTF-8");
						response.sendRedirect(url);
						return;
				}
				
			} 
			catch (GeneralSecurityException ex) {
				response.setContentType("text/html;charset=UTF-8");
				session.setAttribute("flash", "GeneralSecurityException");
				response.sendRedirect("login.jsp");
				return;
			} 
			catch (java.io.UnsupportedEncodingException ex) {
				response.setContentType("text/html;charset=UTF-8");
				session.setAttribute("flash", "UnsupportedEncodingException");
				response.sendRedirect("login.jsp");
				return;
			} 
			finally {
				out.close();  // Always close the output writer
			}
		}
				
}