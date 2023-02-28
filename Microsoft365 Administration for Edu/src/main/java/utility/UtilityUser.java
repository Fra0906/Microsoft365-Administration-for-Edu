package utility;

import powershell.PS;
import powershell.PSUsers;
import powershell.PowerConsole;
import powershell.PS.*;
import school.Student;
import school.Teacher;
import school.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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

    public static void main(String[] args) throws IOException {

        PSUsers.BlockClassAccounts("1B");


    }
}
