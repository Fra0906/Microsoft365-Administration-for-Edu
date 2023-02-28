package school;

import guipackage.App;
import powershell.PSGroup;

import java.util.List;

public class ClassGroup extends Group{


    private String classe;
    private String scuola;

    public ClassGroup(String classe, String scuola){
        super(
                "gruppo classe "+classe +" "+scuola,
                "gruppo_classe_"+classe +"_" +scuola.replace("'","").replace(" ","").replace("°",""),
                "gruppo classe#"+classe +"#"+scuola
        );

        this.classe = classe;
        this.scuola = scuola;

    }

    private ClassGroup() {}

    public static ClassGroup fromExisting(){
        ClassGroup cg = new ClassGroup();
        cg.stopSyncronizing();
        return cg;
    }

    public static ClassGroup fromGroup(Group g){
        ClassGroup cg = new ClassGroup();
        cg.stopSyncronizing();

        cg.setDescription(g.getDescription());
        cg.setGroup_type(g.getGroup_type());
        cg.setGroupName(g.getGroupName());
        cg.setEmail_address(g.getEmail_address());
        cg.setMembers(g.getMembers());
        cg.setObjectId(g.getObjectId());
        cg.setOwners(g.getOwners());

        String[] dati = g.getDescription().split("#");
        if (dati.length > 0) {
            cg.setClasse(dati[1]);
            if(dati.length>1){
                cg.setScuola(dati[2]);
            } else {
                cg.setScuola("");
            }
        } else {
            cg.setClasse("");
            cg.setScuola("");
        }




        cg.startSyncronizing();
        return cg;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
        this.setGroupName("gruppo classe "+classe +" "+scuola);
        this.setDescription("gruppo classe#"+classe +"#"+scuola);
        this.setEmail_address("gruppo_classe_"+classe +"_" +scuola);

    }

    public String getScuola() {
        return scuola;
    }

    public void setScuola(String scuola) {
        this.scuola = scuola;
        this.setGroupName("gruppo classe "+classe +" "+scuola);
        this.setDescription("gruppo classe#"+classe +"#"+scuola);
        this.setEmail_address("gruppo_classe_"+classe +"_" +scuola);
    }

    public static boolean isClassGroup(Group g){
        return g.getGroupName().contains("gruppo classe")
                && g.getDescription().contains("gruppo classe#");
    }
}