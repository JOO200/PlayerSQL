package com.mengcraft.playersql.lib;

import com.mengcraft.playersql.PluginMain;
import com.mengcraft.playersql.lib.ItemUtil.Simple;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;


public class ItemUtilHandler {

    private final Plugin proxy;
    private final Server server;

    private ItemUtil util;
    private String version;
    private ItemStack item;

    public ItemUtilHandler(Plugin in) {
        if (in == null) {
            throw new NullPointerException();
        }
        this.proxy = in;
        this.server = in.getServer();
    }

    public ItemUtil handle() {
        if (util == null) {
            Plugin plugin = proxy.getServer().getPluginManager().getPlugin("ProtocolLib");
            if (plugin == null) {
                if (test(version())) {
                    util = new Simple(version());
                } else {
                    throw new RuntimeException("Hasn't compatible util! Update ProtocolLib");
                }
            } else if (test()) {
                util = new ItemUtil.PLib();
            }
        }
        return util;
    }

    private boolean test() {
        try {
            ItemUtil util = new ItemUtil.PLib();

            if (item().equals(util.convert(util.convert(item())))) {
                proxy.getLogger().info("Server version: " + version + '.');
                proxy.getLogger().info("Warped util work well!");
            } else {
                throw new RuntimeException("Warped util not work!");
            }
            return true;
        } catch (Exception e) {
            proxy.getLogger().warning(e.toString());
            PluginMain.bug.notify(e);
        }
        return false;
    }

    private boolean test(String version) {
        try {
            ItemUtil util = new Simple(version);

            if (item().equals(util.convert(util.convert(item())))) {
                proxy.getLogger().info("Server version: " + version + '.');
                proxy.getLogger().info("Build-in util work well!");
            } else {
                throw new RuntimeException("Build-in util not work!");
            }
            return true;
        } catch (Exception e) {
            proxy.getLogger().warning(e.toString());
            PluginMain.bug.notify(e);
        }
        return false;
    }

    private ItemStack item() {
        if (item == null) {
            item = new ItemStack(Material.DIAMOND_SWORD);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("test");
            meta.setLore(Arrays.asList("a", "b", "c"));

            item.setItemMeta(meta);
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 0);
        }
        return item;
    }

    private String version() {
        if (version == null) {
            version = server.getClass().getName().split("\\.")[3];
        }
        return version;
    }

}
