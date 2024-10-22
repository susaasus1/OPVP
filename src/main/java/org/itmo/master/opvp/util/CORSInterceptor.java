package org.itmo.master.opvp.util;

import ru.tinkoff.kora.common.Context;
import ru.tinkoff.kora.http.common.body.HttpBody;
import ru.tinkoff.kora.http.common.header.HttpHeaders;
import ru.tinkoff.kora.http.server.common.HttpServerInterceptor;
import ru.tinkoff.kora.http.server.common.HttpServerRequest;
import ru.tinkoff.kora.http.server.common.HttpServerResponse;
import ru.tinkoff.kora.http.server.common.HttpServerResponseException;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeoutException;

public final class CORSInterceptor implements HttpServerInterceptor {

    @Override
    public CompletionStage<HttpServerResponse> intercept(Context context, HttpServerRequest request, InterceptChain chain) throws Exception {
        return chain.process(context, request)
                .thenApply(reponse -> HttpServerResponse.of(reponse.code(),
                        HttpHeaders.of("Access-Control-Allow-Origin", "*",
                                "Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PATCH, PUT",
                                "Access-Control-Allow-Headers", "Content-Type"),
                        reponse.body()))
                .exceptionally(e -> {
                    if (e instanceof HttpServerResponseException ex) {
                        return ex;
                    }

                    var body = HttpBody.plaintext(e.getMessage());
                    if (e instanceof IllegalArgumentException) {
                        return HttpServerResponse.of(400, body);
                    } else if (e instanceof TimeoutException) {
                        return HttpServerResponse.of(408, body);
                    } else {
                        return HttpServerResponse.of(500, body);
                    }
                });
    }
}