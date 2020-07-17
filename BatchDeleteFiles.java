import javax.swing.*;
import java.io.File;
import java.util.Objects;

/**
 * 批量删除SSM或Spring boot各个层文件
 *
 * @author jack liu
 * @since 2020/05/10
 */
public class BatchDeleteFiles {

    private static String PATH;

    private static String API_BASE_PATH;

    private static String SERVICE_BASE_PATH;

    private static String SERVICE_IMPL_BASE_PATH;

    private static String DAO_BASE_PATH;

    private static String DAO_XML_BASE_PATH;

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
            deleteFile(1, API_BASE_PATH ,inputModuleName + "ApiController.java");
            // 创建服务接口文件
            deleteFile(2, SERVICE_BASE_PATH ,inputModuleName + "Service.java");
            // 创建服务接口实现类文件
            deleteFile(3, SERVICE_IMPL_BASE_PATH,inputModuleName + "ServiceImpl.java");
            // 创建Dao层接口文件
            deleteFile(4, DAO_BASE_PATH,inputModuleName + "Mapper.java");
            // 创建Dao层xml文件
            deleteFile(5, DAO_XML_BASE_PATH,inputModuleName + "Mapper.xml");

            JOptionPane.showMessageDialog(null, "批处理删除文件成功", "",JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, e.toString(), "", JOptionPane.WARNING_MESSAGE);
        }
    }

    // 删除文件1
    private static void deleteFile(int type, String basePath, String fileFullName) {
        String msg = "";
        File dir, file;
        switch (type) {
            case 1 : msg = "控制器层"; break;
            case 2 : msg = "服务接口层"; break;
            case 3 : msg = "服务实现层"; break;
            case 4 : msg = "Dao层"; break;
            case 5 : msg = "xml层"; break;
            default: break;
        }
        if (inputDirName != null) {
            dir = new File(basePath + inputDirName);
            file = new File(basePath + inputDirName + File.separator + fileFullName);
            if (dir.exists()) {
                deleteFile(file, msg);
            } else {
                JOptionPane.showMessageDialog(null, msg + "目录不存在", "", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            file = new File(basePath + File.separator + fileFullName);
            deleteFile(file, msg);
        }

    }

    // 删除文件2
    private static void deleteFile(File file, String msg) {
        if (file.exists()) {
            if (file.delete()) {
                System.out.println(msg + " 文件删除成功！");
            } else {
                System.out.println(msg + " 文件删除失败！");
            }
        } else {
            JOptionPane.showMessageDialog(null, msg + "文件不存在", "", JOptionPane.WARNING_MESSAGE);
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
