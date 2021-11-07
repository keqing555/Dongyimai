import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

public class Test_FastDFS {
    public static void main(String[] args) throws MyException, IOException {
        //加载配置文件
        ClientGlobal.init("./fdfs_client.conf");
        //创建trackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //创建连接，获取trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建StorageServer的引用
        StorageServer storageServer = null;
        //创建StorageClient对象，需要参数：TrackerServer对象和StorageServer的引用
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //使用StorageClient上传jpg文件
     //   String[] file = storageClient.upload_file("D:/Upload/1f5075a2-1f32-4d34-880a-2b4779a9dd5e.jpg", "jpg", null);
        String[] file = storageClient.upload_file("C:\\Users\\Jarvis\\Videos\\雷电将军PV.mp4", "mp4", null);
        //返回数组，包括组名和图片的路径
        for (String str : file) {
            System.out.println(str);
        }
    }
}
