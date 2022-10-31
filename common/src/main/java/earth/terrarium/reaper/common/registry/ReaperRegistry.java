package earth.terrarium.reaper.common.registry;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.block.SoulBeaconBlock;
import earth.terrarium.reaper.common.block.ReaperGeneratorBlock;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import earth.terrarium.reaper.common.blockentity.SoulBeaconBlockEntity;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import me.codexadrian.spirit.Spirit;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ReaperRegistry {
    public static RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, Reaper.MOD_ID);
    public static RegistryHolder<Block> BLOCKS = new RegistryHolder<>(Registry.BLOCK, Reaper.MOD_ID);
    public static RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, Reaper.MOD_ID);
    //registry holder of sound events
    public static final RegistryHolder<SoundEvent> SOUNDS = new RegistryHolder<>(Registry.SOUND_EVENT, Reaper.MOD_ID);

    public static final RegistryHolder<MenuType<?>> MENUS = new RegistryHolder<>(Registry.MENU, Reaper.MOD_ID);

    public static final Supplier<Block> REAPER_GEN_BLOCK = registerBlockWithItem("reaper_generator", () -> new ReaperGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final Supplier<Block> SOUL_BEACON = registerBlockWithItem("soul_beacon", () -> new SoulBeaconBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final Supplier<BlockEntityType<?>> REAPER_GEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("reaper_generator", () -> RegistryHelpers.createBlockEntityType(ReaperGeneratorBlockEntity::new, REAPER_GEN_BLOCK.get()));
    public static final Supplier<BlockEntityType<?>> SOUL_BEACON_BLOCK_ENTITY = BLOCK_ENTITIES.register("soul_beacon", () -> RegistryHelpers.createBlockEntityType(SoulBeaconBlockEntity::new, SOUL_BEACON.get()));
    public static final Supplier<SoundEvent> REAPER_GEN_SOUND = SOUNDS.register("block.reaper.slam", () -> new SoundEvent(new ResourceLocation(Reaper.MOD_ID, "block.reaper.slam")));

    public static final Supplier<MenuType<ReaperGeneratorMenu>> REAPER_GEN_MENU = MENUS.register("reaper_generator_menu", () -> RegistryHelpers.createMenuType(ReaperGeneratorMenu::new));

    public static final DamageSource REAPER_DAMAGE = new DamageSource("reaper").bypassArmor().bypassEnchantments().setMagic().setNoAggro();

    public static final Supplier<Item> RUNE_FILTER = ITEMS.register("rune_colum", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_INSTADEATH = ITEMS.register("rune_obitus", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_PLAYER = ITEMS.register("rune_humano", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_RANGE = ITEMS.register("rune_dilato", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_SPEED = ITEMS.register("rune_velocitas", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_SPIRIT = ITEMS.register("rune_spiritus", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Block> RUNE_BLOCK_PERSONAL_FILTER = registerBlockWithItem("rune_block_possessio", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_HOSTILE_FILTER = registerBlockWithItem("rune_block_hostilis", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_NEUTRAL_FILTER = registerBlockWithItem("rune_block_beastia", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_RANGE = registerBlockWithItem("rune_block_dilato", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_DOUBLE = registerBlockWithItem("rune_block_duplici", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_EFFICIENCY = registerBlockWithItem("rune_block_efficacia", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));

    public static Supplier<Block> registerBlockWithItem(String name, Supplier<Block> block) {
        var blockRegistry = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(blockRegistry.get(), new Item.Properties().tab(Spirit.SPIRIT)));
        return blockRegistry;
    }

    public static void init() {}

    public static void register() {
        ITEMS.initialize();
        BLOCKS.initialize();
        BLOCK_ENTITIES.initialize();
        SOUNDS.initialize();
    }
}
