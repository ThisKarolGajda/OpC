package me.opkarol.opc.api.misc;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.utils.VariableUtil;

/**
 * You need to register/initialize your class to make the cooldown not be default value (0).
 * @param <K> class holder representing specific unchangeable value
 */
public abstract class CooldownModule<K> {
    private static CooldownModule<?> cooldownModule;
    private final OpMap<K, Long> cooldownMap = new OpMap<>();

    public CooldownModule() {
        cooldownModule = this;
    }

    @SuppressWarnings("all")
    public static <S> CooldownModule<S> getCooldownModule() {
        return (CooldownModule<S>) VariableUtil.getOrDefault(cooldownModule, new CooldownModule<>(){
            @Override
            public long getCooldownTimeSeconds() {
                return 0L;
            }
        });
    }

    public void addCooldown(K object) {
        this.cooldownMap.set(object, getCurrentUnix() + getCooldownTimeSeconds());
    }

    public void addCustomCooldown(K object, long seconds) {
        this.cooldownMap.set(object, seconds);
    }

    public boolean hasActiveCooldown(K object) {
        if (!this.cooldownMap.containsKey(object)) {
            return false;
        }

        long cooldown = this.cooldownMap.unsafeGet(object);
        long current = getCurrentUnix();
        return cooldown >= current;
    }

    public long getLeftCooldown(K object) {
        if (!this.cooldownMap.containsKey(object)) {
            return 0;
        }

        long cooldown = this.cooldownMap.unsafeGet(object);
        long current = getCurrentUnix();
        long diff = cooldown - current;
        return diff < 0 ? 0 : diff;
    }

    private long getCurrentUnix() {
        long MILLI_TO_SECONDS = 1000L;
        return System.currentTimeMillis() / MILLI_TO_SECONDS;
    }

    public OpMap<K, Long> getCooldownMap() {
        return this.cooldownMap;
    }

    public abstract long getCooldownTimeSeconds();
}