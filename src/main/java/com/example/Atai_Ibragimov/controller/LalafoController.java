package com.example.Atai_Ibragimov.controller;

import com.example.Atai_Ibragimov.service.LalafoService;
import com.example.Atai_Ibragimov.model.Advert;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LalafoController implements HttpHandler {
    private final LalafoService lalafoService;

    public LalafoController() {
        this.lalafoService = new LalafoService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleGetRequest(exchange);
            } else {
                sendErrorResponse(exchange, 405, "Метод не поддерживается");
            }
        } catch (Exception e) {
            System.err.println("Критическая ошибка в контроллере: " + e.getMessage());
            sendErrorResponse(exchange, 500, "Внутренняя ошибка сервера");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Обработка запроса на /test-result");
            long startTime = System.currentTimeMillis();

            List<Advert> adverts = lalafoService.getAdverts();
            String htmlResponse = generateHtmlPage(adverts);

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, htmlResponse.getBytes(StandardCharsets.UTF_8).length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(htmlResponse.getBytes(StandardCharsets.UTF_8));
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Успешно отправлено " + adverts.size() + " объявлений за " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            System.err.println("Ошибка обработки GET запроса: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Ошибка генерации страницы: " + e.getMessage());
        }
    }

    private String generateHtmlPage(List<Advert> adverts) {
        StringBuilder html = new StringBuilder();
        String currentTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());

        // HTML Header with Meta tags and CSS
        html.append("""
            <!DOCTYPE html>
            <html lang="ru">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="description" content="Актуальные объявления с Lalafo.kg">
                <meta name="author" content="Atai Ibragimov">
                <title>Lalafo.kg - Актуальные объявления</title>
                <style>
                    /* Reset and Base Styles */
                    * { 
                        margin: 0; 
                        padding: 0; 
                        box-sizing: border-box; 
                    }
                    
                    body { 
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        padding: 20px;
                        line-height: 1.6;
                    }
                    
                    .container { 
                        max-width: 1400px; 
                        margin: 0 auto; 
                    }
                    
                    /* Header Styles */
                    .header { 
                        text-align: center; 
                        margin-bottom: 40px; 
                        padding: 40px 30px; 
                        background: rgba(255, 255, 255, 0.95); 
                        border-radius: 20px; 
                        box-shadow: 0 15px 35px rgba(0,0,0,0.1);
                        backdrop-filter: blur(10px);
                        border: 1px solid rgba(255, 255, 255, 0.3);
                    }
                    
                    .header h1 { 
                        color: #2c3e50; 
                        margin-bottom: 15px;
                        font-size: 2.8em;
                        font-weight: 700;
                        background: linear-gradient(45deg, #667eea, #764ba2);
                        -webkit-background-clip: text;
                        -webkit-text-fill-color: transparent;
                        text-shadow: 0 2px 10px rgba(102, 126, 234, 0.1);
                    }
                    
                    .header .subtitle {
                        color: #7f8c8d;
                        font-size: 1.2em;
                        margin-bottom: 10px;
                    }
                    
                    /* Stats and Info */
                    .stats { 
                        color: #34495e; 
                        font-size: 1.1em;
                        font-weight: 600;
                        margin: 15px 0;
                    }
                    
                    .last-update {
                        color: #95a5a6;
                        font-size: 0.9em;
                        margin-top: 10px;
                    }
                    
                    /* Adverts Grid */
                    .adverts-grid { 
                        display: grid; 
                        grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); 
                        gap: 30px; 
                        margin-bottom: 50px; 
                    }
                    
                    /* Advert Card */
                    .advert-card { 
                        background: white; 
                        border-radius: 20px; 
                        overflow: hidden; 
                        box-shadow: 0 8px 25px rgba(0,0,0,0.1); 
                        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275); 
                        cursor: pointer;
                        border: 1px solid rgba(255, 255, 255, 0.3);
                        position: relative;
                    }
                    
                    .advert-card:hover { 
                        transform: translateY(-12px) scale(1.02); 
                        box-shadow: 0 20px 50px rgba(0,0,0,0.2); 
                    }
                    
                    /* Image Container */
                    .image-container {
                        position: relative;
                        overflow: hidden;
                        height: 240px;
                    }
                    
                    .advert-image { 
                        width: 100%; 
                        height: 100%; 
                        object-fit: cover;
                        transition: transform 0.4s ease;
                    }
                    
                    .advert-card:hover .advert-image {
                        transform: scale(1.1);
                    }
                    
                    .no-image { 
                        width: 100%; 
                        height: 100%; 
                        background: linear-gradient(45deg, #f8f9fa, #e9ecef);
                        display: flex; 
                        align-items: center; 
                        justify-content: center; 
                        color: #6c757d;
                        font-size: 16px;
                        flex-direction: column;
                    }
                    
                    .no-image::before {
                        content: "";
                        font-size: 3em;
                        margin-bottom: 10px;
                    }
                    
                    /* Badge */
                    .advert-badge {
                        position: absolute;
                        top: 15px;
                        right: 15px;
                        background: linear-gradient(45deg, #ff6b6b, #ee5a52);
                        color: white;
                        padding: 6px 12px;
                        border-radius: 15px;
                        font-size: 11px;
                        font-weight: bold;
                        text-transform: uppercase;
                        letter-spacing: 0.5px;
                        box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
                    }
                    
                    /* Advert Info */
                    .advert-info { 
                        padding: 25px; 
                    }
                    
                    .advert-title { 
                        font-size: 17px; 
                        font-weight: 600; 
                        color: #2c3e50; 
                        margin-bottom: 15px; 
                        line-height: 1.4; 
                        display: -webkit-box; 
                        -webkit-line-clamp: 2; 
                        -webkit-box-orient: vertical; 
                        overflow: hidden;
                        height: 48px;
                    }
                    
                    .advert-price { 
                        font-size: 22px; 
                        font-weight: bold; 
                        color: #e74c3c; 
                        margin-bottom: 15px;
                        text-shadow: 0 2px 4px rgba(231, 76, 60, 0.1);
                    }
                    
                    .advert-meta { 
                        display: flex; 
                        justify-content: space-between; 
                        align-items: center;
                        font-size: 14px; 
                        color: #7f8c8d; 
                    }
                    
                    .advert-city { 
                        background: linear-gradient(45deg, #3498db, #2980b9);
                        color: white;
                        padding: 6px 14px; 
                        border-radius: 20px; 
                        font-weight: 600;
                        font-size: 13px;
                    }
                    
                    .advert-date {
                        color: #95a5a6;
                        font-weight: 500;
                        font-size: 13px;
                    }
                    
                    /* Footer */
                    .footer { 
                        text-align: center; 
                        padding: 40px; 
                        color: white;
                        background: rgba(255, 255, 255, 0.1);
                        border-radius: 20px;
                        backdrop-filter: blur(10px);
                        border: 1px solid rgba(255, 255, 255, 0.2);
                        margin-top: 30px;
                    }
                    
                    .footer h3 {
                        margin-bottom: 15px;
                        font-size: 1.4em;
                        font-weight: 600;
                    }
                    
                    .footer p {
                        margin-bottom: 8px;
                        opacity: 0.9;
                    }
                    
                    /* Empty State */
                    .empty-state { 
                        text-align: center; 
                        padding: 100px 40px; 
                        background: rgba(255, 255, 255, 0.95);
                        border-radius: 20px;
                        margin-bottom: 40px;
                        backdrop-filter: blur(10px);
                    }
                    
                    .empty-state h2 { 
                        color: #34495e; 
                        margin-bottom: 25px;
                        font-size: 2.2em;
                        font-weight: 600;
                    }
                    
                    .empty-state p {
                        color: #7f8c8d;
                        margin-bottom: 15px;
                        font-size: 1.1em;
                        line-height: 1.6;
                    }
                    
                    .refresh-btn {
                        background: linear-gradient(45deg, #667eea, #764ba2);
                        color: white;
                        border: none;
                        padding: 15px 40px;
                        border-radius: 30px;
                        font-size: 16px;
                        font-weight: 600;
                        cursor: pointer;
                        margin-top: 25px;
                        transition: all 0.3s ease;
                        box-shadow: 0 5px 20px rgba(102, 126, 234, 0.3);
                    }
                    
                    .refresh-btn:hover {
                        transform: translateY(-3px);
                        box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
                    }
                    
                    /* Loading Animation */
                    .loading {
                        text-align: center;
                        padding: 60px;
                        color: white;
                        font-size: 18px;
                    }
                    
                    .loading-spinner {
                        border: 4px solid rgba(255, 255, 255, 0.3);
                        border-radius: 50%;
                        border-top: 4px solid white;
                        width: 40px;
                        height: 40px;
                        animation: spin 1s linear infinite;
                        margin: 0 auto 20px;
                    }
                    
                    @keyframes spin {
                        0% { transform: rotate(0deg); }
                        100% { transform: rotate(360deg); }
                    }
                    
                    /* Responsive Design */
                    @media (max-width: 1200px) { 
                        .adverts-grid { 
                            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); 
                        }
                    }
                    
                    @media (max-width: 768px) { 
                        .adverts-grid { 
                            grid-template-columns: 1fr; 
                            gap: 25px;
                        }
                        
                        .header h1 {
                            font-size: 2.2em;
                        }
                        
                        body {
                            padding: 15px;
                        }
                        
                        .header {
                            padding: 30px 20px;
                            margin-bottom: 30px;
                        }
                    }
                    
                    @media (max-width: 480px) { 
                        .header h1 {
                            font-size: 1.8em;
                        }
                        
                        .advert-info {
                            padding: 20px;
                        }
                        
                        .advert-price {
                            font-size: 20px;
                        }
                    }
                    
                    /* Utility Classes */
                    .text-center { text-align: center; }
                    .mb-20 { margin-bottom: 20px; }
                    .mt-20 { margin-top: 20px; }
                    .opacity-80 { opacity: 0.8; }
                </style>
            </head>
            <body>
                
                    """);

        if (adverts.isEmpty()) {
            html.append("Не удалось загрузить объявления в реальном времени");
        } else {
            html.append("Найдено объявлений: <strong>").append(adverts.size()).append("</strong>");
        }

        html.append("</div>");

        html.append("""
                    </div>
            """);

        if (adverts.isEmpty()) {
            html.append("""
                    <div class="empty-state">
                        <h2>Не удалось загрузить объявления</h2>
                    </div>
            """);
        } else {
            html.append("<div class=\"adverts-grid\">");

            // Generate advert cards
            for (int i = 0; i < adverts.size(); i++) {
                Advert advert = adverts.get(i);
                html.append(generateAdvertCard(advert, i));
            }

            html.append("</div>");
        }



        return html.toString();
    }

    private String generateAdvertCard(Advert advert, int i) {
        StringBuilder card = new StringBuilder();

        card.append("<div class=\"advert-card\" data-url=\"");
        card.append(escapeHtml(advert.getUrl()));
        card.append("\">");

        // Image section - handle missing images properly
        card.append("<div class=\"image-container\">");

        if (advert.getImageUrl() != null &&
                !advert.getImageUrl().isEmpty() &&
                !advert.getImageUrl().contains("placeholder")) {
            // Show actual image with error handling
            card.append("<img src=\"");
            card.append(escapeHtml(advert.getImageUrl()));
            card.append("\" alt=\"\" class=\"advert-image\" ");
            card.append("onerror=\"this.style.display='none'; this.nextElementSibling.style.display='flex';\">");
            card.append("<div class=\"no-image\" style=\"display:none;\">No photo</div>");
        } else {
            // Show placeholder immediately
            card.append("<div class=\"no-image\">No photo</div>");
        }

        card.append("</div>");

        // Info section
        card.append("<div class=\"advert-info\">");
        card.append("<div class=\"advert-title\">");
        card.append(escapeHtml(advert.getTitle()));
        card.append("</div>");
        card.append("<div class=\"advert-price\">");
        card.append(escapeHtml(advert.getPrice()));
        card.append("</div>");
        card.append("<div class=\"advert-meta\">");
        card.append("<span class=\"advert-city\">");
        card.append(escapeHtml(advert.getCity()));
        card.append("</span>");
        card.append("<span class=\"advert-date\">");
        card.append(escapeHtml(advert.getDate()));
        card.append("</span>");
        card.append("</div>");
        card.append("</div>");
        card.append("</div>");

        return card.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("%", "&#37;")
                .replace("$", "&#36;")
                .replace("{", "&#123;")
                .replace("}", "&#125;")
                .replace("[", "&#91;")
                .replace("]", "&#93;")
                .replace("`", "&#96;");
    }

    private void sendErrorResponse(HttpExchange exchange, int code, String message) throws IOException {
        try {
            StringBuilder errorResponse = new StringBuilder();
            errorResponse.append("""
                <html>
                <head>
                    <title>Ошибка """).append(code).append("""
                    </title>
                    <style>
//                        body { 
//                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
//                            padding: 40px; 
//                            text-align: center; 
//                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
//                            color: white;
//                            min-height: 100vh;
//                            display: flex;
//                            align-items: center;
//                            justify-content: center;
//                        }
                        .error-container {
                            background: rgba(255, 255, 255, 0.1);
                            padding: 50px;
                            border-radius: 20px;
                            backdrop-filter: blur(10px);
                            border: 1px solid rgba(255, 255, 255, 0.2);
                            max-width: 500px;
                            width: 100%;
                        }
                        .error-icon {
                            font-size: 4em;
                            margin-bottom: 20px;
                        }
                        .error { 
                            color: #ff6b6b; 
                            margin: 25px 0;
                            font-size: 1.3em;
                            font-weight: 500;
                        }
                        .retry-btn {
                            background: white;
                            color: #667eea;
                            border: none;
                            padding: 15px 35px;
                            border-radius: 30px;
                            font-size: 16px;
                            font-weight: 600;
                            cursor: pointer;
                            margin-top: 25px;
                            transition: all 0.3s ease;
                            text-decoration: none;
                            display: inline-block;
                        }
                        .retry-btn:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 8px 25px rgba(255, 255, 255, 0.3);
                        }
                    </style>
                </head>
                <body>
                    <div class="error-container">
                  
                        <h1>Ошибка """).append(code).append("""
                        </h1>
                        <div class="error">""")
                    .append(escapeHtml(message))
                    .append("""
                        </div>
                   
                    </div>
                </body>
                </html>
                """);

            String response = errorResponse.toString();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(code, response.getBytes(StandardCharsets.UTF_8).length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            System.err.println("error response: " + e.getMessage());
        }
    }
}