package com.example.braintraining;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            activateAppMetrica();
        } catch (Exception e) {
            e.printStackTrace();
        }

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("affe3bd2-220b-4120-90aa-c3a262165648").withLogs().build();
        YandexMetrica.activate(this, config);
    }

    private void activateAppMetrica() throws Exception {
        YandexMetricaConfig appMetricaConfig = YandexMetricaConfig.newConfigBuilder("affe3bd2-220b-4120-90aa-c3a262165648")
                .handleFirstActivationAsUpdate(isFirstActivationAsUpdate())
                .withLocationTracking(true)
                .withLogs()
                .withStatisticsSending(true)
                .build();
        YandexMetrica.activate(getApplicationContext(), appMetricaConfig);
    }

    private boolean isFirstActivationAsUpdate() throws Exception {
        // Implement logic to detect whether the app is opening for the first time.
        // For example, you can check for files (settings, databases, and so on),
        // which the app creates on its first launch.
        throw new Exception("An operation is not implemented.");
    }
}
