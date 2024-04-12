package dev.loveeev.astracore.module;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.exception.ModuleStateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
public abstract class SpigotService implements Service {

    final Main plugin;
    boolean enabled = false;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    @SneakyThrows(ModuleStateException.class)
    public void enable() {
        if (enabled) throw new ModuleStateException("Module is already enabled");
        enabled = true;
    }

    @Override
    @SneakyThrows(ModuleStateException.class)
    public void disable() {
        if (!enabled) throw new ModuleStateException("Module is already disabled");
        enabled = false;
    }
}
