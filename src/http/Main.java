package http;

import static http.Constant.*;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String args[]) throws Exception {
		System.out.println("start >>");
		
		try (
			ServerSocket server = new ServerSocket(8080);
			Socket socket = server.accept(); // このメソッドはブロッキング処理なので、接続があるまでプログラムはこの場所で停止する。
			InputStream in = socket.getInputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			) {
				
			HttpRequest request = new HttpRequest(in);
			
			System.out.println(request.getHeaderText());
			System.out.println(request.getBodyText());
			
			bw.write("HTTP/1.1 200 OK" + CRLF);
		}
		System.out.println("<<< end");
	}
}
