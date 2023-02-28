package utility;

import guipackage.App;
import guipackage.ObservableStudent;
import guipackage.ObservableUser;
import powershell.PSUsers;
import school.Student;
import school.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityUser {


    public static void exportCSV(List<User> list, String path) throws IOException {
        String SEP = ",";

        if(list.isEmpty())
            return;

        Set<String> heads = list.get(0).getFields().keySet();
        String s = "";
        for(String head : heads){
            s += head+SEP;
        }
        s+="\n";

        for(User user : list){
            for(String head : heads){
                s += user.getFields().get(head).toString().replace(SEP, "-")+SEP;
            }
            s+="\n";
        }

        Files.writeString(Path.of(path), s, StandardCharsets.UTF_8);
    }

    public static boolean UserExists(List<User> list,String user_principal_name){
        for(User s:list){
            if(s.getUserPrincipalName().equals(user_principal_name)){
                return true;
            }
        }
        return false;
    }

    public static boolean UserExistsString(List<String> list,String user_principal_name){
        for(String s:list){
            //System.out.println(s);
            if(s.equals(user_principal_name)){
                return true;
            }
        }
        return false;
    }

    public static User findUser(String objectid, List<? extends User>... lists){
        for(List<? extends User> list : lists)
        {
            for(User s:list){
                if(s.getObjectId().equals(objectid)){
                    return s;
                }
            }
        }

        return null;
    }


    public static void changeUserPassword(String user_principal_name, String password){
        PSUsers.changeUserPassword(user_principal_name,password);
    }


    public static void main(String[] args) throws IOException {

        PSUsers.BlockClassAccounts("1B");

    }

    public static boolean checkEmailSyntax(String email){
        for(String domain : App.getDomainList()) {
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@" + domain, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(email);
            if(m.find()){
                return true;
            }
            //ciao
        }
        return false;
    }
}
