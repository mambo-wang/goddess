package com.h3c.vdi.athena.homework.service.attachment;

import com.h3c.vdi.athena.homework.dto.AttachmentDTO;
import com.h3c.vdi.athena.homework.entity.AttachmentEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by w16051 on 2018/3/27.
 */
public interface AttachmentService {

    /**
     * 根据关联类型和关联表ID获取附件list
     * @param type
     * @param relatedId
     * @return
     */
    List<AttachmentDTO> getAttachmentDTOsByTypeAndRelatedId(Integer type,Long relatedId);

    /**
     * 添加附件记录
     * @param name
     * @param url
     * @return
     */
    Long addAttachment(String name,String url);

    /**
     * 设置相关联的记录
     * 记得处理异常
     * @param id
     * @param type
     * @param relatedId
     */
    void setTypeAndRelatedId(Long id,Integer type,Long relatedId);

    /**
     * 下载附件
     * @param attachmentDTO
     */
    void downloadAttachment(AttachmentDTO attachmentDTO, HttpServletResponse response) throws IOException;

    /**
     * 根据关联类型和关联表ID获取附件IDlist
     * @param type
     * @param relatedId
     * @return
     */
    List<Long> getAttachmentIDsByTypeAndRelatedId(Integer type,Long relatedId);

    /**
     * 根据附件ID删除附件及相关记录
     * @param id
     */
    void deleteAttachmentById(Long id);

    /**
     * 删除附件及相关记录
     * @param attachmentEntity
     */
    void deleteAttachment(AttachmentEntity attachmentEntity);

    /**
     * 根据关联表类型和记录ID删除附件
     * @param type
     * @param relatedId
     */
    void deleteAttachmentsByTypeAndRelatedId(Integer type,Long relatedId);

    /**
     * 上传附件
     * @param
     * @return 返回附件在数据库中的记录的ID
     * @throws IOException
     */
    Long uploadAttachment(MultipartFile file, String username) throws IOException;

    /**
     * 将网盘中的文件拷贝到附件目录下，然后入库,传入文件在宿主机中的绝对路径
     * @param urls
     * @return
     */
    List<Long> makeAttachmentsByUrls(List<String> urls);

    /**
     * 将传入的附件拷贝一份并将其与传入的作业模板绑定
     * @param homeworkTemplateId
     * @param attachmentIds
     */
    void addAttachmentWithHomeworkTemplate(Long homeworkTemplateId,List<Long> attachmentIds);
}
