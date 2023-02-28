package powershell;

import org.json.JSONArray;
import org.json.JSONObject;
import school.License;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PS {


    private static PowerConsole power_console = new PowerConsole(new String[]{"powershell.exe", "-NoExit", "-Command", "-"}, Thread.currentThread());

    PS(){
        try {
            if(!connect()) {
                System.out.println("Autenticazione fallita");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PowerConsole getConsole(){
        return power_console;
    }

    public static boolean connectMFA() throws IOException {
        String result = "";
        result += power_console.execute("Connect-MicrosoftTeams");
        result += power_console.execute("Connect-MsolService");
        result += power_console.execute("Connect-AzureAD");


        if(result.toLowerCase().contains("error")){
            System.out.println("Autenticazione fallita");
            return false;
        }
        return true;
    }

    public static boolean connect() throws IOException{
        return connect("","");
    }

    public static boolean connect(String email, String password) throws IOException {
        String result = "";
        if(email.length()>0) {
            power_console.execute("$User = \"" + email + "\"\n" +
                    "$PWord = ConvertTo-SecureString -String \"" + password + "\" -AsPlainText -Force\n" +
                    "$cred = New-Object -TypeName System.Management.Automation.PSCredential -ArgumentList $User, $PWord", true);

        } else{
            power_console.execute("$cred=Get-Credential");
        }
        result += power_console.execute("Connect-MicrosoftTeams -Credential $cred");
        result += power_console.execute("Connect-MsolService -Credential $cred");
        result += power_console.execute("Connect-AzureAD -Credential $cred");


        if(result.toLowerCase().contains("error")){
            System.out.println("Autenticazione fallita");
            return false;
        }
        return true;
    }

    public static List<String> getDomains() throws IOException {
        try {
            String response = getConsole().execute("Get-AzureADDomain | ConvertTo-Json");// | Format-List ");

            if (response.trim().length() < 3) {
                return new ArrayList<>();
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";

            JSONArray json_array = new JSONObject(response).getJSONArray("items");
            ArrayList<String> list = new ArrayList<>();

            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_domain = json_array.getJSONObject(i);
                String d = json_domain.isNull("Name") ? "" : (String) json_domain.get("Name");
                list.add(d);
            }

            return list;
        } catch (Exception e){
            throw new IOException(e.getMessage());
        }

    }

    public static List<License> getLicences() throws IOException {
        try {
            String response = getConsole().execute("Get-MsolAccountSku | ConvertTo-Json");// | Format-List ");

            if (response.trim().length() < 3) {
                return new ArrayList<>();
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";

            JSONArray json_array = new JSONObject(response).getJSONArray("items");
            ArrayList<License> list = new ArrayList<>();

            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_domain = json_array.getJSONObject(i);

                String nome = json_domain.isNull("AccountSkuId") ? "" : (String) json_domain.get("AccountSkuId");
                int totali = json_domain.isNull("ActiveUnits") ? 0 : (Integer) json_domain.get("ActiveUnits");
                int usati = json_domain.isNull("ConsumedUnits") ? 0 : (Integer) json_domain.get("ConsumedUnits");
                list.add(new License(nome,totali,usati));
            }

            return list;
        } catch (Exception e){
            throw new IOException(e.getMessage());
        }

    }

}




