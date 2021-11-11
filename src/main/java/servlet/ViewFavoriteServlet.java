package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ChatLog;
import bean.User;
import exception.SwackException;
import model.FavoriteModel;


@WebServlet("/ViewFavoriteServlet")
public class ViewFavoriteServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String userid = u.getUserId();
		// 指定されたルームのチャットを取得する
		
		FavoriteModel fm = new FavoriteModel();
		List<ChatLog> chatlogs = null;
		try {
			chatlogs = fm.getFavoriteChatlogs(userid);
			
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		request.setAttribute("chatloglist", chatlogs);
		request.setAttribute("favolist", true);
		request.getRequestDispatcher("/WEB-INF/jsp/viewchatlogs.jsp").forward(request, response);
	}

}
