package com.wen.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CORBA.StringHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExcelUtil
 * @Description
 * @Author 黄文聪
 * @Date 2019-11-26 10:22
 * @Version 1.0
 **/
@Slf4j
public class ExcelUtil {

    @Data
    static class User {
        private String name;
        private String email;
        private String department;
        private String status;
    }

    public static String importExcel(File file1, Integer sheets) {
        String pathName = "C:/Users/Administrator/Desktop/融易区块链邮箱.xlsx";
        File file = new File(pathName);
        if (file.exists()) {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            boolean ISXLS = fileName.matches("^.+\\.(?i)(xls)$");
            boolean ISXLSX = fileName.matches("^.+\\.(?i)(xlsx)$");
            if (ISXLS || ISXLSX) {
                boolean isExcel2003 = true;
                if (ISXLSX) {
                    isExcel2003 = false;
                }
                try {
                    InputStream is = new FileInputStream(file);
                    Workbook wb = null;
                    if (isExcel2003) {
                        wb = new HSSFWorkbook(is);
                    } else {
                        wb = new XSSFWorkbook(is);
                    }
                    Sheet sheet = wb.getSheetAt(sheets);
                    if (null == sheet) {
                        log.info("输入表格出错。");
                    }
                    List<User> list = new ArrayList<>();
                    for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (null == row)
                            continue;
                        User user = new User();
                        String name = row.getCell(0).getStringCellValue();
                        String email = row.getCell(2).getStringCellValue();
                        String department = row.getCell(3).getStringCellValue();
                        String status = row.getCell(4).getStringCellValue();
                        user.setName(name);
                        user.setEmail(email);
                        user.setDepartment(department);
                        user.setStatus(status);
                        list.add(user);
                    }
                    list.stream().forEach(System.out::println);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 读取文件
     *
     * @param filePath
     */
    public static void readFile(String filePath) {
        try {
            InputStream is = new FileInputStream(new File(filePath));
            Integer i = 0;
            byte[] buf = new byte[1024];
            while ((i = is.read(buf)) != -1) {
                log.info("读取的文件内容：" + new String(buf, 0, i));
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写入文件
     *
     * @param filePath
     */
    public static void writeFile(String filePath) {
        try {
            OutputStream os = new FileOutputStream(new File(filePath));
            String txt = "这是一段写入的文本";
            os.write(txt.getBytes());
            os.close();
            log.info("第一次写入文本成功");
            Thread.sleep(10000);
            OutputStream os1 = new FileOutputStream(filePath, true);
            String appendTxt = "\r\n" + "这是追加的一段文本";
            os1.write(appendTxt.getBytes());
            os1.close();
            log.info("第二次追加成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
//        String pathName = "C:/Users/Administrator/Desktop/融易区块链邮箱.xlsx";
//        importExcel(new File(pathName),0);
//        String readPath = "C:/Users/Administrator/Desktop/read.txt";
//        String writePath = "C:/Users/Administrator/Desktop/write.txt";
//        readFile(readPath);
//        writeFile(writePath);

    }
}
