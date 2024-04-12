package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.BaseBuilds;
import dev.loveeev.astracore.data.TownData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ABuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length < 3) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Использование: /abuild <город из тауни> <название здания> <true/false>");
            return true;
        }
        String townName = args[0];
        Town town;
        town = TownyAPI.getInstance().getTown(townName);

        String building = args[1].toLowerCase();
        boolean isBuilding = Boolean.parseBoolean(args[2]);

        BaseBuilds baseBuilds = BaseBuilds.getOrCreate(townName);
        TownData townData = TownData.getOrCreate(townName);
        switch (building) {
            case "военный-завод" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsfactorywar()) {
                        baseBuilds.setFactoryWar(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsfactorywar()) {
                        baseBuilds.setFactoryWar(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "порт" -> {
                if (isBuilding) {
                    if (!baseBuilds.isPort()) {
                        baseBuilds.setPort(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isPort()) {
                        baseBuilds.setPort(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "банк" -> {
                if (isBuilding) {
                    if (!baseBuilds.isBank()) {
                        baseBuilds.setBank(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isBank()) {
                        baseBuilds.setBank(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "телеграф" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsteleg()) {
                        baseBuilds.setTelegraph(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsteleg()) {
                        baseBuilds.setTelegraph(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "предприятие" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsmayorbank()) {
                        baseBuilds.setMayorBank(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsmayorbank()) {
                        baseBuilds.setMayorBank(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "нефтеперерабатывающий-завод" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsfuelcraft()) {
                        baseBuilds.setFuelCraft(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsfuelcraft()) {
                        baseBuilds.setFuelCraft(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "нефтяная-станция" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsfuel()) {
                        baseBuilds.setFuel(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsfuel()) {
                        baseBuilds.setFuel(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "сталелитейный-завод" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIssteelfactory()) {
                        baseBuilds.setSteelFactory(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIssteelfactory()) {
                        baseBuilds.setSteelFactory(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "текстильная-фабрика" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsleather()) {
                        baseBuilds.setLeather(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsleather()) {
                        baseBuilds.setLeather(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            case "монетный-двор" -> {
                if (isBuilding) {
                    if (!baseBuilds.isIsyard()) {
                        baseBuilds.setYard(true);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже выдано.");
                    }
                } else {
                    if (baseBuilds.isIsyard()) {
                        baseBuilds.setYard(false);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Уже забрано.");
                    }
                }
            }
            default -> {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Неверное название здания.");
                return true;
            }
        }
        Main.getInstance().getChatUtility().sendSuccessNotification(player, isBuilding ? "Вы выдали доступ" : "Вы забрали доступ");
        return true;
    }
}
