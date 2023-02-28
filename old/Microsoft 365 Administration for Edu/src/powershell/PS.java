package powershell;

import java.io.*;

public class PS {

    //private static PowerConsole power_console = new PowerConsole(new String[]{"powershell.exe", "-NoExit", "-Command", "-"}, Thread.currentThread());
    private static PowerConsole power_console = new PowerConsole(new String[]{"powershell.exe", "-NoExit", "-Command", "-"}, Thread.currentThread());
    PS(){
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected PowerConsole getConsole(){
        return power_console;
    }

    private static void connect() throws IOException {
        power_console.execute("Connect-MsolService");
    }

}




