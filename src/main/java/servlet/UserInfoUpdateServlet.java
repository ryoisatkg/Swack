package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.LoginModel;
import model.UserModel;

/**
 * Servlet implementation class UserInfoUpdateServlet
 */
@WebServlet("/UserInfoUpdateServlet")
public class UserInfoUpdateServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/edituserinfo.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: 空白の名前、空白のパスワード、空白のメールアドレスだった場合はじくようにする
		User user = (User)request.getSession().getAttribute("user");
		
		String userid = user.getUserId();
		String name = request.getParameter("name");
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		
		StringBuilder errorMsg = new StringBuilder();
		if (name == null || name.length() == 0) {
			errorMsg.append("名前が入っていません<br>");
		}
		if (mail == null || mail.length() == 0) {
			errorMsg.append("メールアドレスが入っていません<br>");
		}
		if (password == null || password.length() == 0) {
			errorMsg.append("パスワードが入っていません<br>");
		}
		
		if (errorMsg.length() > 0) {
			// ログイン画面に戻してエラーメッセージを表示する
			request.setAttribute("errmsg", errorMsg.toString());
			request.getRequestDispatcher("/WEB-INF/jsp/edituserinfo.jsp").forward(request, response);
			return;
		}
		
		UserModel um = new UserModel();
		try {
			if (um.existsWithoutMe(mail, userid)) {
				request.setAttribute("errmsg", "もうすでに同一のメールアドレスのユーザが設定されています");
				request.getRequestDispatcher("/WEB-INF/jsp/edituserinfo.jsp").forward(request, response);
				return;
			}
			
			um.updateUserInfo(userid, name, mail, password);
			User newuserinfo = new LoginModel().checkLogin(mail, password);
			request.getSession().setAttribute("user", newuserinfo);
			request.setAttribute("errmsg", "設定に成功しました");
			request.getRequestDispatcher("/WEB-INF/jsp/edituserinfo.jsp").forward(request, response);
			return;
			
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errmsg", "DBエラーです");
			request.getRequestDispatcher("/WEB-INF/jsp/edituserinfo.jsp").forward(request, response);
		}
	}

}
