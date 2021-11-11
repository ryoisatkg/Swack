package servlet;

import static parameter.Messages.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.UserModel;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// フォームから登録する情報を受け取る
		String userName = request.getParameter("userName");
		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");

		// 入力チェック
		StringBuilder errorMsg = new StringBuilder();
		if (userName == null || userName.length() == 0) {
			errorMsg.append("パスワードが入っていません<br>");
		}
		if (mailAddress == null || mailAddress.length() == 0) {
			errorMsg.append("メールアドレスが入っていません<br>");
		}
		if (password == null || password.length() == 0) {
			errorMsg.append("パスワードが入っていません<br>");
		}

		if (errorMsg.length() > 0) {
			// ログイン画面に戻してエラーメッセージを表示する
			request.setAttribute("errorMsg", errorMsg.toString());
			request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
			return;
		}

		// 処理
		try {
			var um = new UserModel();
			if (um.exists(mailAddress)) {
				// 同一のメールアドレスが登録されていたら登録させない
				request.setAttribute("errorMsg", ERR_USERS_ISREGISTERED);
				request.getRequestDispatcher("WEB-INF/jsp/signup.jsp").forward(request, response);
				return;
			}

			// 登録
			String newId = um.createUserId();
			um.registerUsers(new User(newId, userName, mailAddress, password, false));

			// 登録後はログイン画面へ飛ばす
			response.sendRedirect("LoginServlet");

		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("WEB-INF/jsp/signup.jsp").forward(request, response);
		}
	}
}
