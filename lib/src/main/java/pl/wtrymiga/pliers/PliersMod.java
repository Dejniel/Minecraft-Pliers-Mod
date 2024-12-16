package pl.wtrymiga.pliers;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;

import org.bukkit.inventory.EquipmentSlot;

import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import org.bukkit.entity.Player;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PliersMod extends JavaPlugin implements Listener {
	private static final TextComponent DEFAULT_NAME = Component.text("Pliers");
	private static final TextComponent DESCRIPTION = Component.text("Unique pliers designed for milking chickens");
	private static final long COOLDOWN_TIME_MS = 3000;

	private static final Material PRE_MATERIAL = Material.CARROT_ON_A_STICK;
	private static final String KEY_PLIERS = "pliers";
	private static final String KEY_LAST_USE = "last_use";

	@Override
	public void onEnable() {
		// Dodaj własną recepturę nożyc
		createRecipes();
		// Dodaj własne wyzwalacze
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Uaktualnij recepturę gracza przy wejściu na serwer
		Player player = event.getPlayer();
		player.undiscoverRecipe(NamespacedKey.minecraft(KEY_PLIERS));
		player.discoverRecipe(NamespacedKey.minecraft(KEY_PLIERS));
	}

	private void createRecipes() {
		ItemStack customShears = new ItemStack(PRE_MATERIAL);
		
		ItemMeta meta = customShears.getItemMeta();
		meta.setItemModel(NamespacedKey.minecraft("shears"));
		meta.displayName(DEFAULT_NAME);
		meta.lore(List.of(DESCRIPTION));
		customShears.setItemMeta(meta);

		ShapedRecipe customRecipe = new ShapedRecipe(NamespacedKey.minecraft(KEY_PLIERS), customShears);
		customRecipe.shape("I ", "II");
		customRecipe.setIngredient('I', Material.IRON_INGOT);
		getServer().addRecipe(customRecipe);
		getLogger().info("Added unique pliers designed for milking chickens");
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		// Użyj domyślego zachowania dla lewej ręki
		if (event.getHand() != EquipmentSlot.HAND)
			return;
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null || item.getType() != PRE_MATERIAL)
			return;
		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta == null || !itemMeta.displayName().equals(DEFAULT_NAME))
			return;

		// Anuluj domyślne działanie
		event.setCancelled(true);
		Entity entity = event.getRightClicked();
		if (isCooldownActive(entity))
			return;
		if (!performPliersAction(entity))
			return;
		reduceDurability(player, item, itemMeta);

	}

	private void reduceDurability(Player player, ItemStack item, ItemMeta meta) {
		// Zużycie przedmiotu
		if (meta instanceof org.bukkit.inventory.meta.Damageable damageable) {
			damageable.setDamage(damageable.getDamage() + 1);
			item.setItemMeta(meta);
			if (damageable.getDamage() >= item.getType().getMaxDurability())
				player.getInventory().remove(item);
		}
	}

	private boolean performPliersAction(Entity entity) {
		switch (entity) {
		case Chicken chicken -> entity.getWorld().dropItemNaturally(chicken.getLocation(),
				new ItemStack(Material.EGG, 3 + (int) (Math.random() * 3)));
		case Cow cow -> cow.getWorld().dropItemNaturally(cow.getLocation(),
				new ItemStack(Material.LEATHER, 1 + (int) (Math.random() * 1.2)));
		case Sheep sheep -> sheep.getWorld().dropItemNaturally(sheep.getLocation(),
				new ItemStack(Material.COOKIE, 1 + (int) (Math.random() * 1.5)));
		case Pig pig -> {
			pig.getWorld().dropItemNaturally(pig.getLocation(),
					new ItemStack(Material.CARROT, 1 + (int) (Math.random() * 1.5)));
			pig.getWorld().dropItemNaturally(pig.getLocation(),
					new ItemStack(Material.POTATO, (int) (Math.random() * 3)));
			pig.getWorld().dropItemNaturally(pig.getLocation(),
					new ItemStack(Material.BEETROOT, (int) (Math.random() * 3)));
		}
		case Wolf wolf -> wolf.getWorld().dropItemNaturally(wolf.getLocation(),
				new ItemStack(Material.BONE_MEAL, 2 + (int) (Math.random() * 5)));
		case Donkey donkey -> donkey.getWorld().dropItemNaturally(donkey.getLocation(), new ItemStack(Material.CHEST));

		case Llama llama ->
			llama.getWorld().dropItemNaturally(llama.getLocation(), new ItemStack(Material.WHITE_CARPET));
		case Zombie zombie -> {
			zombie.getWorld().dropItemNaturally(zombie.getLocation(), new ItemStack(Material.ROTTEN_FLESH, 1));

			zombie.getWorld().dropItemNaturally(zombie.getLocation(),
					new ItemStack(Material.IRON_SWORD, (int) (Math.random() * 1.11)));
		}
		case Cod cod -> cod.getWorld().dropItemNaturally(cod.getLocation(),
				new ItemStack(Material.COD, 1 + (int) (Math.random() * 3)));

		case Salmon salmon -> salmon.getWorld().dropItemNaturally(salmon.getLocation(),
				new ItemStack(Material.SALMON, 1 + (int) (Math.random() * 2)));
		default -> {
			return false;
		}
		}
		return true;
	}

	private boolean isCooldownActive(Entity entity) {
		final NamespacedKey lastUseKey = new NamespacedKey(this, KEY_LAST_USE);
		final long currentTime = System.currentTimeMillis();
		if (entity.getPersistentDataContainer().has(lastUseKey, PersistentDataType.LONG)) {
			long lastUseTime = entity.getPersistentDataContainer().get(lastUseKey, PersistentDataType.LONG);
			long timeSinceLastUse = currentTime - lastUseTime;

			if (timeSinceLastUse < COOLDOWN_TIME_MS) {
				// Dodaj efekt dymka podczas przerwy
				entity.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, // Typ cząsteczki
						entity.getLocation().add(0, 0.5, 0), // Lokalizacja powyżej
						3, // Liczba cząsteczek
						0.35, 0.2, 0.35, // Rozpraszanie (X, Y, Z)
						0.15 // Prędkość
				);
				return true;
			}
		}
		entity.getPersistentDataContainer().set(lastUseKey, PersistentDataType.LONG, currentTime);
		return false;
	}

}
