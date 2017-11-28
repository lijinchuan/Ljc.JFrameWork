package Ljc.JFramework.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("压缩中...");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // 输出流关闭
		System.out.println("压缩完成");
	}

	private static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // 方法重载
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
			in.close(); // 输入流关闭
		}
	}

	public static void unZip(String srcPath, String dest) throws IOException {
		File file = new File(srcPath);

		if (!file.exists()) {

			throw new RuntimeException(srcPath + "所指文件不存在");

		}

		ZipFile zf = new ZipFile(file);
		Enumeration entries = zf.entries();

		ZipEntry entry = null;

		while (entries.hasMoreElements()) {

			entry = (ZipEntry) entries.nextElement();

			System.out.println("解压" + entry.getName());

			if (entry.isDirectory()) {

				String dirPath = dest + File.separator + entry.getName();

				File dir = new File(dirPath);

				dir.mkdirs();

			} else {

				// 表示文件

				File f = new File(dest + File.separator + entry.getName());

				if (!f.exists()) {

					String dirs = f.getParentFile().getAbsolutePath();

					File parentDir = new File(dirs);

					parentDir.mkdirs();

				}

				f.createNewFile();

				// 将压缩文件内容写入到这个文件中

				InputStream is = zf.getInputStream(entry);

				FileOutputStream fos = new FileOutputStream(f);

				int count;

				byte[] buf = new byte[8192];

				while ((count = is.read(buf)) != -1) {

					fos.write(buf, 0, count);

				}

				is.close();

				fos.close();

			}

		}
	}
}
