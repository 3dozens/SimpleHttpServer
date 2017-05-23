package http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {

	public void start() {
		try (ServerSocket server = new ServerSocket(8080)) {
			while (true) {
				this.serverProcess(server);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void serverProcess(ServerSocket server) throws Exception {
		try (
			Socket socket = server.accept();
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			) {
			
			HttpRequest request = new HttpRequest(in);
			
			HttpHeader header = request.getHeader();
			
			if (header.isGetMethod()) {
				File file = new File("." + header.getPath());
				
				if (file.exists() && file.isFile()) {
					this.respondLocalFile(file, out);
				} else {
					this.respondNotFoundError(out);
				}
			} else {
				this.respondOk(out);
			}
		}
	}
	
	private void respondLocalFile(File file, OutputStream out) throws IOException {
		HttpResponse response = new HttpResponse(Status.OK);
		response.setBody(file);
		response.writeTo(out);
	}
	
	private void respondNotFoundError(OutputStream out) throws IOException {
		HttpResponse response = new HttpResponse(Status.NOT_FOUND);
		response.addHeader("Content-Type", ContentType.TEXT_PLAIN);
		response.writeTo(out);
	}
	
	private void respondOk(OutputStream out) throws IOException {
		HttpResponse response = new HttpResponse(Status.OK);
		response.writeTo(out);
	}
}
