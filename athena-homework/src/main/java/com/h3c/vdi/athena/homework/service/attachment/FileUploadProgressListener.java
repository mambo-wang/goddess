package com.h3c.vdi.athena.homework.service.attachment;

/**
 * Created by w16051 on 2018/4/25.
 */

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class FileUploadProgressListener implements ProgressListener {
    private HttpSession session;
    public void setSession(HttpSession session){
        this.session=session;
        Progress status = new Progress();//保存上传状态
        session.setAttribute("status", status);
    }
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        Progress status = (Progress) session.getAttribute("status");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        status.setpBytesRead(pBytesRead);
        status.setpContentLength(pContentLength);
        status.setpItems(pItems);
        System.out.println(">>>>>>>>>>>>>>>>>>>>"+status);

    }

}
