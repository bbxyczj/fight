package fight.utils;

import com.sun.nio.file.ExtendedCopyOption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

/**
 * @Author XE-CZJ
 * @Date 2022/8/25 15:16
 */
public class FileUtils {


    public static class SendFileTest {
        public static void main(String[] args) {
            try {
                long l = System.currentTimeMillis();
                FileChannel readChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Documents\\王福义名下车辆.xlsx"), StandardOpenOption.READ);
                long len = readChannel.size();
                long position = readChannel.position();

                FileChannel writeChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Documents\\王福义名下车辆111.xlsx"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                //数据传输
                readChannel.transferTo(position, len, writeChannel);
                readChannel.close();
                writeChannel.close();
                System.out.println("time: "+(System.currentTimeMillis()-l));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class MmapTest {

        public static void main(String[] args) {
            try {
                long l = System.currentTimeMillis();
                FileChannel readChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Documents\\王福义名下车辆.xlsx"), StandardOpenOption.READ);
                MappedByteBuffer data = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, readChannel.size());
                FileChannel writeChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Documents\\王福义名下车辆222.xlsx"),
                        StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                //数据传输
                writeChannel.write(data);
                readChannel.close();
                writeChannel.close();
                System.out.println("time: "+(System.currentTimeMillis()-l));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static class normalFileTest{
        public static void main(String[] args) {
            try {
                long l = System.currentTimeMillis();
                FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Administrator\\Documents\\王福义名下车辆.xlsx");
                File file = new File("C:\\Users\\Administrator\\Documents\\王福义名下车辆333.xlsx");
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte buf[] = new byte[1024];
                int len;
                while ((len = fileInputStream.read(buf)) > 0) {
                    fileOutputStream.write(buf, 0, len);
                }
                System.out.println("time: "+(System.currentTimeMillis()-l));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class NioTest{
        public static void main(String[] args) {
            try {
                long l = System.currentTimeMillis();
                Files.copy(Paths.get("C:\\Users\\Administrator\\Documents\\王福义名下车辆.xlsx")
                        ,Paths.get("C:\\Users\\Administrator\\Documents\\王福义名下车辆444.xlsx"),StandardCopyOption.REPLACE_EXISTING);
                System.out.println("time: "+(System.currentTimeMillis()-l));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }



}
