package com.kuaijie.carrescue.util.texttospeech;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.LinkedList;
import java.util.logging.LogRecord;

/**
 * Created by Mitsuki on 12-28.
 * 每个页面任务语音播报提示类
 */

public class AppTTSController implements ICallBack, TaskHint{
    private static final String TAG = "AppTTSController";

    private final int TTS_PLAY = 1;
    private final int CHECK_TTS_PLAY = 2;

    public static AppTTSController appTTSManager;
    private Context context;
    private IFlyTTS iFlyTTS = null;
    private TTS tts = null;
    private LinkedList<String> wordList = new LinkedList<String>();

    private AppTTSController(Context context){
        this.context = context.getApplicationContext();
        this.iFlyTTS = IFlyTTS.getInstance(this.context);
        this.tts = this.iFlyTTS;
    }

    public static AppTTSController getInstance(Context context){
        if (appTTSManager == null)
            appTTSManager = new AppTTSController(context);
        return appTTSManager;
    }

    public void init(){
        if (iFlyTTS != null)
            iFlyTTS.init();
        tts.setCallback(this);
    }

    public void stopSpeaking(){
        if (iFlyTTS != null)
            iFlyTTS.stopSpeak();
        wordList.clear();
    }

    public void destroy(){
        if (iFlyTTS != null)
            iFlyTTS.destroy();
        appTTSManager = null;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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

    /////////////////ICallBack///////////////
    @Override
    public void onCompleted(int code) {
        if (handler != null) {
            handler.obtainMessage(TTS_PLAY).sendToTarget();
        }
    }
    ////////////////TaskHint/////////////////////
    @Override
    public void addHint(String str) {
        if (wordList != null)
            wordList.addLast(str);
        handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget();
    }
}
