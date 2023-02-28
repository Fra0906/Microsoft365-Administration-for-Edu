import powershell.PSUsers;

import java.io.*;
import java.util.Scanner;



public class MainTest {

    public static void main(String[] args) throws IOException {
        System.out.println("ciao");

        Scanner in = new Scanner(System.in);
        String command = "powershell.exe";
        String output;

        Process psprocess = Runtime.getRuntime().exec(command);

        psprocess.getOutputStream().write("cls".getBytes());
        psprocess.getOutputStream().close();
        String line;
        System.out.println("Standard Output:");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(
                psprocess.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
        }
        stdout.close();
        System.out.println("Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(
                psprocess.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            System.out.println(line);
        }
        stderr.close();
        System.out.println("Done");




        /*PSUsers powershell_user = new PSUsers();
        powershell_user.connect();
        Scanner scanner = new Scanner(System.in);

        if(scanner.nextLine().equals("users"))
            System.out.println(powershell_user.getAllUsers());*/
    }
}
