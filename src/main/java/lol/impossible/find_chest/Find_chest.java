package lol.impossible.find_chest;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Find_chest extends JavaPlugin implements Listener {

    private int numberOfChests = 0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("start_br")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                distributeItems(player);
                return true;
            } else {
                sender.sendMessage("Цю команду можна виконати тільки як гравець.");
                return false;
            }
        }
        return false;
    }

    private void clearChests(List<Block> chestBlocks) {
        for (Block chestBlock : chestBlocks) {
            if (chestBlock.getState() instanceof Chest) {
                Chest chest = (Chest) chestBlock.getState();
                Inventory chestInventory = chest.getInventory();
                chestInventory.clear(); // Очищаємо вміст скрині
            }
        }
    }

    private void distributeItems(Player player) {
        List<Block> chest0Blocks = findChests("Chest_");
        clearChests(chest0Blocks);

        distributeDMGItems(player);
        distributeHealItems(player);
        distributeArmourItems(player);
        distributePotionItems(player);
    }


    private void distributeDMGItems(Player player) {
        Inventory sourceInventory = findDMGChestInventory("ItemDMG");

        if (sourceInventory == null) {
            player.sendMessage("Скриня з предметами DMG не знайдена.");
            return;
        }

        numberOfChests = 0;

        List<Block> chest0Blocks = findChests("Chest_");

        if (!chest0Blocks.isEmpty()) {
            List<ItemStack> itemsToDistribute = new ArrayList<>();
            for (ItemStack item : sourceInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    itemsToDistribute.add(item.clone());
                }
            }
            Collections.shuffle(itemsToDistribute);

            int itemsPerChest = itemsToDistribute.size() / chest0Blocks.size();
            int remainingItems = itemsToDistribute.size() % chest0Blocks.size();
            int currentIndex = 0;
            for (Block chest0Block : chest0Blocks) {
                Chest chest0 = (Chest) chest0Block.getState();
                Inventory targetInventory = chest0.getInventory();
                int itemsToAdd = itemsPerChest + (remainingItems > 0 ? 1 : 0);
                for (int i = 0; i < itemsToAdd && currentIndex < itemsToDistribute.size(); i++) {
                    targetInventory.addItem(itemsToDistribute.get(currentIndex++));
                }
                remainingItems--;
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                chest0Blocks.forEach(chest0Block -> {
                    Chest chest0 = (Chest) chest0Block.getState();
                    chest0.getInventory().getViewers().forEach(humanEntity -> humanEntity.closeInventory());
                });
            });

            numberOfChests += chest0Blocks.size();
        } else {
            player.sendMessage("Скрині з шаблоном імені 'Chest_' не знайдено.");
        }

        player.sendMessage("Знайдено скринь 'Chest_' для предметів DMG: " + numberOfChests);
    }

    private void distributePotionItems(Player player) {
        Inventory sourceInventory = findPotionChestInventory("ItemPotion");

        if (sourceInventory == null) {
            player.sendMessage("Скриня з предметами DMG не знайдена.");
            return;
        }

        numberOfChests = 0;

        List<Block> chest0Blocks = findChests("Chest_");

        if (!chest0Blocks.isEmpty()) {
            List<ItemStack> itemsToDistribute = new ArrayList<>();
            for (ItemStack item : sourceInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    itemsToDistribute.add(item.clone());
                }
            }
            Collections.shuffle(itemsToDistribute);

            int itemsPerChest = itemsToDistribute.size() / chest0Blocks.size();
            int remainingItems = itemsToDistribute.size() % chest0Blocks.size();
            int currentIndex = 0;
            for (Block chest0Block : chest0Blocks) {
                Chest chest0 = (Chest) chest0Block.getState();
                Inventory targetInventory = chest0.getInventory();
                int itemsToAdd = itemsPerChest + (remainingItems > 0 ? 1 : 0);
                for (int i = 0; i < itemsToAdd && currentIndex < itemsToDistribute.size(); i++) {
                    targetInventory.addItem(itemsToDistribute.get(currentIndex++));
                }
                remainingItems--;
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                chest0Blocks.forEach(chest0Block -> {
                    Chest chest0 = (Chest) chest0Block.getState();
                    chest0.getInventory().getViewers().forEach(humanEntity -> humanEntity.closeInventory());
                });
            });

            numberOfChests += chest0Blocks.size();
        } else {
            player.sendMessage("Скрині з шаблоном імені 'Chest_' не знайдено.");
        }

        player.sendMessage("Знайдено скринь 'Chest_' для предметів Potion: " + numberOfChests);
    }

    private void distributeHealItems(Player player) {
        Inventory sourceInventory = findHealChestInventory("ItemHeal");

        if (sourceInventory == null) {
            player.sendMessage("Скриня з предметами Heal не знайдена.");
            return;
        }

        numberOfChests = 0;

        List<Block> chest0Blocks = findChests("Chest_");

        if (!chest0Blocks.isEmpty()) {
            List<ItemStack> healItemsToDistribute = new ArrayList<>();
            for (ItemStack item : sourceInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    healItemsToDistribute.add(item.clone());
                }
            }

            for (ItemStack healItem : healItemsToDistribute) {
                int randomIndex = (int) (Math.random() * chest0Blocks.size());
                Chest randomChest = (Chest) chest0Blocks.get(randomIndex).getState();
                Inventory targetInventory = randomChest.getInventory();
                targetInventory.addItem(healItem);
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                chest0Blocks.forEach(chest0Block -> {
                    Chest chest0 = (Chest) chest0Block.getState();
                    chest0.getInventory().getViewers().forEach(humanEntity -> humanEntity.closeInventory());
                });
            });

            numberOfChests += chest0Blocks.size();
        } else {
            player.sendMessage("Скрині з шаблоном імені 'Chest_' не знайдено.");
        }

        player.sendMessage("Знайдено скринь 'Chest_' для предметів Heal: " + numberOfChests);
    }

    private void distributeArmourItems(Player player) {
        Inventory sourceInventory = findArmourChestInventory("ItemArmour");

        if (sourceInventory == null) {
            player.sendMessage("Скриня з предметами Heal не знайдена.");
            return;
        }

        numberOfChests = 0;

        List<Block> chest0Blocks = findChests("Chest_");

        if (!chest0Blocks.isEmpty()) {
            List<ItemStack> healItemsToDistribute = new ArrayList<>();
            for (ItemStack item : sourceInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    healItemsToDistribute.add(item.clone());
                }
            }

            for (ItemStack healItem : healItemsToDistribute) {
                int randomIndex = (int) (Math.random() * chest0Blocks.size());
                Chest randomChest = (Chest) chest0Blocks.get(randomIndex).getState();
                Inventory targetInventory = randomChest.getInventory();
                targetInventory.addItem(healItem);
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                chest0Blocks.forEach(chest0Block -> {
                    Chest chest0 = (Chest) chest0Block.getState();
                    chest0.getInventory().getViewers().forEach(humanEntity -> humanEntity.closeInventory());
                });
            });

            numberOfChests += chest0Blocks.size();
        } else {
            player.sendMessage("Скрині з шаблоном імені 'Chest_' не знайдено.");
        }

        player.sendMessage("Знайдено скринь 'Chest_' для предметів Armour: " + numberOfChests);
    }


    private Inventory findDMGChestInventory(String chestName) {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;
                        if (chestName.equals(chest.getCustomName())) {
                            return chest.getInventory();
                        }
                    }
                }
            }
        }
        return null;
    }

    private Inventory findHealChestInventory(String chestName) {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;
                        if (chestName.equals(chest.getCustomName())) {
                            return chest.getInventory();
                        }
                    }
                }
            }
        }
        return null;
    }

    private Inventory findArmourChestInventory(String chestName) {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;
                        if (chestName.equals(chest.getCustomName())) {
                            return chest.getInventory();
                        }
                    }
                }
            }
        }
        return null;
    }

    private Inventory findPotionChestInventory(String chestName) {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;
                        if (chestName.equals(chest.getCustomName())) {
                            return chest.getInventory();
                        }
                    }
                }
            }
        }
        return null;
    }

    private List<Block> findChests(String namePrefix) {
        List<Block> chestBlocks = new ArrayList<>();
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;
                        if (chest.getCustomName() != null && chest.getCustomName().startsWith(namePrefix)) {
                            chestBlocks.add(chest.getBlock());
                        }
                    }
                }
            }
        }
        return chestBlocks;
    }
}