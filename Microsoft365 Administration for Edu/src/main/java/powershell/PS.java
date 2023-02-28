package powershell;

import java.io.IOException;

public class PS {


    private static powershell.PowerConsole power_console = new powershell.PowerConsole(new String[]{"powershell.exe", "-NoExit", "-Command", "-"}, Thread.currentThread());

    PS(){
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static powershell.PowerConsole getConsole(){
        return power_console;
    }

    public static void connect() throws IOException {
        power_console.execute("$cred=Get-Credential");
        power_console.execute("Connect-MsolService -Credential $cred");
        power_console.execute("Connect-AzureAD -Credential $cred");
        power_console.execute("Connect-MicrosoftTeams -Credential $cred");
    }

}




