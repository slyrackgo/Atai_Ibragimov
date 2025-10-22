package com.example.Atai_Ibragimov;

import com.example.Atai_Ibragimov.config.ServerConfig;
import com.example.Atai_Ibragimov.controller.LalafoController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AtaiIbragimovApplication {
	public static void main(String[] args) {
		try {
			int port = ServerConfig.getServerPort();
			String host = ServerConfig.getServerHost();

			// Создаем HTTP сервер
			HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);

			// Создаем контекст для обработки запросов
			server.createContext("/test-result", new LalafoController());
			server.setExecutor(null);

			System.out.println("==========================================");
			System.out.println(ServerConfig.getAppName());
			System.out.println("==========================================");
			System.out.println("Сервер запущен на: http://" + host + ":" + port + "/test-result");
			System.out.println("Порт загружен из: application.properties");
			System.out.println("Максимум объявлений: " + ServerConfig.getMaxAdverts());
			System.out.println("==========================================");

			server.start();

			// Добавляем shutdown hook для graceful shutdown
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				System.out.println("Остановка сервера...");
				server.stop(0);
				System.out.println("Сервер остановлен.");
			}));

		} catch (IOException e) {
			System.err.println("Ошибка запуска сервера: " + e.getMessage());
			System.exit(1);
		}
	}
}