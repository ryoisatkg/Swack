
package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.ChatModel;

/**
 * Servlet implementation class ChatDeleteServlet
 */
@WebServlet("/ChatDeleteServlet")
public class ChatDeleteServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int cid;
		try {
			cid = Integer.parseInt(request.getParameter("chatLogId"));
		} catch (NumberFormatException e) {
			response.sendRedirect("ViewRoomChatlogServlet");
			return;
		}

		User u = (User) request.getSession().getAttribute("user");
		String userid = u.getUserId();
		if (userid.equals("U0000") || isPostedUserId(u.getUserId(), cid)) {
			request.setAttribute("chatLogId", cid);
			try {
				new ChatModel().deleteChatlog(cid);
			} catch (NumberFormatException e) {
				// TODO エラーを表示したほうがいい気がする
				response.sendRedirect("ViewRoomChatlogServlet");
				return;
			} catch (SwackException e) {
				response.sendRedirect("ViewRoomChatlogServlet");
				return;
			}
		}
		response.sendRedirect("ViewRoomChatlogServlet");
	}

	private boolean isPostedUserId(String userid, int chatlogid) {
		try {
			String chatuid = new ChatModel().getChatlog(chatlogid, userid).getUserId();

			return chatuid != null && userid.equals(chatuid);
		} catch (SwackException e) {

			e.printStackTrace();
		}
		return false;
	}
}
