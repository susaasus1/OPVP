package org.itmo.master.opvp.operation;

import ru.tinkoff.kora.http.common.annotation.HttpRoute;
import ru.tinkoff.kora.http.common.annotation.Path;
import ru.tinkoff.kora.http.common.annotation.Query;
import ru.tinkoff.kora.http.common.body.HttpBody;
import ru.tinkoff.kora.http.common.header.HttpHeaders;
import ru.tinkoff.kora.http.server.common.HttpServerResponse;
import ru.tinkoff.kora.http.server.common.annotation.HttpController;
import ru.tinkoff.kora.json.common.annotation.Json;

import java.util.List;

import static ru.tinkoff.kora.http.common.HttpMethod.DELETE;
import static ru.tinkoff.kora.http.common.HttpMethod.GET;
import static ru.tinkoff.kora.http.common.HttpMethod.PATCH;
import static ru.tinkoff.kora.http.common.HttpMethod.POST;
import static ru.tinkoff.kora.http.common.HttpMethod.PUT;

@HttpController("/api/v1/operations")
public final class OperationController {

    private final IOperationService operationService;

    public OperationController(IOperationService operationService) {
        this.operationService = operationService;
    }

    @HttpRoute(method = POST, path = "/table/{table}")
    public void createTable(@Path("table") String table) {
        operationService.createTable(table);
    }

    @HttpRoute(method = DELETE, path = "/table/{table}")
    public void dropTable(@Path("table") String table) {
        operationService.dropTable(table);
    }

    @HttpRoute(method = GET, path = "/table/{table}")
    public HttpServerResponse getAllValues(@Path("table") String table) {
        return HttpServerResponse.of(200,
                HttpHeaders.of("Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Methods", "GET, POST, OPTIONS",
                "Access-Control-Allow-Headers", "Content-Type"),
                HttpBody.json(operationService.getAll(table).toString()));
    }

    @HttpRoute(method = GET, path = "/table/{table}/key/{key}")
    @Json
    public String getValueByKey(
            @Path("table") String table,
            @Path("key") String key,
            @Query("required") Boolean required) {

        return required
                ? operationService.get(table, key)
                : operationService.getIfPresent(table, key);
    }

    @HttpRoute(method = POST, path = "/table/{table}/key/{key}")
    public void addValueByKey(@Path("table") String table,
                              @Path("key") String key,
                              @Json String value) {

        operationService.add(table, key, value);
    }

    @HttpRoute(method = PUT, path = "/table/{table}/key/{key}")
    public void putValueByKey(@Path("table") String table,
                              @Path("key") String key,
                              @Json String value) {

        operationService.put(table, key, value);
    }

    @HttpRoute(method = PATCH, path = "/table/{table}/key/{key}")
    public void updateValueByKey(@Path("table") String table,
                                 @Path("key") String key,
                                 String value) {

        operationService.update(table, key, value);
    }

    @HttpRoute(method = DELETE, path = "/table/{table}/key/{key}")
    public void deleteValueByKey(@Path("table") String table,
                                 @Path("key") String key) {

        operationService.remove(table, key);
    }

    @HttpRoute(method = DELETE, path = "/table/{table}/clear")
    public void clearAll(@Path("table") String table) {
        operationService.clear(table);
    }

}
