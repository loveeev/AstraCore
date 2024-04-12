package dev.loveeev.astracore.handler;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class War {
    private String name;
    private boolean isEnable = false;
    private List<Nation> nationList = new ArrayList();
    private List<UUID> memberList = new ArrayList();
    private HashMap<Town, Integer> occupationProgress = new HashMap();
    private List<Town> occupiedTowns = new ArrayList();

    public War(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public List<Nation> getNationList() {
        return this.nationList;
    }

    public List<UUID> getMemberList() {
        return this.memberList;
    }

    public HashMap<Town, Integer> getOccupationProgress() {
        return this.occupationProgress;
    }

    public List<Town> getOccupiedTowns() {
        return this.occupiedTowns;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void setNationList(List<Nation> nationList) {
        this.nationList = nationList;
    }

    public void setMemberList(List<UUID> memberList) {
        this.memberList = memberList;
    }

    public void setOccupationProgress(HashMap<Town, Integer> occupationProgress) {
        this.occupationProgress = occupationProgress;
    }

    public void setOccupiedTowns(List<Town> occupiedTowns) {
        this.occupiedTowns = occupiedTowns;
    }
}