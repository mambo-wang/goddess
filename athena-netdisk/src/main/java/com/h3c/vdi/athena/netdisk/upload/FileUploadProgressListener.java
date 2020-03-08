package com.h3c.vdi.athena.netdisk.upload;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author w14014
 */
@Slf4j
@Component
public class FileUploadProgressListener implements ProgressListener {
    private HttpSession session;
    public void setSession(HttpSession session){
        this.session=session;
        Progress status = new Progress();
        session.setAttribute("status", status);
    }
    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        Progress status = (Progress)session.getAttribute("status");
        status.setPBytesRead(pBytesRead);
        status.setPContentLength(pContentLength);
        status.setPItems(pItems);

        log.info("=================={}==============", status);

    }

}
