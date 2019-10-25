package com.tfar.dankstorage;

import com.tfar.dankstorage.block.DankBlock;
import com.tfar.dankstorage.block.DankItemBlock;
import com.tfar.dankstorage.capability.CapabilityDankStorage;
import com.tfar.dankstorage.client.RenderDankStorage;
import com.tfar.dankstorage.container.DankContainers;
import com.tfar.dankstorage.network.DankPacketHandler;
import com.tfar.dankstorage.recipe.DankUpgradeRecipes;
import com.tfar.dankstorage.tile.DankTiles;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DankStorage.MODID)
public class DankStorage {

  public static final String MODID = "dankstorage";

  public DankStorage() {

    // Register the setup method for modloading
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
  }

  private void setup(final FMLCommonSetupEvent event) {
    DankPacketHandler.registerMessages(MODID);
    CapabilityDankStorage.register();
  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    public static final List<Block> MOD_BLOCKS = new LinkedList<>();

    @SubscribeEvent
    public static void blocks(final RegistryEvent.Register<Block> event) {
      // register a new block here

      Block.Properties properties = Block.Properties.create(Material.IRON).hardnessAndResistance(1,30);
      for (int i = 1; i < 8; i++) {
        register(new DankBlock(properties), "dank_" + i, event.getRegistry());
      }
    }

    @SubscribeEvent
    public static void items(final RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(ItemGroup.DECORATIONS).maxStackSize(1).setTEISR(RegistryEvents::get);
      for (Block block : MOD_BLOCKS)
        register(new DankItemBlock(block, properties), block.getRegistryName().getPath(), event.getRegistry());
    }

    private static Callable<ItemStackTileEntityRenderer> get() {
      return RenderDankStorage::new;
    }

    @SubscribeEvent
    public static void recipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
      register(new SpecialRecipeSerializer<>(DankUpgradeRecipes.DankUpgradeRecipe1::new),"upgrade1",event.getRegistry());
      register(new SpecialRecipeSerializer<>(DankUpgradeRecipes.DankUpgradeRecipe2::new),"upgrade2",event.getRegistry());
      register(new SpecialRecipeSerializer<>(DankUpgradeRecipes.DankUpgradeRecipe3::new),"upgrade3",event.getRegistry());
      register(new SpecialRecipeSerializer<>(DankUpgradeRecipes.DankUpgradeRecipe4::new),"upgrade4",event.getRegistry());
      register(new SpecialRecipeSerializer<>(DankUpgradeRecipes.DankUpgradeRecipe5::new),"upgrade5",event.getRegistry());
      register(new SpecialRecipeSerializer<>(DankUpgradeRecipes.DankUpgradeRecipe6::new),"upgrade6",event.getRegistry());
    }

    @SubscribeEvent
    public static void containers(final RegistryEvent.Register<ContainerType<?>> event) {
      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();
        return new DankContainers.DankContainer1(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_1_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer1(windowId, inv, inv.player)), "portable_dank_1_container", event.getRegistry());

      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();
        return new DankContainers.DankContainer2(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_2_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer2(windowId, inv, inv.player)), "portable_dank_2_container", event.getRegistry());

      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();
        return new DankContainers.DankContainer3(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_3_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer3(windowId, inv, inv.player)), "portable_dank_3_container", event.getRegistry());

      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();

        return new DankContainers.DankContainer4(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_4_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer4(windowId, inv, inv.player)), "portable_dank_4_container", event.getRegistry());

      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();
        return new DankContainers.DankContainer5(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_5_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer5(windowId, inv, inv.player)), "portable_dank_5_container", event.getRegistry());

      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();
        return new DankContainers.DankContainer6(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_6_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer6(windowId, inv, inv.player)), "portable_dank_6_container", event.getRegistry());

      register(IForgeContainerType.create((windowId, inv, data)
              -> {
        BlockPos pos = data.readBlockPos();
        return new DankContainers.DankContainer7(windowId, inv.player.world, pos, inv, inv.player);
      }), "dank_7_container", event.getRegistry());
      register(IForgeContainerType.create((windowId, inv, data)
              -> new DankContainers.PortableDankContainer7(windowId, inv, inv.player)), "portable_dank_7_container", event.getRegistry());
    }



    @SubscribeEvent
    public static void tiles(final RegistryEvent.Register<TileEntityType<?>> event) {
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile1::new, Objects.dank_1).build(null), "dank_1_tile", event.getRegistry());
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile2::new, Objects.dank_2).build(null), "dank_2_tile", event.getRegistry());
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile3::new, Objects.dank_3).build(null), "dank_3_tile", event.getRegistry());
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile4::new, Objects.dank_4).build(null), "dank_4_tile", event.getRegistry());
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile5::new, Objects.dank_5).build(null), "dank_5_tile", event.getRegistry());
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile6::new, Objects.dank_6).build(null), "dank_6_tile", event.getRegistry());
      register(TileEntityType.Builder.create(DankTiles.DankStorageTile7::new, Objects.dank_7).build(null), "dank_7_tile", event.getRegistry());

    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
      registry.register(obj.setRegistryName(new ResourceLocation(MODID, name)));
      if (obj instanceof Block) MOD_BLOCKS.add((Block) obj);
    }
  }

  public static final ClientConfig CLIENT;
  public static final ForgeConfigSpec CLIENT_SPEC;

  public static final ServerConfig SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  static {
    final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    CLIENT_SPEC = specPair.getRight();
    CLIENT = specPair.getLeft();
    final Pair<ServerConfig, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    SERVER_SPEC = specPair2.getRight();
    SERVER = specPair2.getLeft();
  }

  public static class ClientConfig {
    public static ForgeConfigSpec.BooleanValue preview;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
      builder.push("client");
      preview = builder
              .comment("Whether to display the preview of the item in the dank, disable if you have optifine")
              .define("preview",true);
      builder.pop();
    }
  }

  public static class ServerConfig {
    public static ForgeConfigSpec.IntValue stacklimit1;
    public static ForgeConfigSpec.IntValue stacklimit2;
    public static ForgeConfigSpec.IntValue stacklimit3;
    public static ForgeConfigSpec.IntValue stacklimit4;
    public static ForgeConfigSpec.IntValue stacklimit5;
    public static ForgeConfigSpec.IntValue stacklimit6;
    public static ForgeConfigSpec.IntValue stacklimit7;
    public static ForgeConfigSpec.BooleanValue useShareTag;


    public ServerConfig(ForgeConfigSpec.Builder builder) {
      builder.push("server");
      stacklimit1 = builder.
              comment("Stack limit of first dank storage")
              .defineInRange("stacklimit1",256,1,Integer.MAX_VALUE);
      stacklimit2 = builder.
              comment("Stack limit of second dank storage")
              .defineInRange("stacklimit2",1024,1,Integer.MAX_VALUE);
      stacklimit3 = builder.
              comment("Stack limit of third dank storage")
              .defineInRange("stacklimit3",4096,1,Integer.MAX_VALUE);
      stacklimit4 = builder.
              comment("Stack limit of fourth dank storage")
              .defineInRange("stacklimit4",16384,1,Integer.MAX_VALUE);
      stacklimit5 = builder.
              comment("Stack limit of fifth dank storage")
              .defineInRange("stacklimit5",65536,1,Integer.MAX_VALUE);
      stacklimit6 = builder.
              comment("Stack limit of sixth dank storage")
              .defineInRange("stacklimit6",262144,1,Integer.MAX_VALUE);
      stacklimit7 = builder.
              comment("Stack limit of seventh dank storage")
              .defineInRange("stacklimit7",Integer.MAX_VALUE,1,Integer.MAX_VALUE);
      useShareTag = builder.
              comment("Use Share Tag instead of full NBT to reduce the chance of NBT overflow.  Warning: this will cause the Dank\n" +
                      "Storage to wipe it's items in Creative Mode.  There is nothing I can do about this as it is a vanilla bug.")
              .define("useShareTag",false);
      builder.pop();
    }
  }

  @ObjectHolder(MODID)
  public static class Objects {

    public static final Block dank_1 = null;
    public static final Block dank_2 = null;
    public static final Block dank_3 = null;
    public static final Block dank_4 = null;
    public static final Block dank_5 = null;
    public static final Block dank_6 = null;
    public static final Block dank_7 = null;

    public static final ContainerType<DankContainers.DankContainer1> dank_1_container = null;
    public static final ContainerType<DankContainers.DankContainer2> dank_2_container = null;
    public static final ContainerType<DankContainers.DankContainer3> dank_3_container = null;
    public static final ContainerType<DankContainers.DankContainer4> dank_4_container = null;
    public static final ContainerType<DankContainers.DankContainer5> dank_5_container = null;
    public static final ContainerType<DankContainers.DankContainer6> dank_6_container = null;
    public static final ContainerType<DankContainers.DankContainer7> dank_7_container = null;


    public static final ContainerType<DankContainers.PortableDankContainer1> portable_dank_1_container = null;
    public static final ContainerType<DankContainers.PortableDankContainer2> portable_dank_2_container = null;
    public static final ContainerType<DankContainers.PortableDankContainer3> portable_dank_3_container = null;
    public static final ContainerType<DankContainers.PortableDankContainer4> portable_dank_4_container = null;
    public static final ContainerType<DankContainers.PortableDankContainer5> portable_dank_5_container = null;
    public static final ContainerType<DankContainers.PortableDankContainer6> portable_dank_6_container = null;
    public static final ContainerType<DankContainers.PortableDankContainer7> portable_dank_7_container = null;


    public static final TileEntityType<DankTiles.DankStorageTile1> dank_1_tile = null;
    public static final TileEntityType<DankTiles.DankStorageTile2> dank_2_tile = null;
    public static final TileEntityType<DankTiles.DankStorageTile3> dank_3_tile = null;
    public static final TileEntityType<DankTiles.DankStorageTile4> dank_4_tile = null;
    public static final TileEntityType<DankTiles.DankStorageTile5> dank_5_tile = null;
    public static final TileEntityType<DankTiles.DankStorageTile6> dank_6_tile = null;
    public static final TileEntityType<DankTiles.DankStorageTile7> dank_7_tile = null;

    public static final IRecipeSerializer<?> upgrade1 = null;
    public static final IRecipeSerializer<?> upgrade2 = null;
    public static final IRecipeSerializer<?> upgrade3 = null;
    public static final IRecipeSerializer<?> upgrade4 = null;
    public static final IRecipeSerializer<?> upgrade5 = null;
    public static final IRecipeSerializer<?> upgrade6 = null;
  }
}
