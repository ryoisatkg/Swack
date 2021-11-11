package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import dao.RoomsDAO;
import exception.SwackException;

/**
 * Servlet implementation class JoinRoomServlet
 */
@WebServlet("/JoinRoomServlet")
public class JoinRoomServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String uid = u.getUserId();
		String rid = request.getParameter("roomid");
		
		if(rid == null) {
			response.sendRedirect("MainServlet");
			return;
		}
		
		try {
			var rd = new RoomsDAO();
			if(!rd.isJoined(rid, uid)) {
				new RoomsDAO().joinRoom(rid, uid);
			}
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		response.sendRedirect("MainServlet?roomId=" + rid);
	}

}
