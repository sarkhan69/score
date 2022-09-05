package entity;

import lombok.Getter;

@Getter
public class Entity {
    private String date;
    private String country;
    private String league;
    private String team1;
    private String team2;
    private String kf;
    private String chance1;
    private String chance2;

    public Entity(String[] strings) {
        this.date = strings[0];
        this.country = strings[1];
        this.league = strings[2];
        this.team1 = getTeamOne(strings[3]);
        this.team2 = getTeamTwo(strings[3]);
        this.kf = strings[4];
        this.chance1 = getChanceOne(strings[5]);
        this.chance2 = getChanceTwo(strings[5]);
    }

    private String getTeamOne(String string3) {
        String[] arr = string3.split(" - ");
        return arr[0];
    }

    private String getTeamTwo(String string3) {
        String[] arr = string3.split(" - ");
        return arr[1];
    }

    private String getChanceOne(String string5) {
        String[] arr = string5.split(",");
        return arr[0];
    }

    private String getChanceTwo(String string5) {
        String[] arr = string5.split(",");
        return arr[1];
    }
}
