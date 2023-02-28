package guipackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import school.Group;
import school.Student;
import school.Teacher;
import school.User;
import utility.UtilityDocenti;
import utility.UtilityStudenti;
import utility.UtilityUser;

import java.util.ArrayList;
import java.util.List;

public class ObservableUtility {

    public static void refreshStudents(){
        buildObservableStudentList(App.getStudentList());
    }

    public static void refreshUsers(){
        buildUserObservableList(App.getStudentList());
        buildUserObservableList(App.getTeacherList());

    }

    public static void buildObservableStudentList(List<Student> listStudent)
    {
        StudentlistController.getStudentsObservableList().clear();
        for(Student student : listStudent){
            //System.out.println(student);
            ObservableStudent os = new ObservableStudent(student);
            StudentlistController.getStudentsObservableList().add(os);
        }
    }

    public static void refreshTeachers(){
        buildObservableTeacherList(App.getTeacherList());
    }

    public static void buildObservableTeacherList(List<Teacher> listTeacher)
    {
        TeacherlistController.getTeachersObservableList().clear();
        for(Teacher teacher : listTeacher){
            //System.out.println(teacher);
            ObservableTeacher ot = new ObservableTeacher(teacher);
            TeacherlistController.getTeachersObservableList().add(ot);
        }
    }

    public static void refreshGroups(){
        buildObservableGroupList(App.getGroupList());
    }

    public static void buildObservableGroupList(List<Group> listgroup)
    {
        GrouplistController.getGroupsObservableList().clear();
        for(Group g : listgroup){
            //System.out.println(g);
            ObservableGroup og = new ObservableGroup(g);
            GrouplistController.getGroupsObservableList().add(og);
        }
    }

    public static List<Student> getStudentListFromObservable(List<Student> all_students, ObservableList<ObservableStudent> obs_list){
        List<Student> list = new ArrayList<>();
        for(ObservableStudent st : obs_list){
            list.add(UtilityStudenti.findStudent(all_students,st.getEmail()));
        }
        return list;
    }

    public static List<Teacher> getTeacherListFromObservable(List<Teacher> all_teachers, ObservableList<ObservableTeacher> obs_list){
        List<Teacher> list = new ArrayList<>();
        for(ObservableTeacher st : obs_list){
            list.add(UtilityDocenti.findTeacher(all_teachers,st.getEmail()));
        }
        return list;
    }

    public static ObservableList<ObservableUser> buildUserObservableList(List<? extends User> userlist)
    { ObservableList<ObservableUser> user_observable_list = FXCollections.observableArrayList();
        for (User u : userlist) {
            ObservableUser os=null;
            if(u instanceof  Student)
            {
                os= new ObservableStudent((Student) u);
            }
            if(u instanceof Teacher)
            {
                os=new ObservableTeacher((Teacher) u);
            }
            user_observable_list.add(os);
        }
        return user_observable_list;
    }



    public static List<User> getUserListFromObservable(List<? extends User> all_user, ObservableList<ObservableUser> obs_list){
        List<User> list = new ArrayList<>();
        for(ObservableUser st : obs_list){
            list.add(UtilityUser.findUser(st.getObjectId(), all_user));
        }
        return list;
    }

}
