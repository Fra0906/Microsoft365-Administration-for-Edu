package guipackage;

import javafx.beans.property.SimpleStringProperty;
import school.ClassGroup;
import school.Group;
import school.Student;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ObservableGroupClass extends ObservableGroup {

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    private ClassGroup classGroup;

    final SimpleStringProperty group_classe;
    final SimpleStringProperty group_scuola;


    public ObservableGroupClass(ClassGroup classGroup) {
        super(classGroup);
        this.group_classe = new SimpleStringProperty(classGroup.getClasse());
        this.group_scuola = new SimpleStringProperty(classGroup.getScuola());
        this.classGroup = classGroup;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof ClassGroup){
            ClassGroup g = (ClassGroup) o;
            return this.getScuola().toLowerCase().equals(g.getScuola().toLowerCase()) && this.getClasse().toLowerCase().equals(g.getClasse().toLowerCase());
        }
        ObservableGroupClass that = (ObservableGroupClass) o;
        return this.getScuola().toLowerCase().equals(that.getScuola().toLowerCase()) && this.getClasse().toLowerCase().equals(that.getClasse().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(group_classe, group_scuola);
    }

    public String getClasse() {
        return group_classe.get();
    }

    public String getScuola() {
        return group_scuola.get();
    }

}
