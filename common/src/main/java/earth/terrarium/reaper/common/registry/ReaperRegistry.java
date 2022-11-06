package earth.terrarium.reaper.common.registry;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.block.ReaperGeneratorBlock;
import earth.terrarium.reaper.common.block.ReaperGeneratorMenu;
import earth.terrarium.reaper.common.block.SelfIdentifyingRuneBlock;
import earth.terrarium.reaper.common.block.SoulBeaconBlock;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import earth.terrarium.reaper.common.blockentity.SelfIdentifyingRuneBlockEntity;
import earth.terrarium.reaper.common.blockentity.SoulBeaconBlockEntity;
import earth.terrarium.reaper.common.util.Utils;
import me.codexadrian.spirit.Spirit;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
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
    public static RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, Reaper.MODID);
    public static RegistryHolder<Block> BLOCKS = new RegistryHolder<>(Registry.BLOCK, Reaper.MODID);
    public static RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, Reaper.MODID);

    public static final RegistryHolder<SoundEvent> SOUNDS = new RegistryHolder<>(Registry.SOUND_EVENT, Reaper.MODID);

    public static final RegistryHolder<MenuType<?>> MENUS = new RegistryHolder<>(Registry.MENU, Reaper.MODID);

    public static final Supplier<Block> REAPER_GEN_BLOCK = registerBlockWithItem("reaper_generator", () -> new ReaperGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final Supplier<Block> SOUL_BEACON = registerBlockWithItem("soul_beacon", () -> new SoulBeaconBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final Supplier<BlockEntityType<?>> REAPER_GEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("reaper_generator", () -> RegistryHelpers.createBlockEntityType(ReaperGeneratorBlockEntity::new, REAPER_GEN_BLOCK.get()));
    public static final Supplier<BlockEntityType<?>> SOUL_BEACON_BLOCK_ENTITY = BLOCK_ENTITIES.register("soul_beacon", () -> RegistryHelpers.createBlockEntityType(SoulBeaconBlockEntity::new, SOUL_BEACON.get()));
    public static final Supplier<SoundEvent> REAPER_GEN_SOUND = SOUNDS.register("block.reaper.slam", () -> new SoundEvent(new ResourceLocation(Reaper.MODID, "block.reaper.slam")));

    public static final Supplier<MenuType<ReaperGeneratorMenu>> REAPER_GEN_MENU = MENUS.register("reaper_generator_menu", () -> RegistryHelpers.createMenuType(ReaperGeneratorMenu::new));

    public static final DamageSource REAPER_DAMAGE = new DamageSource("reaper").bypassArmor().bypassEnchantments().setMagic().setNoAggro();

    public static final Supplier<Item> RUNE_MORE_ENERGY = ITEMS.register("rune_reficiat", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_INSTADEATH = ITEMS.register("rune_obitus", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_PLAYER = ITEMS.register("rune_humano", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_RANGE = ITEMS.register("rune_dilato", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_SPEED = ITEMS.register("rune_velocitas", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
    public static final Supplier<Item> RUNE_SPIRIT = ITEMS.register("rune_spiritus", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_CATALYST = ITEMS.register("soul_catalyst", () -> new Item(new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Block> RUNE_BLOCK_PERSONAL_FILTER = registerBlockWithItem("rune_block_possessio", () -> new SelfIdentifyingRuneBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<BlockEntityType<SelfIdentifyingRuneBlockEntity>> RUNE_BLOCK_PERSONAL_FILTER_ENTITY = BLOCK_ENTITIES.register("rune_block_possessio", () -> RegistryHelpers.createBlockEntityType(SelfIdentifyingRuneBlockEntity::new, RUNE_BLOCK_PERSONAL_FILTER.get()));
    public static final Supplier<Block> RUNE_BLOCK_HOSTILE_FILTER = registerBlockWithItem("rune_block_hostilis", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_NEUTRAL_FILTER = registerBlockWithItem("rune_block_beastia", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_RANGE = registerBlockWithItem("rune_block_dilato", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_DOUBLE = registerBlockWithItem("rune_block_duplici", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> RUNE_BLOCK_EFFICIENCY = registerBlockWithItem("rune_block_efficacia", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));

    public static final TagKey<Item> RUNES = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Reaper.MODID, "rune"));

    public static Supplier<Block> registerBlockWithItem(String name, Supplier<Block> block) {
        var blockRegistry = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(blockRegistry.get(), new Item.Properties().tab(Spirit.SPIRIT).rarity(Rarity.RARE)));
        return blockRegistry;
    }

    public static void init() {
    }

    public static void register() {
        ITEMS.initialize();
        BLOCKS.initialize();
        BLOCK_ENTITIES.initialize();
        SOUNDS.initialize();
        MENUS.initialize();
    }

    public static DamageSource playerSource(ServerLevel serverLevel) {
        return new EntityDamageSource("reaper", Utils.makeFakePlayer(serverLevel)).bypassArmor().bypassEnchantments().setMagic().setNoAggro();
    }

}
