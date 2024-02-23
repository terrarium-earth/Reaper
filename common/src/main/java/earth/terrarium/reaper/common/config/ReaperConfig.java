package earth.terrarium.reaper.common.config;

import com.teamresourceful.resourcefulconfig.common.annotations.Comment;
import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;
import earth.terrarium.reaper.Reaper;

@Config(value = Reaper.MODID)
@WebInfo(
        title = "Reaper",
        description = "Reaper is a mod that adds a generator that can kill mobs and generate energy."
)
public final class ReaperConfig {
    @ConfigEntry(id = "soul_beacon_energy_cap", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_energy_cap")
    @Comment(value = "The maximum amount of energy the soul beacon can store.")
    public static int soulBeaconEnergyCap = 1000000;

    @ConfigEntry(id = "soul_beacon_energy_prod", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_energy_prod")
    @Comment(value = "The amount of energy the soul beacon produces per tick.")
    public static int genEnergyCap = 1000000;

    @ConfigEntry(id = "soul_beacon_max_range", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_max_range")
    @Comment(value = "The maximum range the soul beacon can affect mobs.")
    public static int genEnergyProd = 80;

    @ConfigEntry(id = "soul_beacon_energy_prod_with_rune", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_energy_prod_with_rune")
    @Comment(value = "The amount of energy the soul beacon produces per tick with the rune.")
    public static int genEnergyProdWithRune = 160;

    @ConfigEntry(id = "soul_beacon_max_range", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_max_range")
    @Comment(value = "The maximum range the soul beacon can affect mobs.")
    public static int genMaxRange = 5;

    @ConfigEntry(id = "soul_beacon_max_range_with_rune", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_max_range_with_rune")
    @Comment(value = "The maximum range the soul beacon can affect mobs with the rune.")
    public static int genMaxRangeWithRune = 8;

    @ConfigEntry(id = "soul_beacon_damage", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_damage")
    @Comment(value = "The amount of damage the soul beacon deals to mobs. If this value is -1, the mob will die instantly.")
    public static int genDamage = 5;

    @ConfigEntry(id = "soul_beacon_damage_with_rune", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_damage_with_rune")
    @Comment(value = "The amount of damage the soul beacon deals to mobs with the rune. If this value is -1, the mob will die instantly.")
    public static int genDamageWithRune = -1;

    @ConfigEntry(id = "soul_beacon_cooldown", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_cooldown")
    @Comment(value = "The cooldown of the soul beacon.")
    public static int genCooldown = 120;

    @ConfigEntry(id = "soul_beacon_cooldown_with_rune", type = EntryType.INTEGER, translation = "config.reaper.soul_beacon_cooldown_with_rune")
    @Comment(value = "The cooldown of the soul beacon with the rune.")
    public static int genCooldownWithRune = 60;
}
