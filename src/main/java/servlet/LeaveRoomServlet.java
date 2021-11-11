package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.RoomModel;
import model.UserModel;

/**
 * Servlet implementation class LeaveRoomServlet
 */
@WebServlet("/LeaveRoomServlet")
public class LeaveRoomServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String uid = u.getUserId();
		String rid = request.getParameter("roomid");

		if (rid == null) {
			response.sendRedirect("MainServlet");
			return;
		}

		try {
			var rm = new RoomModel();
			
			UserModel um = new UserModel();
			if (new UserModel().getLastAccessRoomId(uid).equals(rid)) {
				// 前回アクセスしたルームから抜けたときはそのルームに遷移したくないのでNULLを入れる
				um.updateLastAccessRoomId(uid, null);
			}
			rm.leaveRoom(rid, uid);

		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		response.sendRedirect("MainServlet");
	}

}
