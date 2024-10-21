package org.itmo.master.opvp;

import ru.tinkoff.kora.application.graph.KoraApplication;
import ru.tinkoff.kora.common.KoraApp;
import ru.tinkoff.kora.config.hocon.HoconConfigModule;
import ru.tinkoff.kora.http.server.undertow.UndertowHttpServerModule;
import ru.tinkoff.kora.json.module.JsonModule;
import ru.tinkoff.kora.openapi.management.OpenApiManagementModule;


@KoraApp
public interface Application extends
        HoconConfigModule,
        UndertowHttpServerModule,
        OpenApiManagementModule,
        JsonModule {

    static void main(String[] args) {
        KoraApplication.run(ApplicationGraph::graph);
    }

}