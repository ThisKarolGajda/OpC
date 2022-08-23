package me.opkarol.opc.api.extensions;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import static me.opkarol.opc.api.utils.Util.getOrDefault;

public class Vault {
    private static Vault vault;
    private boolean enabled;
    private Economy economy;

    public Vault() {
        vault = this;
        enabled = Bukkit.getPluginManager().isPluginEnabled("Vault");
        if (enabled) {
            enabled = setupEconomy();
        }
    }

    public static Vault getInstance() {
        return getOrDefault(vault, new Vault());
    }

    public boolean isEnabled() {
        return enabled;
    }

    private boolean setupEconomy() {
        if (!enabled) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public VAULT_RETURN_INFO withdraw(OfflinePlayer player, double amount) {
        if (isEnabled()) {
            if (has(player, amount)) {
                if (economy.withdrawPlayer(player, amount).transactionSuccess()) {
                    return VAULT_RETURN_INFO.WITHDRAW_SUCCESSFUL;
                }
                return VAULT_RETURN_INFO.WITHDRAW_NOT_SUCCESSFUL;
            }
            return VAULT_RETURN_INFO.WITHDRAW_TOO_BROKE;
        }
        return VAULT_RETURN_INFO.PLUGIN_NOT_ENABLED;
    }

    public VAULT_RETURN_INFO deposit(OfflinePlayer player, double amount) {
        if (isEnabled()) {
            if (economy.depositPlayer(player, amount).transactionSuccess()) {
                return VAULT_RETURN_INFO.DEPOSIT_SUCCESSFUL;
            }
            return VAULT_RETURN_INFO.DEPOSIT_NOT_SUCCESSFUL;
        }
        return VAULT_RETURN_INFO.PLUGIN_NOT_ENABLED;
    }

    public boolean has(OfflinePlayer player, double amount) {
        return economy.has(player, amount);
    }

    public enum VAULT_RETURN_INFO {
        PLUGIN_NOT_ENABLED, WITHDRAW_SUCCESSFUL, WITHDRAW_NOT_SUCCESSFUL, WITHDRAW_TOO_BROKE, DEPOSIT_SUCCESSFUL, DEPOSIT_NOT_SUCCESSFUL
    }

}
