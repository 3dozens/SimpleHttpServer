package http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String args[]) throws Exception {
		System.out.println("start >>");
		
		try (
			ServerSocket server = new ServerSocket(8080);
			Socket socket = server.accept(); // このメソッドはブロッキング処理なので、接続があるまでプログラムはこの場所で停止する。
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			) {
				
			HttpRequest request = new HttpRequest(in);
			
			HttpHeader header = request.getHeader();
			
			if (header.isGetMethod()) {
				File file = new File(".", header.getPath());
				if (file.exists() && file.isFile()) {
					respondLocalFile(file, out);
				} else {
					respondNotFoundError(out);
				}
			} else {
				responseOk(out);
			}
			
			System.out.println(request.getHeaderText());
			System.out.println(request.getBodyText());
		}
		
		System.out.println("<<< end");
	}
	
	private static void respondLocalFile(File file, OutputStream out) throws IOException {
		HttpResponse response = new HttpResponse(Status.OK);
		response.setBody(file);
		response.writeTo(out);
	}
	
	private static void respondNotFoundError(OutputStream out) throws IOException {
		HttpResponse response = new HttpResponse(Status.NOT_FOUND);
		response.addHeader("Content-Type", ContentType.TEXT_PLAIN);
		response.setBody("Not Found");
		response.writeTo(out);
	}
	
	private static void responseOk(OutputStream out) throws IOException {
		HttpResponse response = new HttpResponse(Status.OK);
		response.writeTo(out);
	}
}
