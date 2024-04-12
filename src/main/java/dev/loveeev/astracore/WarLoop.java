package dev.loveeev.astracore;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.DataUtil;
import dev.loveeev.astracore.util.TownyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.*;

public class WarLoop {
    public WarLoop() {
    }

    public void startLoop(Main plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (AstraWarsData.hasEnabledWar) {
                War war = DataUtil.getFirstEnabledWar();
                if (war != null) {
                    this.loopBody(war);
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification((Player) null, "WarLoop.startLoop war is null");
                }
            }

        }, 0L, 100L);
    }

    private void loopBody(War war) {
        HashMap<Town, Set<Player>> playersInHomeChunk = new HashMap();
        Iterator var3 = war.getNationList().iterator();

        while(var3.hasNext()) {
            Nation nation = (Nation)var3.next();
            Iterator var5 = nation.getTowns().iterator();

            while(var5.hasNext()) {
                Town town = (Town)var5.next();
                playersInHomeChunk.put(town, new HashSet());
            }
        }

        var3 = Bukkit.getOnlinePlayers().iterator();

        while(true) {
            Player player;
            do {
                if (!var3.hasNext()) {
                    var3 = this.mostNations(playersInHomeChunk).entrySet().iterator();

                    Map.Entry entry;
                    while(var3.hasNext()) {
                        entry = (Map.Entry)var3.next();
                        if (((List)entry.getValue()).size() != 0) {
                            Nation townNation = TownyUtil.getTownNation((Town)entry.getKey());
                            boolean hasOnlyEnemy = this.hasOnlyEnemyNation(townNation, (List)entry.getValue());
                            boolean isOccupiedTown = war.getOccupiedTowns().contains(entry.getKey());
                            if (hasOnlyEnemy != isOccupiedTown) {
                                int seconds = 10;
                                if (war.getOccupationProgress().containsKey(entry.getKey())) {
                                    seconds += (Integer)war.getOccupationProgress().get(entry.getKey());
                                    war.getOccupationProgress().remove(entry.getKey());
                                    if (seconds - 5 >= Settings.TIMEFOROCCUOATION) {
                                        boolean isDeOccupation = false;
                                        if (war.getOccupiedTowns().contains(entry.getKey())) {
                                            war.getOccupiedTowns().remove(entry.getKey());
                                            isDeOccupation = true;
                                        } else {
                                            war.getOccupiedTowns().add((Town)entry.getKey());
                                        }

                                        TownyUtil.occupy(war, (Town)entry.getKey(), townNation, (Nation)((List)entry.getValue()).get(0), isDeOccupation);
                                    } else {
                                        war.getOccupationProgress().put((Town)entry.getKey(), seconds);
                                    }
                                } else {
                                    war.getOccupationProgress().put((Town)entry.getKey(), seconds);
                                }
                            }
                        }
                    }

                    var3 = war.getOccupationProgress().entrySet().iterator();

                    while(var3.hasNext()) {
                        entry = (Map.Entry)var3.next();
                        int newValue = (Integer)entry.getValue() - 5;
                        if (newValue <= 0) {
                            war.getOccupationProgress().remove(entry.getKey());
                        } else {
                            entry.setValue(newValue);
                        }
                    }

                    return;
                }

                player = (Player)var3.next();
            } while(player.hasPermission(Constants.adminPermission));

            Chunk playerLocationChunk = player.getLocation().getChunk();
            Iterator var16 = playersInHomeChunk.keySet().iterator();

            while(var16.hasNext()) {
                Town townInWar = (Town)var16.next();

                try {
                    if (townInWar.hasSpawn() && townInWar.getSpawn().getChunk() == playerLocationChunk) {
                        ((Set)playersInHomeChunk.get(townInWar)).add(player);
                    }
                } catch (TownyException var10) {
                }
            }
        }
    }

    private HashMap<Town, List<Nation>> mostNations(HashMap<Town, Set<Player>> playersInHomeChunk) {
        HashMap<Town, List<Nation>> forReturn = new HashMap();

        Map.Entry entry;
        ArrayList nationForReturn;
        for(Iterator var3 = playersInHomeChunk.entrySet().iterator(); var3.hasNext(); forReturn.put((Town)entry.getKey(), nationForReturn)) {
            entry = (Map.Entry)var3.next();
            HashMap<Nation, Integer> nationMap = new HashMap();
            Iterator var6 = ((Set)entry.getValue()).iterator();

            int biggestPlayersAmount;
            while(var6.hasNext()) {
                Player player = (Player)var6.next();
                Nation nation = TownyUtil.getPlayerNation(player);
                if (nation != null) {
                    biggestPlayersAmount = 1;
                    if (nationMap.containsKey(nation)) {
                        biggestPlayersAmount += (Integer)nationMap.get(nation);
                        nationMap.remove(nation);
                        nationMap.put(nation, biggestPlayersAmount);
                    } else {
                        nationMap.put(nation, biggestPlayersAmount);
                    }
                }
            }

            HashMap<Nation, Integer> sortedNationMap = new HashMap();
            ArrayList<Integer> valueList = new ArrayList(nationMap.values());
            valueList.sort(Collections.reverseOrder());
            Iterator var14 = valueList.iterator();

            Iterator var10;
            Map.Entry entry1;
            while(var14.hasNext()) {
                biggestPlayersAmount = (Integer)var14.next();
                var10 = nationMap.entrySet().iterator();

                while(var10.hasNext()) {
                    entry1 = (Map.Entry)var10.next();
                    if ((Integer)entry1.getValue() == biggestPlayersAmount) {
                        sortedNationMap.put((Nation)entry1.getKey(), biggestPlayersAmount);
                    }
                }
            }

            nationForReturn = new ArrayList();
            biggestPlayersAmount = -1;

            for(var10 = sortedNationMap.entrySet().iterator(); var10.hasNext(); nationForReturn.add((Nation)entry1.getKey())) {
                entry1 = (Map.Entry)var10.next();
                if (biggestPlayersAmount == -1) {
                    biggestPlayersAmount = (Integer)entry1.getValue();
                } else if ((Integer)entry1.getValue() != biggestPlayersAmount) {
                    break;
                }
            }
        }

        return forReturn;
    }

    private boolean hasOnlyEnemyNation(Nation townNation, List<Nation> nationList) {
        boolean returnValue = false;
        if (townNation == null) {
            return false;
        } else {
            Iterator var4 = nationList.iterator();

            while(var4.hasNext()) {
                Nation nation = (Nation)var4.next();
                if (nation == townNation) {
                    return false;
                }

                if (!townNation.hasAlly(nation)) {
                    returnValue = true;
                }
            }

            return returnValue;
        }
    }
}