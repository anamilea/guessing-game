package Server;
import java.net.ServerSocket;

public class Server
{
	public static void main(String[] args) throws Exception
	{
		int index = 100;
		boolean ok = true;
		ServerSocket srvsckt = new ServerSocket(4444); 
		
		System.out.println("Conexiune la server stabilita. Bine ai venit! \n");
		while (ok){
			new ServerThread(srvsckt.accept(), index).start(); //cream o noua instanta a ServerThread la fiecare noua conexiune
			index++;
		}
		srvsckt.close();
	}
}