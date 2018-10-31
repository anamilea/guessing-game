package Server;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random;
import java.util.Scanner;

public class ServerThread extends Thread
{
	private Socket socket = null;
	private int username;
	public  static List<ServerThread> Servers = Collections.<ServerThread>synchronizedList(new ArrayList<ServerThread>());
	
	public ServerThread(Socket s, int u) 
	{
		socket = s;
		username =u;
	}
	
	public int checkCentrate(int input_number, int random) 	
	{ //contorul ce numara cate cifre centrate exista in nr introdus de user
		for(int i=0;i<=3;i++)
		int nr=0;
		{
			if( input_number%10 == random%10)
			nr++;
		 input_number=  input_number/10; 
		 random = random/10;
		}
		return nr;
	}
	public int checkNecentrate(int input_number, int random)	//primim ca parametri de intrare numarul introdus de user si cel generat de server
	{
		int nr=0;	//contorul ce numara cate cifre necentrate exista in nr introdus de user
		int first = input_number;
		int aux = random;
		for(int i=0; i<=3; i++)
		{
			first = input_number % 10;
			for(int j=0; j<=3; j++)
			{
				if(first == random%10 && i != j) //daca cifra se regaseste in celalalt numar, dar nu pe aceeasi pozitie
					nr++;
			random = random/10;
			}
			random = aux;
			input_number=  input_number/10;
		}
		return nr;
	}
	
	public void run()
	{
		Servers.add(new ServerThread(socket,username)); //adaugam noua conexiune in lista
		System.err.println("Clientul" + username+" a fost conectat cu succes.");	//Afisam pe ecranul serverului
		String inputLine;			//Inputul de la user (String)
		int nrInput;			//Inputul de la user, sub forma de numar
		int count = 0;			//Contor pentru numarul de incercari ale userului
		int random = (int)(Math.random()*9000)+1000; //Generam un numar random 1000 si 9999
		
		try
		{
			Scanner in = new Scanner(socket.getInputStream());			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			while (true)
			{
				inputLine = in.nextLine();					
				nrInput = Integer.parseInt(inputLine);	// Transformam inputul de la user in numar
				
				if (nrInput == random)					//Daca numarul introdus este egal cu cel generat
				{
					out.println("Felicitari! Ai ghicit numarul!"	//Afisam pt user
				+ " Ai avut " + count + " incercari. " +
						"Joci din nou? d/n");
					for(ServerThread i : Servers)
					{
						if(i.username != this.username)
							{final PrintWriter t = new PrintWriter(i.socket.getOutputStream(), true);
							t.println("Userul cu numarul "+ this.username+ " a ghicit numarul. Multumim ca ai jucat");
							break;
							}
					}
					if (inputLine.equalsIgnoreCase("d"))		//daca userul introduce litera "d"
					{
						//continuam jocul
					}
					else if (inputLine.equalsIgnoreCase("n"))	//daca userul introduce litera "n"
					{
						out.println("Multumim ca ai jucat. Te mai asteptam!");
						break;		//iesim din while
					}
				}
				else if (nrInput < random)		//Daca numarul introdus e mai mic decat cel generat
				{
					int nrC = checkCentrate(nrInput, random); 			//calculam numarul de cifre centrate
					int nrN = checkNecentrate(nrInput, random); 		//calculam numarul de cifre necentrate
					out.println(nrInput + "? Numarul e prea mic!" + " Cifre centrate: " + nrC + " si cifre necentrate: " + nrN );
					count ++; 			//incrementam numarul de incercari									
				}
				else if (nrInput > random)		//Daca numarul introdus e mai mare decat cel generat
				{
					int nrC = checkCentrate(nrInput, random); 		// calculam numarul de cifre centrate
					int nrN = checkNecentrate(nrInput, random); 	// calculam numarul de cifre necentrate
					out.println(nrInput + "? Numarul e prea mare!" + " Cifre centrate: " + nrC + " si cifre necentrate" + nrN );
					count ++;			//incrementam numarul de incercari			
				}
				else if (inputLine.equalsIgnoreCase("quit"))	// daca userul scrie "quit", indiferent de litere(mari/mici)
				{
					out.println("Multumim ca ai jucat. Te mai asteptam!");
					break;			//iesim din while
				}
				
			}
			
			out.close();			
			in.close();
			socket.close(); 	//inchidem toate conexiunile
		}
		catch (Exception e)			//Daca userul s a deconectat de la server
		{
			System.err.println("Conexiunea a fost intrerupta.");	//Afisam mesajul de eroare
		}
	}
}