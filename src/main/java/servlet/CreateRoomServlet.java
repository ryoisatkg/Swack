
package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.RoomModel;
import model.UserModel;
import parameter.Messages;

/**
 * ルーム作成画面を制御するServlet 本Servletの実行にはログインを必要とする
 */
@WebServlet("/CreateRoomServlet")
public class CreateRoomServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		String[] needJoinUserIds = request.getParameterValues("needsJoinUsers");

		String userid = user.getUserId();
		String roomname = request.getParameter("roomname");
		roomname = roomname == null ? "" : roomname;

		boolean isprivate = userid.equals("U0000") && request.getParameter("ispublic") == null;

		StringBuilder errorMsg = new StringBuilder();
		if (roomname == null || roomname.length() == 0) {
			errorMsg.append("※ルーム名が入力されていません<br>");
		}
		
		try {
			if (new RoomModel().nameExists(roomname)) {
				errorMsg.append("※すでに同じルーム名のルームが存在します<br>");
			}
		} catch (SwackException e1) {
			errorMsg.append(Messages.ERR_SYSTEM);
		}

		if (errorMsg.length() > 0) {
			// ルーム作成画面に戻してエラーメッセージを表示する
			request.setAttribute("errorMsg", errorMsg.toString());
			this.doGet(request, response);
			return;
		}

		String roomid = null;
		try {
			roomid = new RoomModel().createRoom(userid, roomname, isprivate);
			if(needJoinUserIds != null) {
				new RoomModel().joinMultipleUsersToRoom(roomid, needJoinUserIds);
			}
		} catch (SwackException e) {
			e.printStackTrace();
		}

		if (roomid != null) {
			// 自分が作ったルームへ飛ぶ
			response.sendRedirect("MainServlet?roomId=" + roomid);
			return;
		}
		response.sendRedirect("MainServlet");

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		List<User> user = null;
		try {
			user = new UserModel().getUsersWithoutMe(u.getUserId());
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		request.setAttribute("users", user);
		request.getRequestDispatcher("/WEB-INF/jsp/createroom.jsp").forward(request, response);
	}
}
