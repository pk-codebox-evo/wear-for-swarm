package io.seal.swarmwear.service;

import com.mariux.teleport.lib.TeleportService;
import io.seal.swarmwear.EventManager;
import io.seal.swarmwear.lib.Properties;
import io.seal.swarmwear.networking.Foursquare;

public class WearListenerService extends TeleportService {

    private static final String TAG = "WearListenerService";

    @Override
    public void onCreate() {
        super.onCreate();

        setOnGetMessageTaskBuilder(new OnGetMessageTask.Builder() {
            @Override
            public OnGetMessageTask build() {
                return new VenuesOnGetMessageTask();
            }
        });
    }

    private class VenuesOnGetMessageTask extends TeleportService.OnGetMessageTask {
        @Override
        protected void onPostExecute(String path) {
            if (path.startsWith(Properties.Path.SEARCH_VENUES)) {

                if (Foursquare.isLoggedIn(WearListenerService.this)) {
                    SearchVenuesService.start(WearListenerService.this);
                } else {
                    sendMessage(Properties.Path.NO_LOGIN, null);
                }

            } else if (path.startsWith(Properties.Path.CHECK_IN)) {
                String id = path.replaceFirst(Properties.Path.CHECK_IN + '/', "");
                DoCheckinService.start(WearListenerService.this, id);
            } else {
                EventManager.trackAndLogEvent(TAG, "unknown path");
            }
        }
    }

}
