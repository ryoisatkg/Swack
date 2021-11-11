package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import dao.UsersDAO;
import exception.SwackException;

/**
 * Servlet implementation class ForcedOutServlet
 */
@WebServlet("/ForcedOutServlet")
public class ForcedOutServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String userid = u.getUserId();

		if (!userid.equals("U0000")) {
			response.sendRedirect("MainServlet");
			return;
		}

		List<User> users = null;
		try {
			users = ((new UsersDAO()).getUsersWithoutMe(userid));
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		request.setAttribute("users", users);
		request.setAttribute("isRoot", u.getUserId().equals("U0000"));
		request.getRequestDispatcher("/WEB-INF/jsp/forcedout.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String userid = u.getUserId();
		if (!userid.equals("U0000")) {
			response.sendRedirect("MainServlet");
			return;
		}

		String bannedUserId = request.getParameter("forcedOutUserId");
		try {
			new UsersDAO().withdrawalUser(bannedUserId);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		response.sendRedirect("ForcedOutServlet");
	}

}
