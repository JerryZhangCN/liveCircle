package com.lvr.livecircle.api;


import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.lvr.livecircle.app.AppApplication;
import com.lvr.livecircle.bean.Cache;

import java.util.Date;

/**
 * 上传图片到阿里云服务器
 * 依照开发文档进行开发
 */

public class ImgUpload {
    public static OSS oss;
    public static final String bucketName="oss-community";
    public static final String objectKey="";
    public static final String end_point="https://oss-cn-hangzhou.aliyuncs.com";


    private static void init() {
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Cache.getInstance().getSts().getAccessKeyId(), Cache.getInstance().getSts().getAccessKeySecret());
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(Cache.getInstance().getSts().getAccessKeyId(), Cache.getInstance().getSts().getAccessKeySecret(),Cache.getInstance().getSts().getSecurityToken());
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

// oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        oss = new OSSClient(AppApplication.mMainContext, end_point, credentialProvider, conf);
    }

    public static String upload(String path){
        init();
        // 构造上传请求
        String fileName=String.valueOf(new Date().getTime())+".png";
        PutObjectRequest put = new PutObjectRequest(bucketName, fileName, path);
// 文件元信息的设置是可选的
// ObjectMetadata metadata = new ObjectMetadata();
// metadata.setContentType("application/octet-stream"); // 设置content-type
// metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
// put.setMetadata(metadata);
        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("上传图片", path);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
            return "https://oss-community.oss-cn-hangzhou.aliyuncs.com/"+fileName;
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("异常", e.getRequestId());
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
            return null;
        }
        return null;
    }
}
