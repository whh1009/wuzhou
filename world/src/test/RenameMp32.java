package test;

import java.io.*;

/**
 * Created by wanghonghui on 2015/5/6.
 */
public class RenameMp32 {
    public static void main(String [] args) throws Exception {
        String txtPath = "H:\\y于小燕mp3改名\\20150506\\out.txt";
        String [] lines = fileInput(txtPath).split("@@@");
        for(String line : lines) {

            String srcPath = "H:\\y于小燕mp3改名\\20150506\\汉藏英图解小词典MP3\\汉藏英图解小词典MP3\\"+line.split("\t")[0].replace(" ", "");
            String destPath = "H:\\y于小燕mp3改名\\20150506\\小词典\\"+line.split("\t")[1].replace(" \\","\\").replace("?", "？");

            copyFile(new File(srcPath), new File(destPath), false);
        }

    }

    public static String fileInput(String filePath) {
        StringBuffer sb = new StringBuffer();
        File file = new File(filePath);
        try {
            FileInputStream stream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(stream, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp).append("@@@");
            }
            br.close();
            isr.close();
            stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 单个文件拷贝。
     *
     * @param srcFile
     * @param destFile
     * @param overwrite
     *            是否覆盖目的文件
     * @throws IOException
     */
    public static void copyFile(File srcFile, File destFile, boolean overwrite) throws IOException {
        if(!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        // 首先判断源文件是否存在
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
        }
        // 判断源文件是否可读
        if (!srcFile.canRead()) {
            throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
        }

        if (overwrite == false) {
            // 目标文件存在就不覆盖
            if (destFile.exists())
                return;
        } else {
            // 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
            if (destFile.exists()) {
                if (!destFile.canWrite()) {
                    throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
                }
            } else {
                // 不存在就创建一个新的空文件。
                if (!destFile.createNewFile()) {
                    throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
                }
            }
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        byte[] block = new byte[1024];
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
            while (true) {
                int readLength = inputStream.read(block);
                if (readLength == -1)
                    break;// end of file
                outputStream.write(block, 0, readLength);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    // just ignore
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    // just ignore
                }
            }
        }
    }
}

