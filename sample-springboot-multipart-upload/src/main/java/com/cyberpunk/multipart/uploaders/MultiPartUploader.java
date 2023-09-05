package com.cyberpunk.multipart.uploaders;

/**
 * @author lujun
 * @date 2023/9/5 14:56
 */
public interface MultiPartUploader {


    void doStart();

    void doUpload();

    void doFinish();


    void doStop();

    void doCancel();


}
