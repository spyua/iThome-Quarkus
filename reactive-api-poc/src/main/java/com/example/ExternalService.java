package com.example;


import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ExternalService {

    // 響應式外部 API 調用
    public Uni<String> callExternalApi() {
        return Uni.createFrom().item(() -> {
            // 模擬 I/O 操作 (比如調用外部 HTTP 服務)
            try {
                TimeUnit.MILLISECONDS.sleep(100);  // 模擬外部延遲
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "External API response";
        });
    }

    // 同步外部 API 調用
    public String callExternalApiSync() {
        // 模擬 I/O 操作 (比如調用外部 HTTP 服務)
        try {
            TimeUnit.MILLISECONDS.sleep(100);  // 模擬外部延遲
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "External API response";
    }

}
