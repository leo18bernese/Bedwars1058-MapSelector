package me.leoo.bedwars.mapselector.menu;

import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.utils.ItemUtil;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MapSelectorMenuProxy {

    public static void openFirstGui(Player player, String group) {
        Inventory inventory = Bukkit.createInventory(null, MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.per_group_menu.slots"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.per_group_menu.title"));

        for (String extraItems : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)) {
            if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".enabled") &&
                    MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".extra") &&
                    MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".gui") == 1) {
                inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".name"), MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item." + extraItems + ".lore"), MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".enchanted"), group, null, "true", "map_selector.menu.item." + extraItems, null));
            }
        }

        List<String> joinRandomLore = new ArrayList<>();
        for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.join_random.lore")) {
            if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(group));
            joinRandomLore.add(s);
        }

        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.join_random.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.join_random.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.join_random.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.join_random.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.join_random.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.join_random.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), joinRandomLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.join_random.enchanted"), group, null, "true", null, null));
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_selector.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map_selector.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_selector.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_selector.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map_selector.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_selector.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.map_selector.lore"), MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_selector.enchanted"), group, null, "true", null, null));
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.close.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.close.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.close.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.close.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.close.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.close.name"), MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.close.lore"), MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.close.enchanted"), group, null, "true", null, null));
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.rejoin.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.rejoin.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.rejoin.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.rejoin.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.rejoin.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.rejoin.name"), MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.rejoin.lore"), MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.rejoin.enchanted"), group, null, "true", null, null));
        }

        player.openInventory(inventory);
    }

    public static void openSecondGui(Player player, String group, int startMaps, int currentpage) {
        Inventory inventory = Bukkit.createInventory(null, MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.selector.slots"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.selector.title"));

        SelectorUtil.getStartMaps().put(player, startMaps);
        SelectorUtil.getPage().put(player, currentpage);

        List<String> groups;
        if (group.contains(",")) {
            groups = Arrays.asList(group.split(","));
        } else {
            groups = Collections.singletonList(group);
        }

        if (SelectorUtil.isOldDate()) {
            MapSelector.getPlugin().getMapSelectorDatabase().setAllPlayersUses(0);
        }

        List<CachedArena> arenas = new ArrayList<>();
        for (CachedArena arena : ArenaManager.getArenas()) {
            if (groups.contains(arena.getArenaGroup()) && arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) {
                arenas.add(arena);
            }
        }
        if (arenas.isEmpty()) {
            player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.no_maps"));
            return;
        }

        com.andrei1058.bedwars.proxy.api.Language language = LanguageManager.get().getPlayerLanguage(player);
        arenas.sort(Comparator.comparing(arena -> arena.getDisplayName(language)));

        for (String extraItems : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)) {
            if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".enabled")) {
                if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".extra")) {
                    if (MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".gui") == 2) {
                        inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".name"), MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item." + extraItems + ".lore"), MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".enchanted"), group, "null", "true", "map_selector.menu.item." + extraItems, null));
                    }
                }
            }
        }

        for (String slotUsed : MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.slots").split(",")) {
            int slot;
            try {
                slot = Integer.parseInt(slotUsed);
            } catch (Exception e) {
                continue;
            }
            if (startMaps >= arenas.size()) {
                continue;
            }

            List<String> mapLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.map.lore")) {
                if (s.contains("{group_name}"))
                    s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
                if (s.contains("{available_games}")) s = s.replace("{available_games}", "1");
                if (s.contains("{times_joined}"))
                    s = s.replace("{times_joined}", String.valueOf(MapSelector.getPlugin().getYamlConfig().getMapJoins(player, arenas.get(startMaps).getArenaName())));
                if (s.contains("{selections_type}"))
                    s = s.replace("{selections_type}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
                if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.getSelectionsType(player));
                if (s.contains("{remaining_uses}"))
                    s = s.replace("{remaining_uses}", SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message")) ? MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                if (s.contains("{status}")) s = s.replace("{status}", arenas.get(startMaps).getStatus().toString());
                if (s.contains("{on}"))
                    s = s.replace("{on}", String.valueOf(arenas.get(startMaps).getCurrentPlayers()));
                if (s.contains("{max}")) s = s.replace("{max}", String.valueOf(arenas.get(startMaps).getMaxPlayers()));
                mapLore.add(s);
            }

            List<String> mapFavoriteLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.map_favorite.lore")) {
                if (s.contains("{group_name}"))
                    s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
                if (s.contains("{available_games}")) s = s.replace("{available_games}", "1");
                if (s.contains("{times_joined}"))
                    s = s.replace("{times_joined}", String.valueOf(MapSelector.getPlugin().getYamlConfig().getMapJoins(player, arenas.get(startMaps).getArenaName())));
                if (s.contains("{selections_type}"))
                    s = s.replace("{selections_type}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
                if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.getSelectionsType(player));
                if (s.contains("{remaining_uses}"))
                    s = s.replace("{remaining_uses}", SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message")) ? MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                if (s.contains("{status}")) s = s.replace("{status}", arenas.get(startMaps).getStatus().toString());
                if (s.contains("{on}"))
                    s = s.replace("{on}", String.valueOf(arenas.get(startMaps).getCurrentPlayers()));
                if (s.contains("{max}")) s = s.replace("{max}", String.valueOf(arenas.get(startMaps).getMaxPlayers()));
                mapFavoriteLore.add(s);
            }

            List<String> mapNoUsesNoPermissionsLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.map_no_permissions_no_uses.lore")) {
                if (s.contains("{group_name}"))
                    s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
                if (s.contains("{available_games}")) s = s.replace("{available_games}", "1");
                if (s.contains("{times_joined}"))
                    s = s.replace("{times_joined}", String.valueOf(MapSelector.getPlugin().getYamlConfig().getMapJoins(player, arenas.get(startMaps).getArenaName())));
                if (s.contains("{selections_type}"))
                    s = s.replace("{selections_type}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
                if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.getSelectionsType(player));
                if (s.contains("{remaining_uses}"))
                    s = s.replace("{remaining_uses}", SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message")) ? MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                if (s.contains("{status}")) s = s.replace("{status}", arenas.get(startMaps).getStatus().toString());
                if (s.contains("{on}"))
                    s = s.replace("{on}", String.valueOf(arenas.get(startMaps).getCurrentPlayers()));
                if (s.contains("{max}")) s = s.replace("{max}", String.valueOf(arenas.get(startMaps).getMaxPlayers()));
                mapNoUsesNoPermissionsLore.add(s);
            }

            if (SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                if (MapSelector.getPlugin().getYamlConfig().isFavorite(player, String.valueOf(arenas.get(startMaps).getArenaName()))) {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_favorite.enabled")) {
                        inventory.setItem(slot, ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map_favorite.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapFavoriteLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_favorite.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
                    }
                } else {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map.enabled")) {
                        inventory.setItem(slot, ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
                    }
                }
            } else {
                if (player.hasPermission(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.permission"))) {
                    if (MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))) {
                        if (MapSelector.getPlugin().getYamlConfig().isFavorite(player, String.valueOf(arenas.get(startMaps).getArenaName()))) {
                            if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_favorite.enabled")) {
                                inventory.setItem(slot, ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map_favorite.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapFavoriteLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_favorite.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
                            }
                        } else {
                            if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map.enabled")) {
                                inventory.setItem(slot, ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
                            }
                        }
                    } else {
                        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enabled")) {
                            inventory.setItem(slot, ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map_no_permissions_no_uses.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapNoUsesNoPermissionsLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
                        }
                    }
                } else {
                    ItemStack item = ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.map_no_permissions_no_uses.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapNoUsesNoPermissionsLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group);
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enabled")) {
                        inventory.setItem(slot, item);
                    }
                }
            }

            startMaps++;
        }

        List<String> nextPageLore = new ArrayList<>();
        for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.next_page.lore")) {
            nextPageLore.add(s.replace("{next_page}", String.valueOf((currentpage + 1) + 1)));
        }

        List<String> previousPageLore = new ArrayList<>();
        for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.previous_page.lore")) {
            previousPageLore.add(s.replace("{previous_page}", String.valueOf((currentpage - 1) + 1)));
        }

        if (arenas.size() - (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.slots").split(",").length * currentpage) > MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.slots").split(",").length &&
                MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.next_page.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.next_page.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.next_page.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.next_page.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.next_page.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.next_page.name"), nextPageLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.next_page.enchanted"), null, null, "true", null, group));
        }

        if (currentpage > 0 && MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.previous_page.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.previous_page.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.previous_page.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.previous_page.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.previous_page.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.previous_page.name"), previousPageLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.previous_page.enchanted"), null, null, "true", null, group));
        }

        List<String> randommapLore = new ArrayList<>();
        for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.random_map.lore")) {
            randommapLore.add(s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)).replace("{selections_type}", SelectorUtil.getSelectionsType(player)));
        }
        List<String> randomfavouriteLore = new ArrayList<>();
        for (String s : MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.random_favourite.lore")) {
            randomfavouriteLore.add(s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)).replace("{selections_type}", SelectorUtil.getSelectionsType(player)));
        }

        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.random_map.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.random_map.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_map.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_map.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.random_map.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_map.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), randommapLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.random_map.enchanted"), null, group, "true", null, null));
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.random_favourite.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.random_favourite.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_favourite.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_favourite.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.random_favourite.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_favourite.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), randomfavouriteLore, MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.random_favourite.enchanted"), null, group, "true", null, null));
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.back.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.back.slot"), ItemUtil.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.back.item")), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.back.head_url"), MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item.back.data"), MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.back.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), MapSelector.getPlugin().getMainConfig().getList("map_selector.menu.item.back.lore"), MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item.back.enchanted"), null, group, "true", null, null));
        }

        player.openInventory(inventory);
    }
}