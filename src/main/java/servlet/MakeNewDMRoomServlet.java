package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Room;
import bean.User;
import exception.SwackException;
import model.RoomModel;
import model.UserModel;

@WebServlet("/MakeNewDMRoomServlet")
public class MakeNewDMRoomServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String uid = u.getUserId();

		Map<String, Room> map = null;
		try {
			map = new RoomModel().getJoined2UsersDMRooms(uid);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// TODO: まだDMを作っていない自分以外のユーザ一覧を表示するか、 DM作成済かの状態付きの自分以外のユーザ一覧をとる

		List<User> usersWithoutMe = null;
		try {
			usersWithoutMe = new UserModel().getUsersWithoutMe(uid);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		request.setAttribute("users", usersWithoutMe);
		request.setAttribute("roomMap", map);
		request.getRequestDispatcher("/WEB-INF/jsp/makenewdmroom.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String partnerUserId = request.getParameter("partnerUserId");
		if (partnerUserId == null) {
			response.sendRedirect("MainServlet");
			return;
		}

		User u = (User) request.getSession().getAttribute("user");
		String myUserId = u.getUserId();
		String roomid = null;
		RoomModel rm = new RoomModel();
		try {
			// 自分と相手のみが含まれるDM部屋に飛ばす
			roomid = rm.get2usersDMRoom(myUserId, partnerUserId);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if (roomid != null) {

			// すでに部屋が存在する場合は部屋を作らずにそこに飛ばす
			response.sendRedirect("MainServlet?roomId=" + roomid);
			return;
		}

		String newRoomId = null;
		// TODO: 自分と相手のみが含まれるDB部屋がなければ、新規で作成してそこに飛ばす
		try {

			// DM部屋作成、myUserIdのユーザが参加
			newRoomId = rm.createRoom(myUserId, "P" + myUserId + "," + partnerUserId, true, true);

			// 相手だけを参加させる
			rm.joinRoom(newRoomId, partnerUserId);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println(newRoomId);
		response.sendRedirect("MainServlet?roomId=" + newRoomId);
	}

}
