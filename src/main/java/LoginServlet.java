import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        description = "Login Servlet Testing",
        urlPatterns = {"/LoginServlet"},
        initParams = {
                @WebInitParam(name="user",value = "Amruta"),
                @WebInitParam(name = "password",value = "Bridgelabz#1")
        }
)
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");

        if(!validateUser(user))
        {
            String error = "<font color = red> username must be start with capital and have minimum 3 digits</font>";
            setErrorResponse(req, resp,error);
        }
        else if(!validatePassword(pwd)){
            String error = "<font color = red> password rules " +
                    "1. min 8 chars" +
                    "2. at least 1 upper case, at least 1 lower case,at least 1 number" +
                    "3. at least 1 special character" +
                    " </font>";
            setErrorResponse(req, resp,error);
        }
        else
        {
            // Authenticate user
            if(Authenticate(user, pwd))
            {
                setSuccessResponse(req,resp,user);
            }
            else
            {
                String error = "<font color = red> Either username or password is wrong</font>";
                setErrorResponse(req, resp,error);
            }
        }
    }

    // sets the output error http response
    private void setErrorResponse(HttpServletRequest req, HttpServletResponse resp, String errorHtmlMsg)
            throws ServletException, IOException
    {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
        PrintWriter out = resp.getWriter();
        out.println(errorHtmlMsg);
        rd.include(req,resp);
    }
    private void setSuccessResponse(HttpServletRequest req, HttpServletResponse resp,String user)
            throws ServletException, IOException{
        req.setAttribute("user",user);
        req.getRequestDispatcher("LoginSuccess.jsp").forward(req,resp);
    }

    private boolean Authenticate(String reqUserName, String reqPassword) throws ServletException, IOException{
        String userID = getServletConfig().getInitParameter("user");
        String password = getServletConfig().getInitParameter("password");
        if(userID.equals(reqUserName) && password.equals(reqPassword)){
            return true;
        }
        return  false;
    }

    private static boolean validateUser(String user){
        return user.matches("^[A-Z]+[a-zA-Z]{3,8}");
    }
    private static boolean validatePassword(String password){
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[-+_!@#$%^&*.,?]).+$");
    }
}
