package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String args[]) throws Exception {
		System.out.println("start >>");
		
		try (
				ServerSocket server = new ServerSocket(8080);
				Socket socket = server.accept(); // このメソッドはブロッキング処理なので、接続があるまでプログラムはこの場所で停止する。
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				) {
			in.lines().forEach(System.out::println);
		}
	}
}
