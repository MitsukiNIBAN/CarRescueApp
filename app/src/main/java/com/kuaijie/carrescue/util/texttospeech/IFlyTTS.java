package com.kuaijie.carrescue.util.texttospeech;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by Mitsuki on 12-28.
 */

public class IFlyTTS implements TTS, SynthesizerListener, AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = "IFlyTTS";

    private static IFlyTTS iFlyTTS = null;
    private Context context = null;
    private boolean isPlaying = false;
    private AudioManager audioManager = null;
    private ICallBack callBack = null;

    private SpeechSynthesizer speechSynthesizer;

    public static IFlyTTS getInstance(Context context) {
        if (iFlyTTS == null) {
            iFlyTTS = new IFlyTTS(context);
        }
        return iFlyTTS;
    }

    private IFlyTTS(Context context) {
        this.context = context;
        createSynthesizer();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private void createSynthesizer() {
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(context,
                ecocide -> {
                    if (ErrorCode.SUCCESS == ecocide) {
                        //初始化成功
                        Log.i(TAG, "TTS初始化成功");
                    }
                });
    }

    ////////////////////TTS//////////////////////
    @Override
    public void init() {
        //设置发音人
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置语速,值范围：[0, 100],默认值：50
        speechSynthesizer.setParameter(SpeechConstant.SPEED, "55");
        //设置音量
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "tts_volume");
        //设置语调
        speechSynthesizer.setParameter(SpeechConstant.PITCH, "tts_pitch");
        //设置与其他音频软件冲突的时候是否暂停其他音频
        speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        //女生仅vixy支持多音字播报
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "vixy");
    }

    @Override
    public void playText(String playText) {
        Log.i(TAG, "play text:" + playText);
        if (playText != null && playText.contains("京藏")) {
            playText = playText.replace("京藏", "京藏[=zang4]");
        }
        if (playText != null && playText.length() > 0) {
            int result = audioManager.requestAudioFocus(this,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                speechSynthesizer.startSpeaking(playText, this);
                isPlaying = true;
            }
        }
    }

    @Override
    public void stopSpeak() {
        Log.i(TAG, "stopSpeak");
        if (speechSynthesizer != null) {
            speechSynthesizer.stopSpeaking();
        }
        isPlaying = false;
    }

    @Override
    public void destroy() {
        Log.i(TAG, "destroy");
        stopSpeak();
        if (speechSynthesizer != null) {
            speechSynthesizer.destroy();
        }
        iFlyTTS = null;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void setCallback(ICallBack callback) {
        this.callBack = callback;
    }

    ////////////////////OnAudioFocusChangeListener///////////////////////
    @Override
    public void onAudioFocusChange(int i) {

    }

    ////////////////////SynthesizerListener//////////////////////////////
    @Override
    public void onSpeakBegin() {
        isPlaying = true;
    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {
        isPlaying = false;
    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {
        Log.i(TAG, "onCompleted");
        isPlaying = false;
        if (audioManager != null) {
            audioManager.abandonAudioFocus(this);
        }
        if (callBack != null) {
            if (speechError == null) {
                callBack.onCompleted(0);
            }
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }
}
