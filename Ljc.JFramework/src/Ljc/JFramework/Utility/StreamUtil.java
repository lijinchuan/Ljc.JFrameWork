package Ljc.JFramework.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class StreamUtil {

	final static int MAXInitLen = 1024 * 1;

	public static byte[] ReadStream(InputStream in, int streamlen) throws Exception {
		if (streamlen < 0) {
			List<ByteBuffer> list = new LinkedList<ByteBuffer>();

			int totallen = 0;
			while (true) {
				java.nio.ByteBuffer bb = ByteBuffer.allocate(MAXInitLen);
				int offset = 0;
				int readlen = 0;
				while (true) {
					readlen = in.read(bb.array(), offset, MAXInitLen - offset);
					if (readlen == -1) {
						break;
					}
					offset += readlen;
					if (offset == MAXInitLen) {
						break;
					}
				}
				if (offset > 0) {
					totallen += offset;
					bb.position(offset);
					list.add(bb);
				} else {
					break;
				}
			}

			byte[] bytes = ByteBuffer.allocate(totallen).array();
			int idx = 0;
			for (ByteBuffer it : list) {
				int len = it.position();

				for (int i = 0; i < len; i++) {
					bytes[idx++] = it.array()[i];
				}

			}

			return bytes;
		} else {
			java.nio.ByteBuffer bb = ByteBuffer.allocate(streamlen);
			int offset = 0;
			int readlen = 0;
			while (true) {
				readlen = in.read(bb.array(), offset, streamlen - offset);
				if (readlen == -1) {
					if (offset != streamlen) {
						throw new IOException("¶ÁÈ¡Á÷Ê§°Ü");
					}
					break;
				}
				offset += readlen;
			}
			bb.position(0);
			return bb.array();
		}

	}
}
