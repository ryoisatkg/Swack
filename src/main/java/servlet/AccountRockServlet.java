package servlet;

import static parameter.Messages.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import exception.SwackException;
import model.LoginModel;

/**
 * Servlet implementation class AccountRockServlet
 */
@WebServlet("/AccountRockServlet")
public class AccountRockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AccountRockServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// メアドとパスワードをフォームからもらう
		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");
		
		int count=0;
		// 処理
		try {
			// ログインチェック
			User user = new LoginModel().checkLogin(mailAddress, password);
			if (user == null) {
				// 認証失敗
				request.setAttribute("errorMsg", ERR_LOGIN_PARAM_MISTAKE);
				request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
				count+=1;
				return;
			} else {
				// 認証成功(ログイン情報をセッションに保持)
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect("MainServlet");
				return;
			}

		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
