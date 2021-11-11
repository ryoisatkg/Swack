package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bean.User;

@WebServlet("/UploadServlet")
public class UploadServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	//upload.jsp遷移用
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/upload.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ログイン情報からユーザID取得
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		//ファイル名は「ユーザID.png」とする
		String fileName = user.getUserId() + ".png";
		//アップロード処理
		upload(request, fileName);
		response.sendRedirect("MainServlet");
	}

	private void upload(HttpServletRequest request, String fileName)
			throws IOException, FileNotFoundException {
		// アップロードファイルを受け取る準備
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// tempディレクトリをアイテムファクトリーの一次領域に設定
		ServletContext servletContext = this.getServletConfig().getServletContext();
		factory.setRepository((File) servletContext.getAttribute("javax.servlet.context.tempdir"));
		// ServletFileUploadを作成
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// リクエストをファイルアイテムのリストに変換
			List<FileItem> items = upload.parseRequest(request);
			// アップロードパス取得
			System.out.println(Paths.get("./").toAbsolutePath().normalize());
			String upPath = servletContext.getRealPath("/") + "images/";
			byte[] buff = new byte[1024];
			int size = 0;
			for (FileItem item : items) {
				// アップロードファイルの処理
				if (!item.isFormField()) {
					// ファイルをuploadディレクトリに保存
					BufferedInputStream in = new BufferedInputStream(item.getInputStream());
					File f = new File(upPath + fileName);
					System.out.println(f.getAbsolutePath());
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
					while ((size = in.read(buff)) > 0) {
						out.write(buff, 0, size);
					}
					out.close();
					in.close();
					// アップロードしたファイル情報を表示
					System.out.println("upload:" + servletContext.getContextPath() + "/images/" + fileName);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}