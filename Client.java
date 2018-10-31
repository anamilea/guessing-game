package Client;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Client
{
	
    public static void main(String[] args) throws IOException 
    {
        try 
        {
            Socket clientSocket = new Socket("localhost", 4444); 		
            System.out.println("Conexiune stabilita.");	   		 //anuntam userul ca a fost stabilita conexiunea
            Scanner scktIn = new Scanner(clientSocket.getInputStream()); //inputul de la socket
            
            PrintWriter scktOut = new PrintWriter(clientSocket.getOutputStream(),true); //outputul socketului
            Scanner kbdIn = new Scanner(System.in);
            System.out.println("Bine ai venit!");
            System.out.println("Care este numele tau?");
            String username = kbdIn.nextLine();		 //luam usernameul din inputul userului
            
            String srvRsp = "";
            System.out.println("Buna, " + username + "! Ma gandesc la un numar intre 1000 si 9999. Poti sa il ghicesti?");
            System.out.println("Scrie 'quit' pentru a iesi.");
            
                   
            while (true) 
            {
                String userIn = kbdIn.nextLine(); 		//inputul de la tastatura
                scktOut.println(userIn); 					//trimitem inputul catre socket (skOut)
                srvRsp = scktIn.nextLine(); 				//luam raspunsul socketului (skIn)
                System.out.println(srvRsp); 			//afisam raspunul 
                
                if (srvRsp.equals("Multumim ca ai jucat."))
                    break; //daca utilizatorul a ales sa incheie jocul, afisam mesajul primit de la server 				
            }
            System.out.println("Multumim ca ai jucat.");
            //inchidem toate conexiunile
            scktOut.close();
            kbdIn.close();
            scktIn.close();
            clientSocket.close();
        } 
        catch (ConnectException e) //cand nu exista conexiunea la server
        {
            System.err.println("Conexiune esuata. Verifica serverul si conexiunea la el.");
            System.exit(1);
        } 
        catch (IOException e) 
        {
            System.err.println("Nu exista Input sau Output pentru conectare");
            System.exit(1);
        }
    }
}