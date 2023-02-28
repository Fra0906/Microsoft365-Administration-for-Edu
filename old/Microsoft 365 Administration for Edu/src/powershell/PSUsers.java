package powershell;

import com.profesorfalken.jpowershell.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PSUsers extends PS{

    //private PowerShell shell;

    public static void main(String[] args) throws IOException, InterruptedException {
        PSUsers ps = new PSUsers();
        //Thread.sleep(10000);
        //System.out.println("2+2");
        //System.out.println( ps.getConsole().execute("2+2;"));
        //Thread.sleep(10000);

        //System.out.println("getmsol");
        //System.out.println(ps.getConsole().execute("Get-MsolUser"));
        //Thread.sleep(10000);
        Scanner s = new Scanner(System.in);
        while (true){
            System.out.print("> ");
            System.out.println(ps.getConsole().execute(s.nextLine()));
        }
    }


    public void connect(){
        System.out.println("sto connettendo");
        //shell = PowerShell.openSession();
        //Map<String, String> config = new HashMap<String,String>();
        //config.put("maxWait","30000");
        //shell.configuration(config);


        //PowerShellResponse res = shell.executeSingleCommand("Get-Credential");

        //System.out.println(res.getCommandOutput());

    }

    public String getAllUsers(){
        System.out.println("sono entrato");
        //PowerShellResponse response = shell.executeSingleCommand("Get-MsolUser -All");
        System.out.println("sono uscito");
        //return response.getCommandOutput();





        return null;
    }

}
