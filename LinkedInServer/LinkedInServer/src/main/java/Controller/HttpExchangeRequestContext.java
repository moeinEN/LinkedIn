package Controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpExchangeRequestContext implements org.apache.commons.fileupload.RequestContext {
    private final HttpExchange exchange;

    public HttpExchangeRequestContext(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getCharacterEncoding() {
        return exchange.getRequestHeaders().getFirst("Content-Type");
    }

    @Override
    public String getContentType() {
        return exchange.getRequestHeaders().getFirst("Content-Type");
    }

    @Override
    public int getContentLength() {
        return Integer.parseInt(exchange.getRequestHeaders().getFirst("Content-Length"));
    }

    @Override
    public java.io.InputStream getInputStream() throws IOException {
        return exchange.getRequestBody();
    }
}
