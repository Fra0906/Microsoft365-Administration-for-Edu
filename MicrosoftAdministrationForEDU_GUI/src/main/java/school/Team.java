package school;

import powershell.PSGroup;

public class Team extends Group{

    public Team(String nomeTeam, String description, User owner, String template){
        super();
        stopSyncronizing();
        setGroupName(nomeTeam);
        setDescription(description);
        addOwner(owner);
        String objectid = PSGroup.CreateTeam(nomeTeam, description, owner, template);
        setObjectId(objectid);
        startSyncronizing();
        //setEmail_address(nomeTeam);
    }

    private Team(){}

    public static Team fromExisting(){
        Team team = new Team();
        team.stopSyncronizing();
        return team;
    }
}
