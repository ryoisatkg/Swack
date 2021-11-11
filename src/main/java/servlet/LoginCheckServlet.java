package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: userが退会処理されていないか(まだ存在するか)のチェック
		
		
		if (request.getSession().getAttribute("user") == null) {
			response.sendRedirect("LoginServlet");
			return;
		
		}
		super.service(request, response);

	}

}
sadsads