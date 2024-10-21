//package org.itmo.master.opvp.operation;
//
//import ru.tinkoff.kora.http.common.annotation.HttpRoute;
//import ru.tinkoff.kora.http.common.annotation.Path;
//import ru.tinkoff.kora.http.common.annotation.Query;
//import ru.tinkoff.kora.http.server.common.annotation.HttpController;
//import ru.tinkoff.kora.json.common.annotation.Json;
//
//import java.util.List;
//
//import static ru.tinkoff.kora.http.common.HttpMethod.DELETE;
//import static ru.tinkoff.kora.http.common.HttpMethod.GET;
//import static ru.tinkoff.kora.http.common.HttpMethod.PATCH;
//import static ru.tinkoff.kora.http.common.HttpMethod.POST;
//import static ru.tinkoff.kora.http.common.HttpMethod.PUT;
//
//@HttpController("/api/v1")
//public final class OperationController {
//
//    private final IOperationService managementDataService;
//
//    public OperationController(IOperationService managementDataService) {
//        this.managementDataService = managementDataService;
//    }
//
//    @HttpRoute(method = GET, path = "")
//    @Json
//    public List<String> getAllValues() {
//        return managementDataService.getAll();
//    }
//
//    @HttpRoute(method = GET, path = "/{key}")
//    @Json
//    public String getValueByKey(
//            @Path("key") String key,
//            @Query("required") Boolean required) {
//
//        return required
//                ? managementDataService.get(key)
//                : managementDataService.getIfPresent(key);
//    }
//
//    @HttpRoute(method = POST, path = "/{key}")
//    public void addValueByKey(@Path("key") String key, String value) {
//        managementDataService.add(key, value);
//    }
//
//    @HttpRoute(method = PUT, path = "/{key}")
//    public void putValueByKey(@Path("key") String key, String value) {
//        managementDataService.put(key, value);
//    }
//
//    @HttpRoute(method = PATCH, path = "/{key}")
//    public void updateValueByKey(@Path("key") String key, String value) {
//        managementDataService.update(key, value);
//    }
//
//    @HttpRoute(method = DELETE, path = "/{key}")
//    public void deleteValueByKey(@Path("key") String key) {
//        managementDataService.remove(key);
//    }
//
//    @HttpRoute(method = DELETE, path = "")
//    public void clearAll() {
//        managementDataService.clear();
//    }
//
//}
