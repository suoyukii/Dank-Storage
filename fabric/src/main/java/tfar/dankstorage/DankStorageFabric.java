package tfar.dankstorage;

import com.mojang.brigadier.CommandDispatcher;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import tfar.dankstorage.blockentity.DockBlockEntity;
import tfar.dankstorage.command.DankCommands;
import tfar.dankstorage.init.ModBlockEntityTypes;
import tfar.dankstorage.inventory.api.DankInventorySlotWrapper;
import tfar.dankstorage.network.DankPacketHandler;
import tfar.dankstorage.platform.ClothConfig;
import tfar.dankstorage.world.DankInventoryFabric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DankStorageFabric implements ModInitializer,
        ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopped, CommandRegistrationCallback {


    public static ClothConfig CONFIG;


    @Override
    public void onInitialize() {
        AutoConfig.register(ClothConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ClothConfig.class).getConfig();
        ServerLifecycleEvents.SERVER_STARTED.register(this);
        ServerLifecycleEvents.SERVER_STOPPED.register(this);
        CommandRegistrationCallback.EVENT.register(this);

        ItemStorage.SIDED.registerForBlockEntity(DankStorageFabric::getStorage,ModBlockEntityTypes.dock);
        DankStorage.init();
        DankPacketHandler.registerPackets();
    }


    @Override
    public void onServerStarted(MinecraftServer server) {
        DankStorage.onServerStart(server);
    }

    @Override
    public void onServerStopped(MinecraftServer server) {
        DankStorage.onServerShutDown(server);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandbuildcontext, CommandSelection commandselection) {
        DankCommands.register(dispatcher);
    }

    static Map<BlockEntity, CombinedStorage<ItemVariant,DankInventorySlotWrapper>> MAP = new HashMap<>();

    //item api

    public static CombinedStorage<ItemVariant,DankInventorySlotWrapper> getStorage(DockBlockEntity dockBlockEntity, Direction direction) {

        DankInventoryFabric dankInventoryFabric = (DankInventoryFabric) dockBlockEntity.getInventory();

        CombinedStorage<ItemVariant,DankInventorySlotWrapper> storage = MAP.get(dockBlockEntity);

        if (storage != null && storage.parts.size() != dankInventoryFabric.slotCount()) {
            storage = null;
        }
        if (storage == null) {
            storage = create(dankInventoryFabric);
            MAP.put(dockBlockEntity,storage);
        }
        return storage;
    }


    public static CombinedStorage<ItemVariant,DankInventorySlotWrapper> create(DankInventoryFabric dankInventoryFabric) {
        int slots = dankInventoryFabric.slotCount();

        List<DankInventorySlotWrapper> storages = new ArrayList<>();

        for (int i = 0 ;i < slots;i++) {
            DankInventorySlotWrapper storage = new DankInventorySlotWrapper(dankInventoryFabric,i);
            storages.add(storage);
        }

        return new CombinedStorage<>(storages);
    }

    /*

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
              .define("preview", true);
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
    public static ForgeConfigSpec.ConfigValue<List<String>> convertible_tags;

    public static final List<String> defaults = Lists.newArrayList(
            "forge:ingots/iron",
            "forge:ingots/gold",
            "forge:ores/coal",
            "forge:ores/diamond",
            "forge:ores/emerald",
            "forge:ores/gold",
            "forge:ores/iron",
            "forge:ores/lapis",
            "forge:ores/redstone",

            "forge:gems/amethyst",
            "forge:gems/peridot",
            "forge:gems/ruby",

            "forge:ingots/copper",
            "forge:ingots/lead",
            "forge:ingots/nickel",
            "forge:ingots/silver",
            "forge:ingots/tin",

            "forge:ores/copper",
            "forge:ores/lead",
            "forge:ores/ruby",
            "forge:ores/silver",
            "forge:ores/tin");

    public ServerConfig(ForgeConfigSpec.Builder builder) {
      builder.push("server");
      stacklimit1 = builder.
              comment("Stack limit of first dank storage")
              .defineInRange("stacklimit1", 256, 1, Integer.MAX_VALUE);
      stacklimit2 = builder.
              comment("Stack limit of second dank storage")
              .defineInRange("stacklimit2", 1024, 1, Integer.MAX_VALUE);
      stacklimit3 = builder.
              comment("Stack limit of third dank storage")
              .defineInRange("stacklimit3", 4096, 1, Integer.MAX_VALUE);
      stacklimit4 = builder.
              comment("Stack limit of fourth dank storage")
              .defineInRange("stacklimit4", 16384, 1, Integer.MAX_VALUE);
      stacklimit5 = builder.
              comment("Stack limit of fifth dank storage")
              .defineInRange("stacklimit5", 65536, 1, Integer.MAX_VALUE);
      stacklimit6 = builder.
              comment("Stack limit of sixth dank storage")
              .defineInRange("stacklimit6", 262144, 1, Integer.MAX_VALUE);
      stacklimit7 = builder.
              comment("Stack limit of seventh dank storage")
              .defineInRange("stacklimit7", Integer.MAX_VALUE, 1, Integer.MAX_VALUE);

      convertible_tags = builder.
              comment("Tags that are eligible for conversion, input as a list of resourcelocation, eg 'forge:ingots/iron'")
              .define("convertible tags", defaults);
      builder.pop();
    }
  }*/
}
