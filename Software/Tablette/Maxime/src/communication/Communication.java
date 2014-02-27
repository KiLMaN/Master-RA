package communication;

public class Communication{
	int tcpRecievePort = 8082;
	int udpRecievePort = 10000;
	TcpServer _tcpServer;
	UdpServer _udpServer;
	
	Thread _tcpServerThread;
	Thread _udpServerThread;
	
	public Communication(){
		
	}

	public void run() {
		_tcpServer = new TcpServer(tcpRecievePort);
		_udpServer = new UdpServer(udpRecievePort);
		
		_tcpServerThread = new Thread(_tcpServer);
		_tcpServerThread.start();
		
		//_udpServerThread = new Thread(_udpServer);
		//_udpServerThread.start();
	}
	
	public void stop(){
		
		
	}

}
