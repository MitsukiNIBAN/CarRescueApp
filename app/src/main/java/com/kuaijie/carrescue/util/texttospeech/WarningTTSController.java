package com.kuaijie.carrescue.util.texttospeech;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.LinkedList;

/**
 * Created by Mitsuki on 12-28.
 */

public class WarningTTSController implements ICallBack {
    private static final String TAG = "WarningTTSController";

    private final int TTS_PLAY = 1;
    private final int CHECK_TTS_PLAY = 2;

    public static WarningTTSController warningTTSManager;
    private Context context;
    private IFlyTTS iFlyTTS = null;
    private TTS tts = null;
    private LinkedList<String> wordList = new LinkedList<String>();

    private WarningTTSController(Context context) {
        this.context = context.getApplicationContext();
        this.iFlyTTS = IFlyTTS.getInstance(this.context);
        this.tts = this.iFlyTTS;
    }

    public static WarningTTSController getInstance(Context context) {
        if (warningTTSManager == null)
            warningTTSManager = new WarningTTSController(context);
        return warningTTSManager;
    }

    public void init() {
        if (iFlyTTS != null)
            iFlyTTS.init();
        tts.setCallback(this);
    }

    public void stopSpeaking() {
        if (iFlyTTS != null)
            iFlyTTS.stopSpeak();
        wordList.clear();
    }

    public void destroy() {
        if (iFlyTTS != null)
            iFlyTTS.destroy();
        warningTTSManager = null;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TTS_PLAY:
                    if (tts != null && wordList.size() > 0) {
                        tts.playText(wordList.removeFirst());
                    }
                    break;
                case CHECK_TTS_PLAY:
                    if (!tts.isPlaying()) {
                        handler.obtainMessage(TTS_PLAY).sendToTarget();
                    }
                    break;
            }
        }
    };

    //////////////ICallBack////////////////////
    @Override
    public void onCompleted(int code) {
        if (handler != null) {
            handler.obtainMessage(TTS_PLAY).sendToTarget();
        }
    }
}
