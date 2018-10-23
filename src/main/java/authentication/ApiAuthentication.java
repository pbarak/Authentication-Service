package authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import crypto.Encryption;

/**
 * Servlet implementation class ApiAuthentication
 */
@WebServlet("/ApiAuthentication")
public class ApiAuthentication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiAuthentication() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static String getMyIdentifier() {
  		return "APIAUTH";
  	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		
		
		try {
			;

				String systemIdentifier = request.getParameter("system");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				

				// TODO handle missing system, username or password
				if (systemIdentifier == null || username == null || password == null || systemIdentifier.equals("") || username.equals("") || password.equals("") ) {
					String error="Missing system, username or password";
					JSONObject resJSON = new JSONObject();
					resJSON.put("error", error);			
					JSONObject dataJSON = new JSONObject();		
					resJSON.put("data",dataJSON);
					resJSON.put("issuer", this.getMyIdentifier());
					out.print(resJSON);
					out.flush();
					return;
				}
				
				Map system = Systems.getSystem(systemIdentifier);
				if (system == null) {
					String error="Unknown System";
					JSONObject resJSON = new JSONObject();
					resJSON.put("error", error);			
					JSONObject dataJSON = new JSONObject();	
					resJSON.put("data",dataJSON);	
					resJSON.put("issuer", this.getMyIdentifier());
					out.print(resJSON);
					out.flush();
					return;
				}
				
				Map userData = Validation.validateUser(username, password);
				if (userData == null) {
					String error="Wrong username or password";
					JSONObject resJSON = new JSONObject();
					resJSON.put("error", error);			
					JSONObject dataJSON = new JSONObject();	
					resJSON.put("data",dataJSON);
					resJSON.put("issuer", this.getMyIdentifier());
					out.print(resJSON);
					out.flush();
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
				JSONObject resJSON = new JSONObject();
				resJSON.put("error", "");
				resJSON.put("data", tokenJsonObj);
				out.print(resJSON);
				out.flush();
			
				
			} 
			catch (GeneralSecurityException ex) {
				String error="GeneralSecurityException";
				JSONObject resJSON = new JSONObject();
				resJSON.put("error", error);			
				JSONObject dataJSON = new JSONObject();	
				resJSON.put("data",dataJSON);	
				out.print(resJSON);
				out.flush();
				return;
				
			} 
			catch (java.io.UnsupportedEncodingException ex) {
				String error="UnsupportedEncodingException";
				JSONObject resJSON = new JSONObject();
				resJSON.put("error", error);			
				JSONObject dataJSON = new JSONObject();	
				resJSON.put("data",dataJSON);	
				out.print(resJSON);
				out.flush();
				return;
				
			} 
			finally {
				out.close();  // Always close the output writer
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}