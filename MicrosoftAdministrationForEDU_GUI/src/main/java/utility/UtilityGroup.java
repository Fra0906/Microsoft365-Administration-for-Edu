package utility;

import powershell.PSGroup;
import school.Group;
import school.Student;

import java.util.ArrayList;
import java.util.List;

public class UtilityGroup {


    public static Group findGroup(List<Group> list, String objectId){
        for(Group g:list){
            if(g.getObjectId().equals(objectId)){
                return g;
            }
        }
        return null;
    }

    public static void deleteGroup(List<Group> list, Group g){
        PSGroup.delete_group(g.getObjectId());
        list.remove(g);
    }

    public static boolean GroupExists(List<Group> list, String email){
        for(Group s:list){
            if(s.getEmail_address().equals(email)){
                return true;
            }
        }
        return false;
    }

    public static List<Group> searchGroup(List<Group> groupList, String name)
    {
        List<Group> result_list = new ArrayList<>();
        for (Group g : groupList)
        {
            if (name.length() > 0 && (g.getGroupName()!=null || g.getDescription()!=null ))
                if (!(g.getGroupName().toLowerCase().contains(name.toLowerCase())
                        | g.getDescription().toLowerCase().contains(name.toLowerCase())))
                    continue;
            result_list.add(g);
        }
        return result_list;
    }

}
