package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Room;
import bean.User;
import exception.SwackException;
import model.ChatModel;

/**
 * Servlet implementation class ViewMemberServlet
 */
@WebServlet("/ViewMemberServlet")
public class ViewMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		List<Room> roomInfos = null;
		try {
			roomInfos = new ChatModel().getViewableRoomInfosWithJoined(u.getUserId());
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		request.setAttribute("roomInfos", roomInfos);
		request.getRequestDispatcher("/WEB-INF/jsp/viewroom.jsp").forward(request, response);
	}

}

