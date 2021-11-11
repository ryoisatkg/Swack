package model;

import java.util.List;

import bean.ChatLog;
import dao.FavoriteDAO;
import exception.SwackException;

public class FavoriteModel {
	public void addFavorite(String userid, int chatlogid) throws SwackException {
		new FavoriteDAO().addFavorite(userid, chatlogid);
	}

	public void removeFavorite(String userid, int chatlogid) throws SwackException {
		new FavoriteDAO().removeFavorite(userid, chatlogid);
	}

	public boolean isFavorited(String userid, int chatlogid) throws SwackException {
		return new FavoriteDAO().isFavorited(userid, chatlogid);
	}

	public List<ChatLog> getFavoriteChatlogs(String userid) throws SwackException {
		return new FavoriteDAO().getFavoriteChatlogs(userid);
	}
}
