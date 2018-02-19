package com.kuaijie.carrescue.util.threadpool;

import com.kuaijie.carrescue.constant.ServerAddress;
import com.kuaijie.carrescue.entity.Photo;
import com.kuaijie.carrescue.util.TaskNotification;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.task.XExecutor;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.File;
import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Created by MitsukiSaMa on 11-27.
 */

public class TaskThreadPool implements XExecutor.OnAllTaskEndListener {

    private boolean isPaused;

    private TaskThreadPoolExecutor taskThreadPoolExecutor;
    private TaskThreadPoolExecutor dataCacheThreadPoolExecutor;

    private OkUpload okUpload;

    private static class ThreadPoolHolder {
        private static final TaskThreadPool INSTANCE = new TaskThreadPool();
    }

    private TaskThreadPool() {
    }

    public static final TaskThreadPool getInstance() {
        return ThreadPoolHolder.INSTANCE;
    }

    public void init() {
        taskThreadPoolExecutor = new TaskThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        dataCacheThreadPoolExecutor = new TaskThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        okUpload = OkUpload.getInstance();
        okUpload.getThreadPool().setCorePoolSize(1);
        okUpload.addOnAllTaskEndListener(this);
    }

    public void addTask(mission m) {
        taskThreadPoolExecutor.execute(() -> {
            try {
                m.task();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public interface mission {
        void task();
    }

    public void pause() {
        System.out.println("=========>暂停线程池");
        taskThreadPoolExecutor.pause();
    }

    public void resume() {
        System.out.println("=========>恢复线程池");
        taskThreadPoolExecutor.resume();
    }

    public boolean getPaused() {
        return taskThreadPoolExecutor.getPaused();
    }

    public long getTaskCount() {
        return taskThreadPoolExecutor.getTaskCount() - taskThreadPoolExecutor.getCompletedTaskCount();
    }

    public void addCacheTask(mission m) {
        dataCacheThreadPoolExecutor.execute(() -> {
            try {
                m.task();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void addImgUploadTask(List<Photo> photos) {
        for (Photo photo : photos) {
            File f = new File(photo.getFileAddress());
            if (f.exists()) {
                PostRequest<String> postRequest = OkGo.<String>post(ServerAddress.UPLOAD_IMG_SERVER)
                        .params("fileName", photo.getFileName())
                        .params("file", f)
                        .converter(new StringConvert());

                UploadTask<String> task = OkUpload.request(photo.getFileName(), postRequest)
                        .save().register(new ImgUploadListener<>());
                task.start();
            }
        }
    }

    //////////////XExecutor.OnAllTaskEndListener/////////////////////
    @Override
    public void onAllTaskEnd() {
        //所有上传任务已完成,隐藏任务栏通知
        TaskNotification.getInstance()
                .removeNotification(TaskNotification.IMG_UP_LOAD);
    }

    private class ImgUploadListener<T> extends UploadListener<T> {

        public ImgUploadListener() {
            super("ImgUploadListener");
        }

        @Override
        public void onStart(Progress progress) {
            //任务开始
            TaskNotification.getInstance().imgUpload(progress,
                    OkUpload.restore(UploadManager.getInstance().getUploading()).size());
        }

        @Override
        public void onProgress(Progress progress) {
            //上传中
            //更新进度条
            TaskNotification.getInstance().imgUpload(progress,
                    OkUpload.restore(UploadManager.getInstance().getUploading()).size());
        }

        @Override
        public void onError(Progress progress) {
            //上传失败
            //重新上传当前任务
            if (okUpload.hasTask(progress.tag))
                okUpload.getTask(progress.tag).restart();
            TaskNotification.getInstance().imgUpload(progress,
                    OkUpload.restore(UploadManager.getInstance().getUploading()).size());
        }

        @Override
        public void onFinish(T t, Progress progress) {
            //上传结束
            //remove掉当前任务，更新任务栏提示
            if (true) {
                if (okUpload.hasTask(progress.tag))
                    okUpload.removeTask(progress.tag);
                File file = new File(progress.filePath + progress.fileName);
                if (file.isFile() && file.exists())
                    file.delete();
                TaskNotification.getInstance().imgUpload(progress,
                        OkUpload.restore(UploadManager.getInstance().getUploading()).size());
            }
        }

        @Override
        public void onRemove(Progress progress) {
            //任务移除
            //remove当前任务
        }
    }
}
