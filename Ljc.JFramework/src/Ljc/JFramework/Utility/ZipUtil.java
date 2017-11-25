package Ljc.JFramework.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("ѹ����...");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // ������ر�
		System.out.println("ѹ�����");
	}

	private static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // ��������
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // ����zipѹ�������base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // �ݹ�������ļ���
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // ����zipѹ�������base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // ���ֽ���д�뵱ǰzipĿ¼
			}
			bi.close();
			in.close(); // �������ر�
		}
	}

	public static void unZip(String zipfile, String destdir) throws IOException {
		ZipInputStream Zin = new ZipInputStream(new FileInputStream(zipfile));// ����Դzip·��
		BufferedInputStream Bin = new BufferedInputStream(Zin);
		String Parent = destdir; // ���·�����ļ���Ŀ¼��
		File Fout = null;
		ZipEntry entry;
		while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
			Fout = new File(Parent, entry.getName());
			if (!Fout.exists()) {
				(new File(Fout.getParent())).mkdirs();
			}
			FileOutputStream out = new FileOutputStream(Fout);
			BufferedOutputStream Bout = new BufferedOutputStream(out);
			int b;
			while ((b = Bin.read()) != -1) {
				Bout.write(b);
			}
			Bout.close();
			out.close();
			System.out.println(Fout + "��ѹ�ɹ�");
		}
		Bin.close();
		Zin.close();
	}
}
