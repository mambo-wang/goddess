package com.h3c.vdi.athena.common.utils;

import com.h3c.vdi.athena.common.constant.FileConstant;
import com.h3c.vdi.athena.common.constant.TimeConstant;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 导出excel工具类
 */
public final class PoiUtils {

    private static Logger log = LoggerFactory.getLogger(PoiUtils.class);

    /**
     * 完整时间格式yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PoiUtils() {}

    /**
     * 格式化日期时间
     * @param time      long类型日期
     * @param formatter 时间格式
     * @return 格式化后日期时间
     */
    public static String formatDate(Long time, DateTimeFormatter formatter) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, TimeConstant.BEIJING_ZONE);
        return localDateTime.format(formatter);
    }

    /**
     * 将日期时间格式化成yyyy-MM-dd HH:mm:ss格式
     * @param datetime long类型日期时间
     * @return 格式化后日期时间
     */
    public static String formatFullDateTime(Long datetime) {
        return formatDate(datetime, FORMATTER_FULL);
    }

    /**
     * 获取客户端浏览器类型、编码下载文件名
     * @param request
     * @param fileName
     * @return String
     * @author kf6567
     * @date 2016-8-17
     */
    public static String encodeFileName(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        String rtn = "";
        try {
            String new_filename = URLEncoder.encode(fileName, "UTF8");

            // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            rtn = "filename=\"" + new_filename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.contains("msie")) {
                    rtn = "filename=\"" + new_filename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.contains("opera")) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.contains("safari")) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.contains("applewebkit")) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.contains("mozilla")) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.info("", e);
        }
        return rtn;
    }

    /**
     * 下载到客户端浏览器
     *
     * @param workbook 要下载的Excel表格文件
     * @param response 响应
     * @param fileName 文件名称
     * @throws IOException io流异常
     */
    public static void downloadBrowser(Workbook workbook, HttpServletResponse response, String fileName, HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");

        // 清空response
        response.reset();

        // 设置response的响应头Header，控制浏览器以下载的形式打开文件
        response.setHeader("Content-disposition",
                "attachment;" + encodeFileName(request, fileName));
        //获得输出流，包装成缓冲流可以提高输入、输出效率。但需要写flush方法才能清空缓冲区
        OutputStream toClient = new BufferedOutputStream(
                response.getOutputStream());

        //设置文件类型和编码格式
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        //将表格数据直接写入到输出流
        workbook.write(toClient);

        //flush方法迫使缓冲的输出数据被写出到底层输出流中，其实其内部也是调用write方法
        toClient.flush();
        //关闭流
        toClient.close();
    }

    /**
     * 根据文件后缀名判断该文件是否是Excel表格，如果是则返回true，否则返回FALSE
     *
     * @param fileType 表示文件类型的后缀
     * @return 布尔值
     */
    public static boolean isExcelFile(String fileType) {
        if (fileType.equalsIgnoreCase(FileConstant.FILE_EXCEL_XLS) || fileType.equalsIgnoreCase(FileConstant.FILE_EXCEL_XLSX)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成默认表格样式
     * @param workbook 表格
     * @return 样式
     */
    public static CellStyle getDefaultCellStyle(Workbook workbook){
        //样式
        CellStyle cellStyle = workbook.createCellStyle();
        //垂直居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //能换行
        cellStyle.setWrapText(true);
        //边框为黑色细实线
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 生成默认字体
     * @param workbook 表格
     * @return 字体
     */
    public static Font getDefaultFont(Workbook workbook){
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        return font;
    }

    /**
     * 获取单元格样式
     *
     * @param workbook excel文件（创建样式用）
     * @return 第单元格样式
     */
    public static CellStyle getCommonCellStyle(Workbook workbook) {
        //样式
        CellStyle cellStyle = PoiUtils.getDefaultCellStyle(workbook);
        //左对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //字体
        cellStyle.setFont(PoiUtils.getCommonCellFont(workbook));
        return cellStyle;
    }

    /**
     * 普通文本的字体
     * @param workbook excel
     * @return 字体
     */
    public static Font getCommonCellFont(Workbook workbook){
        Font font = PoiUtils.getDefaultFont(workbook);
        font.setColor(Font.COLOR_NORMAL);
        return font;
    }

    /**
     * 获取单元格样式
     *
     * @param workbook excel文件（创建样式用）
     * @return 第单元格样式
     */
    public static CellStyle getMouldCellStyle(Workbook workbook) {
        //样式
        CellStyle cellStyle = PoiUtils.getDefaultCellStyle(workbook);
        //左对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cellStyle.setFont(PoiUtils.getMouldCellFont(workbook));
        return cellStyle;
    }

    /**
     * 获取excel模板提示信息的字体
     * @param workbook excel
     * @return 字体
     */
    public static Font getMouldCellFont(Workbook workbook){
        Font font = PoiUtils.getDefaultFont(workbook);
        font.setColor(Font.COLOR_RED);
        return font;
    }

    /**
     * 获取第一列样式
     *
     * @param workbook excel文件（创建样式用）
     * @return 第一列样式
     */
    public static CellStyle getFirstCellStyle(Workbook workbook) {
        CellStyle firstCellStyle = PoiUtils.getDefaultCellStyle(workbook);
        //居中
        firstCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //字体
        firstCellStyle.setFont(PoiUtils.getFirstCellFont(workbook));
        return firstCellStyle;
    }

    /**
     * 获取标题行的字体
     * @param workbook excel
     * @return 字体
     */
    public static Font getFirstCellFont(Workbook workbook){
        Font font = PoiUtils.getDefaultFont(workbook);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 14);
        return font;
    }

    /**
     * 根据excel文件生成一个名字为sheetName的sheet，在根据headers生成表头
     *
     * @param workbook  excl文件
     * @param sheetName sheet的名字
     * @param headers   表头集合
     * @return 表
     */
    public static Sheet createSheetWithTitle(Workbook workbook, String sheetName, List<String> headers,int titleRowNumber) {

        Sheet sheet = workbook.createSheet(sheetName);

        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        CellStyle style = PoiUtils.getFirstCellStyle(workbook);

        // 产生表格标题行
        Row row = sheet.createRow(titleRowNumber);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers.get(i));
        }
        return sheet;
    }

    public static Sheet createSheetWithTitleOfListMode(Workbook workbook, String sheetName, List<String> headers,int titleRowNumber) {

        int numberOfSheets = workbook.getNumberOfSheets();

        Sheet sheet;

        if(numberOfSheets > 0){
            sheet = workbook.getSheetAt(0);
        }else {
            sheet = workbook.createSheet(sheetName);
        }
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        CellStyle style = PoiUtils.getFirstCellStyle(workbook);

        // 产生表格标题行
        Row row = sheet.createRow(titleRowNumber);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers.get(i));
        }
        return sheet;
    }

    /**
     * 根据excel文件生成一个名字为sheetName的sheet，在根据headers生成表头
     *
     * @param workbook  excl文件
     * @param sheetName sheet的名字
     * @param headers   表头集合
     * @return 表
     */
    public static Sheet createSheetWithRichTitle(Workbook workbook, String sheetName, List<RichTextString> headers, int titleRowNumber) {

        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        CellStyle style = PoiUtils.getFirstCellStyle(workbook);

        // 产生表格标题行
        Row row = sheet.createRow(titleRowNumber);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers.get(i));
        }
        return sheet;
    }

    /**
     * 通用的创建表格方法
     * @param dataList 需要导出的数据集合
     * @param fieldNames 属性名集合
     * @param headers 表头集合
     * @param sheetName 数据表名称
     * @return WorkBook对象
     * @throws NoSuchFieldException 找不到属性
     * @throws IllegalAccessException
     */
    public static Workbook createCommonWorkbookWithCommonTitle(List dataList, List<String> fieldNames, List<String> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {

        // 声明一个工作薄，xls格式，新版excel也能打开
        HSSFWorkbook workbook = new HSSFWorkbook();
        return createCommonWorkbookWithCommonTitle(workbook, dataList, fieldNames, headers, sheetName);
    }

    public static Workbook createCommonWorkbookWithCommonTitle(Workbook workbook, List dataList, List<String> fieldNames, List<String> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {
        // 创建一个数据表(sheet)并且生成表头
        Sheet sheet = createSheetWithTitle(workbook,sheetName,headers,0);
        return createCommonWorkbook(workbook, sheet, dataList, fieldNames);
    }

    public static Workbook createCommonWorkbookWithRichTitle(Workbook workbook, List dataList, List<String> fieldNames, List<RichTextString> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {
        // 创建一个数据表(sheet)并且生成表头
        Sheet sheet = createSheetWithRichTitle(workbook,sheetName,headers,0);

        return createCommonWorkbook(workbook, sheet, dataList, fieldNames);
    }

    public static Workbook createCommonWorkbook(Workbook workbook, Sheet sheet, List dataList, List<String> fieldNames) throws NoSuchFieldException, IllegalAccessException {
        CellStyle defaultCellStyle = PoiUtils.getCommonCellStyle(workbook);

        // 遍历集合数据，产生数据行
        Row row;
        int index = 0;
        for (Object data : dataList) {
            index++;
            row = sheet.createRow(index);
            row.setHeightInPoints(25);
            int cellIndex = 0;
            //获取集合元素的类类型，也就是要下载的类的类类型
            Class c = data.getClass();

            //遍历属性名集合，获取每一个要导出的属性的名字
            for (String fieldName : fieldNames) {

                //如果是属性中有好几级属性（该字段是引用类型），获取每一级属性的名字
                String[] fields = fieldName.split("\\.");

                //定义要插入到数据库中的数据对象
                Object insertToCell = null;
                for (int i = 0; i < fields.length; i++) {

                    //i== 0说明是第一级属性
                    if (i == 0) {
                        //从data中根据属性名获取属性对象
                        Field nameField = c.getDeclaredField(fields[i]);
                        //设置属性对象可读
                        nameField.setAccessible(true);
                        //获取data中该属性的值
                        insertToCell = nameField.get(data);
                        //如果第一级属性为null，则不解析下一级属性
                        if (insertToCell == null) {
                            break;
                        }
                    } else {
                        //从上一级属性值中获取属性对象
                        Field nameField = insertToCell.getClass().getDeclaredField(fields[i]);
                        //设置可读
                        nameField.setAccessible(true);
                        //获取上一级属性对象中该属性的值
                        insertToCell = nameField.get(insertToCell);
                    }
                }

                //创建单元格
                Cell cell = row.createCell(cellIndex++);
                //设置单元格类型为字符串
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(defaultCellStyle);
                //插入数据
                if (insertToCell == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue("" + insertToCell);
                }
            }
        }

        return workbook;
    }

    public static void createTitleRow(Sheet sheet, HSSFWorkbook workbook, String title, int lastCol) {
        //标题行
        Row rowTitle = sheet.createRow(0);
        rowTitle.setHeight((short) (15 * 35));
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellStyle(PoiUtils.getFirstCellStyle(workbook));
        cellTitle.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
    }

}
