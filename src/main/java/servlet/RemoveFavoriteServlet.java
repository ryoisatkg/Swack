package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.ChatModel;
import model.FavoriteModel;


@WebServlet("/RemoveFavoriteServlet")
public class RemoveFavoriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String cid_str = request.getParameter("chatLogId");
		
		boolean fromFavorite = request.getParameter("favorite") != null;
		
		String redirectTo = fromFavorite ? "ViewFavoriteServlet" : "ViewRoomChatlogServlet";
		
		if (cid_str == null) {
			response.sendRedirect(redirectTo);
			return;
		}

		int cid;
		try {
			cid = Integer.parseInt(cid_str);
		} catch (NumberFormatException e) {
			System.out.println(e);
			response.sendRedirect(redirectTo);
			return;
		}

		String uid = u.getUserId();

		try {
			FavoriteModel fm = new FavoriteModel();
			if (new ChatModel().getChatlog(cid, uid) == null || !fm.isFavorited(uid, cid)) {
				response.sendRedirect(redirectTo);
				return;
			}

			fm.removeFavorite(uid, cid);
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		response.sendRedirect(redirectTo);
		return;
	}


}
