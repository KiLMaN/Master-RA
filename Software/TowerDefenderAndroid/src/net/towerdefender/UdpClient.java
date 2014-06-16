package net.towerdefender;

import gameplay.Position;
import gameplay.Tower;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

public class UdpClient implements Runnable {
	private int _recievePort = 6783;
	private Context _context;
	private int TIMEOUT_RECEPTION_REPONSE = 2000; // Time en ms

	public UdpClient(Context mcontext) {
		this._context = mcontext;
	}

	private InetAddress getAdresseBroadcast() throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) this._context
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifiManager.getDhcpInfo();

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public void sendBroadcast() {

	}

	public ArrayList<Tower> getTowers() {
		ArrayList<InetAddress> ipClients = new ArrayList<InetAddress>();
		try {
			ipClients = sendFrame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<InetAddress> it = ipClients.iterator();
		ArrayList<Tower> towers = new ArrayList<Tower>();
		int cpt = 1;
		while (it.hasNext()) {
			InetAddress a = it.next();

			Tower t = new Tower(cpt++, new Position());
			t.setIp(a.toString());
			towers.add(t);
		}

		return towers;
	}

	public ArrayList<InetAddress> sendFrame() throws Exception {
		byte[] i = new byte[3];
		i[0] = (byte) 0xD0;

		DatagramSocket socket = new DatagramSocket(_recievePort);
		socket.setBroadcast(true);
		InetAddress broadcastAdress = InetAddress.getByName("255.255.255.255");
		DatagramPacket packet = new DatagramPacket(i, i.length,
				broadcastAdress, _recievePort);
		socket.send(packet);

		byte[] buf = new byte[30];
		packet = new DatagramPacket(buf, buf.length);
		socket.setSoTimeout(TIMEOUT_RECEPTION_REPONSE);
		socket.receive(packet);

		ArrayList<InetAddress> ipClients = new ArrayList<InetAddress>();
		while (packet.getLength() == 0 || packet.getData()[0] == (byte) 0xD0) {
			socket.receive(packet);

			ipClients.add(socket.getInetAddress());
		}
		if (packet.getLength() > 0)
			System.out.println(packet.getLength());
		socket.close();

		return ipClients;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
