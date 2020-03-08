package com.h3c.vdi.athena.netdisk.service.disk;

import com.h3c.vdi.athena.netdisk.model.dto.BatchShareFileDTO;
import com.h3c.vdi.athena.netdisk.model.dto.FileInfoDTO;
import com.h3c.vdi.athena.netdisk.model.dto.MoveCopyDTO;
import com.h3c.vdi.athena.netdisk.model.dto.ShareFileDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
public interface NetDiskMgr {

    /**
     * 查询某路径下的文件列表
     * @param path 路径
     * @return 文件列表
     * @throws IOException
     */
    List<FileInfoDTO> listFiles(String path) throws IOException;

    /**
     * 查询目录树，将所有文件以树状结构返回
     * @return 所有文件
     * @throws IOException 文件异常
     */
    List<FileInfoDTO> listFiles() throws IOException;

    /**
     * 上传单个文件
     * @param file 文件
     * @param path 路径
     * @param username 当前登陆用户名
     * @param fileName 文件名
     * @throws IOException 异常
     */
    void uploadFiles(MultipartFile file, String path, String username, String fileName) throws IOException;

    /**
     * 批量上传文件
     * @param files 文件
     * @param path 路径
     * @param username 当前登陆用户名
     * @throws IOException IO异常
     */
    void uploadFiles(MultipartFile[] files, String path, String username) throws IOException;

    /**
     * 下载文件
     * @param path 路径
     * @param username 用户名
     * @param response 浏览器响应
     * @throws IOException IO异常
     */
    void downloadFiles(String path, String username, HttpServletResponse response) throws IOException;

    /**
     * 获取某个文件的详细信息
     * @param path 文件路径
     * @param username 用户名
     * @return 文件信息
     * @throws IOException IO异常
     */
    FileInfoDTO getSingleFileInfo(String path, String username) throws IOException;

    /**
     * 批量删除文件
     * @param paths 文件路径
     */
    void removeFile(String[] paths);

    /**
     * 单个分享文件，暂时不用
     * @param shareFileDTO 分享信息
     */
    @Deprecated
    void shareFile(ShareFileDTO shareFileDTO);

    /**
     * 批量分享文件
     * @param shareFileDTO 分享文件的信息
     */
    void shareFile(BatchShareFileDTO shareFileDTO);

    /**
     * 批量取消分享
     * @param shareIds 分享id
     */
    void cancelShareFile(Long[] shareIds);

    /**
     * 按照文件类型查询
     * @param type 文件类型: video/document/music/picture/compress
     * @return 文件信息
     */
    List<FileInfoDTO> queryByType(String type);

    /**
     * 创建文件夹
     * @param path 文件夹路径
     * @throws IOException IO异常
     */
    void createFolder(String path) throws IOException;

    /**
     * 移动文件
     * @param moveCopyDTO 移动信息
     */
    void moveFile(MoveCopyDTO moveCopyDTO);

    /**
     * 复制文件
     * @param moveCopyDTO 复制文件
     */
    void copyFile(MoveCopyDTO moveCopyDTO);

    /**
     * 查询文件分享信息
     * @param sharedWith true:别人分享给我，false:我分享给别人
     * @return
     */
    List<FileInfoDTO> querySharedWith(String sharedWith);

}
