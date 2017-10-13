package Ljc.JFramework.SocketApplication;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import Ljc.JFramework.CoreException;
import Ljc.JFramework.EntityBufCore;
import Ljc.JFramework.Utility.BitConverter;
import Ljc.JFramework.Utility.HashEncryptUtil;

public class SocketApplicationComm {
	public static int SendMessage(Socket s, Message message) throws CoreException {
		try {
			if (s == null) {
				return 0;
			}

			byte[] data = null;
			int bufferindex = -1;
			long size = 0;
			EntityBufCore.Serialize(message);

			byte[] dataLen = BitConverter.GetBytes(data.length - 4);

			for (int i = 0; i < 4; i++) {
				data[i] = dataLen[i];
			}

			long crc32 = HashEncryptUtil.GetCRC32(data, 8);
			byte[] crc32bytes = BitConverter.GetBytes(crc32);
			for (int i = 4; i < 8; i++) {
				data[i] = crc32bytes[i - 4];
			}

			synchronized (s) {
				DataOutputStream dos = null;
				try {
					OutputStream os = s.getOutputStream();
					dos = new DataOutputStream(os);
					dos.write(data);

					dos.flush();

				} finally {
					if (dos != null) {
						dos.close();
					}
				}

				return data.length;
			}
		} catch (Exception ex) {
			CoreException cex = new CoreException(ex.getMessage(), ex);
			cex.Data.put("TransactionID", message.getMessageHeader().getTransactionID());
			throw cex;
		}
	}
}
