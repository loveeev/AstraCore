package dev.loveeev.astracore.data;

import lombok.Getter;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;


@Getter
public class BuildingData {
    private final String name;
    private final List<String> lore;
    private final CompMaterial material;
    private final Integer id;

    public BuildingData(String name, List<String> lore, CompMaterial material, Integer id) {
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.id = id;
        Building.addAll(this);
    }
}
