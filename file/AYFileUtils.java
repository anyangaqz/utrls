package com.sunsheen.cns.edt.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunsheen.jfids.commons.fileupload.FileItem;
import com.sunsheen.jfids.commons.fileupload.FileUploadException;
import com.sunsheen.jfids.commons.fileupload.disk.DiskFileItemFactory;
import com.sunsheen.jfids.commons.fileupload.servlet.ServletFileUpload;
import com.sunsheen.jfids.system.config.Configs;

/**
 * 文件操作公共的方法
 * 
 * @author AnYang
 *
 */
public class AYFileUtils {

	public static final String REALPATH = Configs.get("File.fileUpload");// 读取文件上传路径;

	/**
	 * 读取文件夹下所有文件名称
	 */
	public static List<String> getAllFileName(String path) {
		List<String> dataList = new ArrayList<>();
		File f = new File(path);
		if (!f.exists()) {
			System.out.println(path + " not exists");
			return null;
		}

		File fa[] = f.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			if (fs.isDirectory()) {
				dataList.add(fs.getName());
			} else {
				dataList.add(fs.getName());
			}
		}
		return dataList;
	}

	/**
	 * @param sourceFilePath
	 *            待压缩文件（夹）路径
	 * @param targetPath
	 *            压缩后，压缩文件所在目录
	 * @param zipFileName
	 *            压缩后的文件名称{.zip结尾}
	 * @return
	 * @Description: 创建zip文件
	 */
	public static boolean createZipFile(String sourceFilePath, String targetPath, String zipFileName) {

		boolean flag = false;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		// 要压缩的文件资源
		File sourceFile = new File(sourceFilePath);
		// zip文件存放路径
		String zipPath = "";

		if (null != targetPath && !"".equals(targetPath)) {
			zipPath = targetPath + File.separator + zipFileName;
		} else {
			zipPath = new File(sourceFilePath).getParent() + File.separator + zipFileName;
		}

		if (sourceFile.exists() == false) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
			return flag;
		}

		try {
			File zipFile = new File(zipPath);
			if (zipFile.exists()) {
				System.out.println(zipPath + "目录下存在名字为:" + zipFileName + ".zip" + "打包文件.");
			} else {
				File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
				} else {
					fos = new FileOutputStream(zipPath);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					// 生成压缩文件
					writeZip(sourceFile, "", zos);
					flag = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (null != zos) {
					zos.close();
				}
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @param file
	 * @param parentPath
	 * @param zos
	 * @Description:
	 */
	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if (file.exists()) {
			// 处理文件夹
			if (file.isDirectory()) {
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				if (files.length != 0) {
					for (File f : files) {
						// 递归调用
						writeZip(f, parentPath, zos);
					}
				} else {
					// 空目录则创建当前目录的ZipEntry
					try {
						zos.putNextEntry(new ZipEntry(parentPath));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.err.println("创建ZIP文件失败");
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("创建ZIP文件失败");
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.err.println("创建ZIP文件失败");
					}
				}
			}
		}
	}

	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * 去掉指定字符串的开头的指定字符
	 * 
	 * @param stream
	 *            原始字符串
	 * @param trim
	 *            要删除的字符串
	 * @return
	 */
	public static String stringStartTrim(String stream, String trim) {
		// null或者空字符串的时候不处理
		if (stream == null || stream.length() == 0 || trim == null || trim.length() == 0) {
			return stream;
		}
		// 要删除的字符串结束位置
		int end;
		// 正规表达式
		String regPattern = "[" + trim + "]*+";
		Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);
		// 去掉原始字符串开头位置的指定字符
		Matcher matcher = pattern.matcher(stream);
		if (matcher.lookingAt()) {
			end = matcher.end();
			stream = stream.substring(end);
		}
		// 返回处理后的字符串
		return stream;
	}

	/**
	 * 迭代删除文件夹
	 * 
	 * @param dirPath
	 *            文件夹路径
	 */
	public static boolean deleteDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();
			} else {
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
		return true;
	}

	/**
	 * 创建多级目录
	 * 
	 * @param path
	 *            目录
	 * 
	 * @throws IOException
	 */
	public static Boolean mkdir(String path) throws IOException {
		File dir = new File(path);
		if (dir.exists()) {
			System.out.println("创建目录" + path + "失败，目标目录已经存在");
			return false;
		}
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + path + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + path + "失败！");
			return false;
		}
	}

	// 上传文件 获取文件 和 其他参数
	public static Map<String, Object> uploadFiles(HttpServletRequest request, HttpServletResponse response) throws FileUploadException, Exception {

		Map<String, Object> retMap = new HashMap<>();

		response.setContentType("text/html;charset=UTF-8");
		// 创建一个解析器工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置工厂的内存缓冲区大小，默认是10K
		factory.setSizeThreshold(1024 * 1024 * 64);
		// 文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置编码格式
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> itemList = upload.parseRequest(request);

		List<Map<String, Object>> fileList = new ArrayList<>();

		for (FileItem item : itemList) {
			// 判断输入的类型是 普通输入项 还是文件
			if (item.isFormField()) {
				// 普通输入项 不做处理
				String idName = item.getFieldName();
				String value = new String(item.getString().getBytes("ISO8859_1"), "utf-8");
				retMap.put(idName, value);

			} else {

				Map<String, Object> fileMap = new HashMap<>();

				// 上传的是文件，获得文件上传字段中的文件名
				// 注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名。
				String fileName = item.getName();

				// 返回文件的源名称
				fileMap.put("filename", fileName);

				// 获取文件后缀名
				String[] strArray = fileName.split("\\.");
				int suffixIndex = strArray.length - 1;
				fileName = strArray[suffixIndex];

				// 生成最后的文件名
				String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
				fileName = uuid + "." + fileName;

				String url = REALPATH + "/" + fileName;

				File file = new File(REALPATH);
				if (!file.exists() && !file.isDirectory()) {
					// 生成目录
					file.mkdirs();
				}

				// 保存文件
				file = new File(url);
				item.write(file);

				fileMap.put("file", file);
				fileMap.put("servername", fileName);

				fileList.add(fileMap);
			}
		}
		retMap.put("fileList", fileList);
		return retMap;
	}

	/**
	 * 移动文件
	 * 
	 * @param src
	 *            源文件路径名称
	 * @param dest
	 *            目标文件路径名称
	 * @return
	 */
	public static Boolean moveFile(String src, String dest) {
		File startFile = new File(src);
		File endFile = new File(dest);
		if (startFile.renameTo(endFile)) {
			System.out.println("文件移动成功！目标路径：{" + endFile.getAbsolutePath() + "}");
			return true;
		} else {
			System.out.println("文件移动失败！起始路径：{" + startFile.getAbsolutePath() + "}");
			return false;
		}
	}

	/**
	 * 以流的方法下载文件
	 * 
	 * @param path : 文件路径
	 * @param fileName : 文件名称(包括后缀名)
	 * @param response : 响应对象 
	 * @return
	 * @throws IOException
	 */
	public static HttpServletResponse downloadFile(String path, String fileName, HttpServletResponse response) throws IOException {
		// path是指欲下载的文件的路径。
		File file = new File(path);
		// 取得文件名。
//		String name = file.getName();
		// 取得文件的后缀名。
//		String ext = name.substring(name.lastIndexOf(".") + 1).toUpperCase();
		// 以流的形式下载文件。
		InputStream fis = new BufferedInputStream(new FileInputStream(path));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		// 清空response
		response.reset();
		// 设置response的Header
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		response.addHeader("Content-Length", "" + file.length());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
		return response;
	}
}
