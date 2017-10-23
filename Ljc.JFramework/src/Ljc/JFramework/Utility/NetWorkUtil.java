package Ljc.JFramework.Utility;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class NetWorkUtil {
	public static List<InetAddress> getIpV4Address() throws SocketException {
		// Before we connect somewhere, we cannot be sure about what we'd be bound to;
		// however,
		// we only connect when the message where client ID is, is long constructed.
		// Thus,
		// just use whichever IP address we can find.

		List<InetAddress> list = new LinkedList<InetAddress>();

		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface current = interfaces.nextElement();
			if (!current.isUp() || current.isLoopback() || current.isVirtual())
				continue;
			Enumeration<InetAddress> addresses = current.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr.isLoopbackAddress())
					continue;

				if (addr instanceof Inet4Address)
					list.add(addr);
			}
		}

		return list;
	}
}
