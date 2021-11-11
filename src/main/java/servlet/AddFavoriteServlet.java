package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.ChatModel;
import model.FavoriteModel;

/**
 * Servlet implementation class AddFavoriteServlet
 */
@WebServlet("/AddFavoriteServlet")
public class AddFavoriteServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String cid_str = request.getParameter("chatLogId");
		if (cid_str == null) {
			response.sendRedirect("ViewRoomChatlogServlet");
			return;
		}

		int cid;
		try {
			cid = Integer.parseInt(cid_str);
		} catch (NumberFormatException e) {
			System.out.println(e);
			response.sendRedirect("ViewRoomChatlogServlet");
			return;
		}

		String uid = u.getUserId();

		try {
			FavoriteModel fm = new FavoriteModel();
			if (new ChatModel().getChatlog(cid, uid) == null || fm.isFavorited(uid, cid)) {
				response.sendRedirect("ViewRoomChatlogServlet");
				return;
			}

			fm.addFavorite(uid, cid);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		response.sendRedirect("ViewRoomChatlogServlet");
		return;
	}

}
