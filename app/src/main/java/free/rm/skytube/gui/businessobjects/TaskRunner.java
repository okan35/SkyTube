package free.rm.skytube.gui.businessobjects;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import free.rm.skytube.app.SkyTubeApp;
import free.rm.skytube.businessobjects.db.SubscriptionsDb;
import free.rm.skytube.gui.businessobjects.views.SubscribeButton;

public class TaskRunner {
    private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
    private Handler handler = new Handler();
    private String TAG = TaskRunner.class.getSimpleName();

    public <R> void executeAsync(SubscribeButton subscribeButton, String channelId) {
        System.out.println("okan okan");
        executor.execute(() -> {
            try {
                Boolean isUserSubbed;

                try {
                    isUserSubbed = SubscriptionsDb.getSubscriptionsDb().isUserSubscribedToChannel(channelId);
                } catch (Throwable tr) {
                    Log.e(TAG, "Unable to check if user has subscribed to channel id=" + channelId, tr);
                    isUserSubbed = null;
                }
                final Boolean userSubbedResult = isUserSubbed;

                handler.post(() -> {
                    if (userSubbedResult == null) {
                        String err = String.format(SkyTubeApp.getStr(free.rm.skytube.R.string.error_check_if_user_has_subbed), channelId);
                        Toast.makeText(subscribeButton.getContext(), err, Toast.LENGTH_LONG).show();
                    } else if (userSubbedResult) {
                        subscribeButton.setUnsubscribeState();
                    } else {
                        subscribeButton.setSubscribeState();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
