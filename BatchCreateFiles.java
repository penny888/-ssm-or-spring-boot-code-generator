import javax.swing.*;
import java.io.*;
import java.util.Objects;

/**
 * 批量新建SSM或Spring boot各个层文件
 *
 * @author jack liu
 * @since 2020/05/09
 */
public class BatchCreateFiles {

    private static String PATH;

    private static String API_BASE_PATH;

    private static String SERVICE_BASE_PATH;

    private static String SERVICE_IMPL_BASE_PATH;

    private static String DAO_BASE_PATH;

    private static String DAO_XML_BASE_PATH;

    private static final String CONTROLLER_FILE_CONTENT = "package com.controller.api;\r\n\r\nimport com.service.ST;\r\nimport org.springframework.web.bind.annotation.PostMapping;\r\nimport org.springframework.web.bind.annotation.GetMapping;\r\nimport org.springframework.web.bind.annotation.RequestMapping;\r\nimport org.springframework.web.bind.annotation.RestController;\r\nimport javax.annotation.Resource;\r\nimport java.util.List;\r\n\r\n@RestController\r\n@RequestMapping(\"/api/MU\")\r\n" +
        "public class CN {\r\n\r\n\t@Resource\r\n\tprivate ST SN;\r\n\r\n\t@GetMapping(\"/list\")\r\n\tpublic List<?> list() { return SN.list(); }\r\n\r\n\t@GetMapping(\"/get-one\")\r\n\tpublic <T> T getOne() { return SN.getOne(); }\r\n\r\n\t@PostMapping(\"/insert\")\r\n\tpublic <T> T insert() { return SN.insert(); }\r\n\r\n\t@PostMapping(\"/update\")\r\n\tpublic <T> T update() { return SN.update(); }\r\n\r\n\t@PostMapping(\"/delete\")\r\n\tpublic <T> T delete() { return SN.delete(); }\r\n\r\n}";

    private static final String SERVICE_FILE_CONTENT = "package com.service;\r\n\r\nimport java.util.List;\r\n\r\npublic interface IN {\r\n\r\n\tList<?> list();\r\n\r\n\t<T> T getOne();\r\n\r\n\t<T> T insert();\r\n\r\n\t<T> T update();\r\n\r\n\t<T> T delete();\r\n\r\n}";

    private static final String SERVICE_IMPL_FILE_CONTENT = "package com.service.impl;\r\n\r\nimport com.service.ST;\r\nimport com.dao.MT;\r\nimport org.springframework.stereotype.Service;\r\nimport javax.annotation.Resource;\r\nimport java.util.List;\r\n\r\n@Service\r\n" +
        "public class CN implements ST {\r\n\r\n\t@Resource\r\n\tprivate MT MN;\r\n\r\n\t@Override\r\n\tpublic List<?> list() { return MN.list(); }\r\n\r\n\t@Override\r\n\tpublic <T> T getOne() { return MN.getOne(); }\r\n\r\n\t@Override\r\n\tpublic <T> T insert() { return MN.insert(); }\r\n\r\n\t@Override\r\n\tpublic <T> T update() { return MN.update(); }\r\n\r\n\t@Override\r\n\tpublic <T> T delete() { return MN.delete(); }\r\n\r\n}";

    private static final String DAO_FILE_CONTENT = "package com.dao;\r\n\r\nimport org.apache.ibatis.annotations.Mapper;\r\nimport java.util.List;\r\n\r\n@Mapper\r\npublic interface IN {\r\n\r\n\tList<?> list();\r\n\r\n\t<T> T getOne();\r\n\r\n\t<T> T insert();\r\n\r\n\t<T> T update();\r\n\r\n\t<T> T delete();\r\n\r\n}";

    private static final String DAO_XML_FILE_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
        "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
        "<mapper namespace=\"com.dao.MT\">\r\n\r\n\t<select id=\"list\"></select>\r\n\r\n\t<select id=\"getOne\"></select>\r\n\r\n\t<insert id=\"insert\"></insert>\r\n\r\n\t<update id=\"update\"></update>\r\n\r\n\t<delete id=\"delete\"></delete>\r\n\r\n</mapper>";

    private static String inputDirName;

    private static String inputModuleName;

    public static void main(String[] args) {
        if (!initDirPath()) System.exit(0); // 初始化各个层目录路径
        String input = JOptionPane.showInputDialog("请输功能模块名，例如UserLogin，如果有目录请用/分隔");
        try {
            if (Objects.isNull(input) || Objects.equals(input.trim(), "")) {
                System.out.println(" 输入不能为空或空字符串！");
                JOptionPane.showMessageDialog(null, "输入不能为空或空字符串", "", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else if (input.contains("/")) {
                // 输入包含目录
                inputDirName = input.split("/")[0];
                inputModuleName = input.split("/")[1];
            } else {
                // 输入不包含目录
                inputModuleName = input;
            }
            // 创建控制器类文件
            createFile(1, API_BASE_PATH, inputModuleName + "ApiController");
            // 创建服务接口文件
            createFile(2, SERVICE_BASE_PATH, inputModuleName + "Service");
            // 创建服务接口实现类文件
            createFile(3, SERVICE_IMPL_BASE_PATH, inputModuleName + "ServiceImpl");
            // 创建Dao层接口文件
            createFile(4, DAO_BASE_PATH, inputModuleName + "Mapper");
            // 创建Dao层xml文件
            createFile(5, DAO_XML_BASE_PATH, inputModuleName + "Mapper");

            JOptionPane.showMessageDialog(null, "批处理创建文件成功", "", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, e.toString(), "", JOptionPane.WARNING_MESSAGE);
        }

    }

    // 创建文件1
    private static void createFile(int type, String basePath, String fileName) throws IOException {
        String fileExt = type == 5 ? ".xml" : ".java";
        File dir, file;
        if (inputDirName != null) {
            dir = new File(basePath + inputDirName);
            file = new File(basePath + inputDirName + File.separator + fileName + fileExt);
            if (dir.exists()) {
                createFile(type, fileName, file);
            } else {
                if (dir.mkdir()) {
                    createFile(type, fileName, file);
                }
            }
        } else {
            file = new File(basePath + File.separator + fileName + fileExt);
            createFile(type, fileName, file);
        }
    }

    // 创建文件2
    private static void createFile(int type, String fileName, File file) throws IOException {
        String msg = ""; // 消息提示
        String fileContent = ""; // 文件内容
        if (type == 1) {
            // 创建控制器类文件
            msg = "控制器类文件";
            fileContent = inputDirName != null ? CONTROLLER_FILE_CONTENT.replace("package com.controller.api;", "package com.controller.api." + inputDirName + ";").replace("com.service.ST", "com.service." + inputDirName + ".ST") : CONTROLLER_FILE_CONTENT;
            String ApiUrlModule = inputModuleName.replaceAll("([A-Z][a-z]+)", "$1-").toLowerCase();
            fileContent = fileContent.replace("CN", fileName).replace("ST", inputModuleName + "Service").replace("SN", inputModuleName.substring(0, 1).toLowerCase() + inputModuleName.substring(1) + "Service").replace("MU", ApiUrlModule.substring(0, (ApiUrlModule.length() - 1)));
        } else if (type == 2) {
            // 创建服务接口文件
            msg = "服务接口文件";
            fileContent = SERVICE_FILE_CONTENT.replace("IN", fileName);
            fileContent = inputDirName != null ? fileContent.replace("package com.service;", "package com.service." + inputDirName + ";") : fileContent;
        } else if (type == 3) {
            // 创建服务实现类文件
            msg = "服务实现类文件";
            fileContent = inputDirName != null ? SERVICE_IMPL_FILE_CONTENT.replace("package com.service.impl;", "package com.service.impl." + inputDirName + ";").replace("com.service.ST", "com.service." + inputDirName + ".ST").replace("com.dao.MT", "com.dao." + inputDirName + ".MT") : SERVICE_IMPL_FILE_CONTENT;
            fileContent = fileContent.replace("CN", fileName).replace("ST", inputModuleName + "Service").replace("MT", inputModuleName + "Mapper").replace("MN", inputModuleName.substring(0, 1).toLowerCase() + inputModuleName.substring(1) + "Mapper");
        } else if (type == 4) {
            // 创建Dao层接口文件
            msg = "Dao层接口文件";
            fileContent = DAO_FILE_CONTENT.replace("IN", fileName);
            fileContent = inputDirName != null ? fileContent.replace("package com.dao;", "package com.dao." + inputDirName + ";") : fileContent;
        } else if (type == 5) {
            // 创建Dao层xml文件
            msg = "Dao层xml文件";
            fileContent = DAO_XML_FILE_CONTENT.replace("MT", fileName);
            fileContent = inputDirName != null ? fileContent.replace("com.dao.", "com.dao." + inputDirName + ".") : fileContent;
        }
        if (file.exists()) {
            JOptionPane.showMessageDialog(null, msg + "已经存在", "", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        if (file.createNewFile()) {
            System.out.println(msg + "创建成功");
            try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file)))) {
                out.write(fileContent);
            }
        }
    }

    // 初始化各个层目录路径
    private static boolean initDirPath() {
        File baseDir = new File("./src");
        searchFile(baseDir, "api");
        if (Objects.isNull(PATH)) {
            JOptionPane.showMessageDialog(null, "api目录不存在", "", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        API_BASE_PATH = PATH;
        PATH = null;
        searchFile(baseDir, "service");
        if (Objects.isNull(PATH)) {
            JOptionPane.showMessageDialog(null, "service目录不存在", "", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        SERVICE_BASE_PATH = PATH;
        PATH = null;
        searchFile(new File(SERVICE_BASE_PATH), "impl");
        if (Objects.isNull(PATH)) {
            JOptionPane.showMessageDialog(null, "service/impl目录不存在", "", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        SERVICE_IMPL_BASE_PATH = PATH;
        PATH = null;
        searchFile(baseDir, "dao");
        if (Objects.isNull(PATH)) {
            JOptionPane.showMessageDialog(null, "dao目录不存在", "", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        DAO_BASE_PATH = PATH;
        PATH = null;
        searchFile(baseDir, "mapper");
        if (Objects.isNull(PATH)) {
            JOptionPane.showMessageDialog(null, "mapper目录不存在", "", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        DAO_XML_BASE_PATH = PATH;
        PATH = null;
        return true;
    }

    // 搜索各个层目录路径
    private static void searchFile(File folder, final String keyword) {
        File[] subFolders = folder.listFiles(File::isDirectory);
        if (subFolders != null && subFolders.length > 0) {
            for (File subFolder : subFolders) {
                if (subFolder.getName().toLowerCase().equals(keyword)) {
                    PATH = subFolder.getAbsolutePath() + File.separator;
                } else {
                    searchFile(subFolder, keyword);
                }
            }
        }
    }


}
