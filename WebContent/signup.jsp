<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8" />
        <title>signupForm</title>
        
        <!-- The stylesheet -->
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/design/signupstyle.css" />
        <!--[if lt IE 9]>
          <script src="htpt://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        
       
    </head>
    
    <body>

        <div id="main">
        	
        	<h1>Sign up, it's FREE!</h1>
        	<h2 id="error_text"></h2>
        
	 	<%
		if (session.getAttribute("flash") != null) {
			%>
				<div class="flash" id="error" >
					<h4>
						<%=session.getAttribute("flash")%>
					</h4>
				</div>
			<%
			session.removeAttribute("flash");
		}
		%>
			

        	
        	<form class="" method="post" action="register">
        		
        		<div class="row username">
	    			<input type="text" id="username" name="username" placeholder="username" />
        		</div>
        		
        		<div class="row Name">
	    			<input type="text" id="Name" name="Name" placeholder="Name" />
        		</div>
        		
        		<div class="row nickname">
	    			<input type="text" id="nickname" name="nickname" placeholder="nickname" />
        		</div>
        		
        		<div class="row pass">
        			<input type="password" id="password1" name="password1" placeholder="Password" />
        		</div>
        		
        		<div class="row pass"> 
        			<input type="password" id="password2" name="password2" placeholder="Password (repeat)"  />
        		</div>
        		
        		<!-- The rotating arrow -->
        		<div class="arrowCap"></div>
        		<div class="arrow"></div>
        		
        		<p class="meterText">Password Meter</p>
        		
        		<input type="submit" value="Register" />
        
        		
        	</form>
        </div>
        
        <footer>
	        
            <a class="footer"> &copy; 2018 </a>
        </footer>
        
        <!-- JavaScript includes - jQuery, the complexify plugin and our own script.js -->
		 
		 <script src="https://code.jquery.com/jquery-1.7.2.min.js"></script>
		  <script type="text/javascript" src=scripts/loginscript.js></script> 
		

		     
    </body>
</html>
