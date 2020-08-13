/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kral.test;

import io.helidon.common.configurable.Resource;
import io.helidon.common.pki.KeyConfig;
import io.helidon.config.Config;
import io.helidon.webclient.WebClient;
import io.helidon.webclient.WebClientTls;

/**
 * This is an example of how to set mutual TLS in WebClient via builder.
 */
public class MutualTlsViaBuilder {

    public static void main(String[] args) {
        KeyConfig keyConfig = KeyConfig.keystoreBuilder()
                .keystore(Resource.create("badssl.com-client.p12"))
                .keystorePassphrase("badssl.com")
                .build();

        WebClient webClient = WebClient.builder()
                .tls(WebClientTls.builder()
                             .clientKeyStore(keyConfig)
                             .build())
                .build();

        webClient.get()
                .uri("https://client.badssl.com/")
                .request()
                .thenCompose(response -> {
                    System.out.println(response.status());
                    return response.content().as(String.class);
                })
                .thenAccept(System.out::println)
                .await();
    }

}
