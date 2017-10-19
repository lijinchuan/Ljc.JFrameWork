package Ljc.JFramework.SocketApplication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import Ljc.JFramework.CoreException;
import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.HashEncryptUtil;

public class SocketApplicationComm {
	private static AtomicLong seqNum;
	private static String _seqperfix = UUID.randomUUID().toString().replace("-", "");

	public static int SendMessage(Socket s, Message message) throws CoreException {
		try {
			if (s == null) {
				return 0;
			}

			byte[] data = null;
			int bufferindex = -1;
			long size = 0;
			data = EntityBufCore.Serialize(message, true);

			byte[] dataLen = BitConverter.GetBytes(data.length + 4);

			byte[] data2 = new byte[data.length + 8];
			for (int i = 0; i < 4; i++) {
				data2[i] = dataLen[i];
			}

			int crc32 = (int) HashEncryptUtil.GetCRC32(data, 0);
			System.out.println("ะฃั้:" + String.valueOf(crc32));
			byte[] crc32bytes = BitConverter.GetBytes(crc32);
			for (int i = 4; i < 8; i++) {
				data2[i] = crc32bytes[i - 4];
			}

			for (int i = 0; i < data.length; i++) {
				data2[i + 8] = data[i];
			}
			data = data2;

			synchronized (s) {
				DataOutputStream dos = null;
				try {
					OutputStream os = s.getOutputStream();
					dos = new DataOutputStream(os);
					dos.write(data);

					dos.flush();

				} catch (IOException ex) {
					if (dos != null) {
						dos.close();
					}
					s.close();
					throw ex;
				}

				return data.length;
			}
		} catch (Exception ex) {
			CoreException cex = new CoreException(ex.getMessage(), ex);
			cex.Data.put("TransactionID", message.getMessageHeader().getTransactionID());
			throw cex;
		}
	}

	public static String GetSeqNum() {
		long seqNumMiro = seqNum.incrementAndGet();
		return String.format("%s_%d", _seqperfix, seqNumMiro);
	}
}
