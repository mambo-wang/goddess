package com.h3c.vdi.athena.homework.dao;

import com.h3c.vdi.athena.homework.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by w16051 on 2018/3/27.
 */
public interface AttachmentDao extends JpaRepository<AttachmentEntity,Long>{

    @Query(value = "select * from tbl_attachments where TYPE=?1 and RELATED_ID=?2 and IS_DELETED=?3", nativeQuery = true)
    List<AttachmentEntity> queryAttachmentsByTypeAndRelatedIdAndDeleted(Integer type,Long relatedId,String deleted);

    @Query(value = "select * from tbl_attachments where TYPE=?1 and RELATED_ID in ?2 and IS_DELETED=?3", nativeQuery = true)
    List<AttachmentEntity> queryAttachmentsByTypeAndRelatedIdInAndDeleted(Integer type,List<Long> relatedIds,String deleted);

    @Query(value = "select * from tbl_attachments where URL=?1 and Name=?2 and IS_DELETED=?3", nativeQuery = true)
    AttachmentEntity queryAttachmentByURLAndName(String url,String name,String deleted);

    @Override
    @Query(value = "select * from tbl_attachments where ID=?1 and IS_DELETED='n'", nativeQuery = true)
    AttachmentEntity getOne(Long id);

    @Query(value = "select ID from tbl_attachments where TYPE=?1 and RELATED_ID=?2 and IS_DELETED=?3", nativeQuery = true)
    List<Long> queryAttachmentIdsByTypeAndRelatedIdAndDeleted(Integer type,Long relatedId,String deleted);
}
