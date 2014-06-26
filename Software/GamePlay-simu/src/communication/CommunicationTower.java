package communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import tools.Tools;

public class CommunicationTower {
	private Socket socket;
	private boolean running = false;
	private boolean connected = false;
	private static final int SERVERPORT = 6786;
	private int positionV = 50;
	private String SERVER_IP = "";
	private OutputStream out;
	DataOutputStream dos;

	public CommunicationTower() {

	}

	// / Constructors
	public CommunicationTower(String s) {
		SERVER_IP = s;
	}

	// / Getters / Setters
	public boolean isConnected() {
		return connected;
	}

	public boolean isRunning() {
		return running;
	}

	public void setIp(String ip) {
		SERVER_IP = ip;
	}

	public int[] getIpNumbers() {
		int[] ip = new int[] { 0, 0, 0, 0 };
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP.substring(
					1, SERVER_IP.length()));
			String[] ips = serverAddr.getHostAddress().split("\\.");
			ip[0] = Integer.parseInt(ips[0]);
			ip[1] = Integer.parseInt(ips[1]);
			ip[2] = Integer.parseInt(ips[2]);
			ip[3] = Integer.parseInt(ips[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	public int getIdFromIP() {
		return this.getIpNumbers()[3] - 100;
	}

	// / TCP client
	private void runTcpClient() {
		if (SERVER_IP == "")
			return;
		try {
			// Create socket
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP.substring(
					1, SERVER_IP.length()));
			socket = new Socket(serverAddr, SERVERPORT);

			// Update running flags
			running = true;

			InputStream in = socket.getInputStream();
			out = socket.getOutputStream();

			byte[] buf = new byte[20];

			this.connect();

			while (running == true) {

				int sizeRead = in.read(buf);

				if (sizeRead > 0) {
					if (verifyCRC(buf, sizeRead)) {
						switch (buf[0]) {
						case (byte) 0xCD: { // R�ponse connexion
							connected = true;
							break;
						}
						case (byte) 0xDD: { // R�ponse d�connexion
							connected = false;
							break;
						}
						case (byte) 0x81: { // R�ponse ouverture flux vid�o
							break;
						}
						case (byte) 0x91: { // R�ponse fermeture video flux

							break;
						}
						}
					}
				}
			}
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();

			if (socket.isConnected())
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} catch (IOException e) {
			e.printStackTrace();
			if (socket.isConnected())
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
	}

	private boolean verifyCRC(byte[] data, int length) {
		// return true;
		short crc = (short) Tools.calc_crc16(data, length - 2);
		short crcRead = (short) (data[length - 2] << 8 | data[length - 1]);

		return true; // (crc == crcRead);

	}

	private void sendMessage(byte[] message, int size) {
		char crc = Tools.calc_crc16(message, size);
		byte[] msg = new byte[size + 2];

		System.arraycopy(message, 0, msg, 0, size);
		msg[size] = (byte) (crc >> 8);
		msg[size + 1] = (byte) (crc & 0xFF);

		try {
			dos = new DataOutputStream(out);
			dos.write(msg, 0, size + 2);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Thread t;

	// / Actions called by Tower
	public boolean startCommunication() {
		t = new Thread(new Runnable() {
			public void run() {
				try {
					runTcpClient();
				} catch (Exception e) {
					connected = false;
					running = false;
				}
			}
		});
		t.start();
		return running;
	}

	public void stopCommunication() {

		if (t != null)
			if (t.isAlive())
				this.running = false;
	}

	public boolean connect() {
		if (running == false)
			return false;

		byte[] answer = { (byte) 0xCC };
		sendMessage(answer, 1);
		return true;
	}

	public boolean disconnect() {
		if (running == false)
			return false;

		byte[] answer = { (byte) 0xDC };
		sendMessage(answer, 1);
		return true;
	}

	public void openVideoFlux() {
		if (running == false || connected == false)
			return;

		byte[] answer = { (byte) 0x80 };
		sendMessage(answer, 1);
	}

	public void closeVideoFlux() {
		if (running == false || connected == false)
			return;

		byte[] answer = { (byte) 0x90 };
		sendMessage(answer, 1);
	}

	public void moveH(int degree) {
		if (degree == 0 || connected == false)
			return;

		byte[] msg = { (byte) 0xA0, (byte) 0x00,
				(byte) ((degree > 0) ? 0x00 : 0x40), (byte) (Math.abs(degree)) };
		sendMessage(msg, 4);

	}

	public void moveVOffset(int offset) {
		if (offset == 0)
			return;
		moveV(offset + positionV);
	}

	public void moveV(int degree) {
		if (connected == false)
			return;
		if (degree > 60)
			degree = 60;
		else if (degree < 40)
			degree = 40;

		byte[] msg = { (byte) 0xA0, (byte) 0X80,
				(byte) ((degree > 0) ? 0x00 : 0x40), (byte) (Math.abs(degree)) };
		sendMessage(msg, 4);
		positionV = degree;
	}

}
