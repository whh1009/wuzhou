package com.wz.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.*;

public class FileUtil {

	/**
	 * 创建单个文件夹。
	 * 
	 * @param dir
	 * @param ignoreIfExitst
	 *            true 表示如果文件夹存在就不再创建了。false是重新创建。
	 * @throws java.io.IOException
	 */
	public static void createDir(String dir, boolean ignoreIfExitst) throws IOException {
		File file = new File(dir);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		if (file.mkdir() == false) {
			throw new IOException("Cannot create the directory = " + dir);
		}
	}

	/**
	 * 创建多个文件夹
	 * 
	 * @param dir
	 * @param ignoreIfExitst
	 * @throws java.io.IOException
	 */
	public static void createDirs(String dir, boolean ignoreIfExitst) throws IOException {
		File file = new File(dir);

		if (ignoreIfExitst && file.exists()) {
			return;
		}

		if (file.mkdirs() == false) {
			throw new IOException("Cannot create directories = " + dir);
		}
	}

	/**
	 * 删除一个文件。
	 * 
	 * @param filename
	 * @throws java.io.IOException
	 */
	public static void deleteFile(String filename) throws IOException {
		File file = new File(filename);
		if (file.isDirectory()) {
			throw new IOException("IOException -> BadInputException: not a file.");
		}
		if (file.exists() == false) {
			throw new IOException("IOException -> BadInputException: file is not exist.");
		}
		if (file.delete() == false) {
			throw new IOException("Cannot delete file. filename = " + filename);
		}
	}

	/**
	 * 删除文件夹及其下面的子文件夹
	 * 
	 * @param dir
	 * @throws java.io.IOException
	 */
	public static void deleteDir(File dir) throws IOException {
		if (dir.isFile())
			throw new IOException("IOException -> BadInputException: not a directory.");
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}// if
		dir.delete();
	}

	public static String getPathSeparator() {
		return File.pathSeparator;
	}

	public static String getFileSeparator() {
		return File.separator;
	}

	/**
	 * 获取到目录下面文件的大小。包含了子目录。
	 * 
	 * @param dir
	 * @return
	 * @throws java.io.IOException
	 */
	public static long getDirLength(File dir) throws IOException {
		if (dir.isFile())
			throw new IOException("BadInputException: not a directory.");
		long size = 0;
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				// file.getName();
				// System.out.println(file.getName());
				long length = 0;
				if (file.isFile()) {
					length = file.length();
				} else {
					length = getDirLength(file);
				}
				size += length;
			}// for
		}// if
		return size;
	}

	/**
	 * 将文件清空。
	 * 
	 * @param srcFilename
	 * @throws java.io.IOException
	 */
	public static void emptyFile(String srcFilename) throws IOException {
		File srcFile = new File(srcFilename);
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the file: " + srcFile.getAbsolutePath());
		}
		if (!srcFile.canWrite()) {
			throw new IOException("Cannot write the file: " + srcFile.getAbsolutePath());
		}

		FileOutputStream outputStream = new FileOutputStream(srcFilename);
		outputStream.close();
	}

	/**
	 * 写文件。如果此文件不存在就创建一个。
	 * 
	 * @param content
	 *            String
	 * @param fileName
	 *            String
	 * @param destEncoding
	 *            String
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static void writeFile(String content, String fileName, String destEncoding) throws FileNotFoundException, IOException {

		File file = null;
		try {
			file = new File(fileName);
			if (!file.exists()) {
				if (file.createNewFile() == false) {
					throw new IOException("create file '" + fileName + "' failure.");
				}
			}
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
			if (file.canWrite() == false) {
				throw new IOException("'" + fileName + "' is a read-only file.");
			}
		} finally {
			// we dont have to close File here
		}

		BufferedWriter out = null;
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));

			out.write(content);
			out.flush();
		} catch (FileNotFoundException fe) {
			throw fe;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 读取文件的内容，并将文件内容以字符串的形式返回。
	 * 
	 * @param fileName
	 * @param srcEncoding
	 * @return
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	public static String readFile(String fileName, String srcEncoding) throws FileNotFoundException, IOException {

		File file = null;
		try {
			file = new File(fileName);
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
		} finally {
			// we dont have to close File here
		}

		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));
			char[] block = new char[512];
			while (true) {
				int readLength = reader.read(block);
				if (readLength == -1)
					break;// end of file
				result.append(block, 0, readLength);
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
			throw fe;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
			}
		}
	}

	/*
	 * 1 ABC 2 abC Gia su doc tu dong 1 lay ca thay 5 dong => 1 --> 5 3 ABC
	 */
	public static String[] getLastLines(File file, int linesToReturn) throws IOException, FileNotFoundException {

		final int AVERAGE_CHARS_PER_LINE = 250;
		final int BYTES_PER_CHAR = 2;

		RandomAccessFile randomAccessFile = null;
		StringBuffer buffer = new StringBuffer(linesToReturn * AVERAGE_CHARS_PER_LINE);
		int lineTotal = 0;
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
			long byteTotal = randomAccessFile.length();
			long byteEstimateToRead = linesToReturn * AVERAGE_CHARS_PER_LINE * BYTES_PER_CHAR;

			long offset = byteTotal - byteEstimateToRead;
			if (offset < 0) {
				offset = 0;
			}

			randomAccessFile.seek(offset);
			// log.debug("SKIP IS ::" + offset);

			String line = null;
			String lineUTF8 = null;
			while ((line = randomAccessFile.readLine()) != null) {
				lineUTF8 = new String(line.getBytes("ISO8859_1"), "UTF-8");
				lineTotal++;
				buffer.append(lineUTF8).append("/n");
			}
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException ex) {
				}
			}
		}

		String[] resultLines = new String[linesToReturn];
		BufferedReader in = null;
		try {
			in = new BufferedReader(new StringReader(buffer.toString()));

			int start = lineTotal /* + 2 */- linesToReturn; // Ex : 55 - 10 = 45
			// ~ offset
			if (start < 0)
				start = 0; // not start line
			for (int i = 0; i < start; i++) {
				in.readLine(); // loop until the offset. Ex: loop 0, 1 ~~ 2
				// lines
			}

			int i = 0;
			String line = null;
			while ((line = in.readLine()) != null) {
				resultLines[i] = line;
				i++;
			}
		} catch (IOException ie) {
			throw ie;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
				}
			}
		}
		return resultLines;
	}

	/**
	 * 单个文件拷贝。
	 * 
	 * @param srcFilename
	 * @param destFilename
	 * @param overwrite
	 * @throws java.io.IOException
	 */
	public static void copyFile(String srcFilename, String destFilename, boolean overwrite) throws IOException {

		File srcFile = new File(srcFilename);
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
		}

		File destFile = new File(destFilename);

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

	/**
	 * 单个文件拷贝。
	 * 
	 * @param srcFile
	 * @param destFile
	 * @param overwrite
	 *            是否覆盖目的文件
	 * @throws java.io.IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean overwrite) throws IOException {
		destFile.getParentFile().mkdirs();
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

	/**
	 * 拷贝文件，从源文件夹拷贝文件到目的文件夹。 <br>
	 * 参数源文件夹和目的文件夹，最后都不要带文件路径符号，例如：c:/aa正确，c:/aa/错误。
	 * 
	 * @param srcDirName
	 *            源文件夹名称 ,例如：c:/test/aa 或者c://test//aa
	 * @param destDirName
	 *            目的文件夹名称,例如：c:/test/aa 或者c://test//aa
	 * @param overwrite
	 *            是否覆盖目的文件夹下面的文件。
	 * @throws java.io.IOException
	 */
	public static void copyFiles(String srcDirName, String destDirName, boolean overwrite) throws IOException {
		File srcDir = new File(srcDirName);// 声明源文件夹
		// 首先判断源文件夹是否存在
		if (!srcDir.exists()) {
			throw new FileNotFoundException("Cannot find the source directory: " + srcDir.getAbsolutePath());
		}

		File destDir = new File(destDirName);
		if (overwrite == false) {
			if (destDir.exists()) {
				// do nothing
			} else {
				if (destDir.mkdirs() == false) {
					throw new IOException("Cannot create the destination directories = " + destDir);
				}
			}
		} else {
			// 覆盖存在的目的文件夹
			if (destDir.exists()) {
				// do nothing
			} else {
				// create a new directory
				if (destDir.mkdirs() == false) {
					throw new IOException("Cannot create the destination directories = " + destDir);
				}
			}
		}

		// 循环查找源文件夹目录下面的文件（屏蔽子文件夹），然后将其拷贝到指定的目的文件夹下面。
		File[] srcFiles = srcDir.listFiles();
		if (srcFiles == null || srcFiles.length < 1) {
			// throw new IOException ("Cannot find any file from source
			// directory!!!");
			return;// do nothing
		}

		// 开始复制文件
		int SRCLEN = srcFiles.length;

		for (int i = 0; i < SRCLEN; i++) {
			// File tempSrcFile = srcFiles[i];

			File destFile = new File(destDirName + File.separator + srcFiles[i].getName());
			// 注意构造文件对象时候，文件名字符串中不能包含文件路径分隔符";".
			// log.debug(destFile);
			if (srcFiles[i].isFile()) {
				copyFile(srcFiles[i], destFile, overwrite);
			} else {
				// 在这里进行递归调用，就可以实现子文件夹的拷贝
				copyFiles(srcFiles[i].getAbsolutePath(), destDirName + File.separator + srcFiles[i].getName(), overwrite);
			}
		}
	}

	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {

	}

	public static void fileOutput(String filename, String charset, String result) throws IOException {
		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(fos, charset);
		out.write(result);
		if (out != null) {
			out.close();
		}
		if (fos != null) {
			fos.close();
		}
	}

	public static String fileInput(String srcPath, String charset) throws IOException {
		File file = new File(srcPath);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(stream, charset);
		BufferedReader br = new BufferedReader(isr);
		String temp = null;
		StringBuffer sb = new StringBuffer();
		while ((temp = br.readLine()) != null) {
			sb.append(temp).append("\r\n");
		}
		br.close();
		isr.close();
		stream.close();
		return sb.toString();
	}

	/**
	 * 生成zip包
	 * 
	 * @param zipPath
	 *            zip输出目录 E:\\szhzipant.zip
	 * @param srcPath
	 *            打包目录
	 * 
	 */
	public static void zipFiles(String zipPath, String srcPath) {
		File srcdir = new File(srcPath);
		if (!srcdir.exists())
			throw new RuntimeException(srcPath + "不存在！");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(new File(zipPath));
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		// fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹
		// eg:zip.setIncludes("*.java");
		// fileSet.setExcludes(...); 排除哪些文件或文件夹
		zip.addFileset(fileSet);

		zip.execute();
	}
	public static String fileInput2(String srcPath, String charset)
			throws IOException {
		File file = new File(srcPath);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(stream, charset);
		BufferedReader br = new BufferedReader(isr);
		String temp = null;
		StringBuffer sb = new StringBuffer();
		while ((temp = br.readLine()) != null) {
			sb.append(temp).append("@@");
		}
		br.close();
		isr.close();
		stream.close();
		return sb.toString();
	}
	
	/**
	 * 文件夹大小(MB)
	 * 
	 * @param file
	 * @return
	 */
	public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
//            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

}
