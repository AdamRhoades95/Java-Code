package com.akgames.animation;

public class LeaderBoardInput {
    String username, mode, kills, points, level;

    public LeaderBoardInput() {
    }

    public LeaderBoardInput(String username, String mode, String kills, String points, String level) {
        this.username = username;
        this.kills = kills;
        this.points = points;
        this.level = level;
        this.mode = mode;
    }

    public String getUsername() {
        return username;
    }

    public String getKills() {
        return kills;
    }

    public String getPoints() {
        return points;
    }

    public String getLevel() {
        return level;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setKills(String kills) {
        this.kills = kills;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
