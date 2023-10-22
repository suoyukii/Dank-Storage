package tfar.dankstorage.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import tfar.dankstorage.init.ModMenuTypes;
import tfar.dankstorage.item.DankItem;
import tfar.dankstorage.utils.DankStats;
import tfar.dankstorage.utils.PickupMode;
import tfar.dankstorage.utils.Utils;
import tfar.dankstorage.world.DankInventoryForge;

public class DankMenu extends AbstractDankMenu {

    public ItemStack bag;

    public DankMenu(MenuType<?> type, int windowId, Inventory inv, DankInventoryForge dankInventoryForge) {
        super(type, windowId, inv, dankInventoryForge);
        Player player = inv.player;
        this.bag = player.getMainHandItem().getItem() instanceof DankItem ? player.getMainHandItem() : player.getOffhandItem();
        addDankSlots();
        addPlayerSlots(inv, inv.selected);
    }

    @Override
    protected DataSlot getServerPickupData() {
        return new DataSlot() {
            @Override
            public int get() {
                return Utils.getPickupMode(bag).ordinal();
            }

            @Override
            public void set(int pValue) {
                Utils.setPickupMode(bag, PickupMode.PICKUP_MODES[pValue]);
            }
        };
    }

    public static DankMenu t1(int id, Inventory inv) {
        return t1s(id, inv,  DockMenu.createDummy(DankStats.one));
    }

    public static DankMenu t2(int id, Inventory inv) {
        return t2s(id, inv,  DockMenu.createDummy(DankStats.two));
    }

    public static DankMenu t3(int id, Inventory inv) {
        return t3s(id, inv,  DockMenu.createDummy(DankStats.three));
    }

    public static DankMenu t4(int id, Inventory inv) {
        return t4s( id, inv,  DockMenu.createDummy(DankStats.four));
    }

    public static DankMenu t5(int id, Inventory inv) {
        return t5s( id, inv,  DockMenu.createDummy(DankStats.five));
    }

    public static DankMenu t6(int id, Inventory inv) {
        return t6s(id, inv,  DockMenu.createDummy(DankStats.six));
    }

    public static DankMenu t7(int id, Inventory inv) {
        return t7s(id, inv, DockMenu.createDummy(DankStats.seven));
    }

    public static DankMenu t1s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_1_container, id, inv, dankInventoryForge);
    }

    public static DankMenu t2s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_2_container, id, inv, dankInventoryForge);
    }

    public static DankMenu t3s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_3_container, id, inv, dankInventoryForge);
    }

    public static DankMenu t4s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_4_container, id, inv, dankInventoryForge);
    }

    public static DankMenu t5s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_5_container, id, inv, dankInventoryForge);
    }

    public static DankMenu t6s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_6_container, id, inv, dankInventoryForge);
    }

    public static DankMenu t7s(int id, Inventory inv, DankInventoryForge dankInventoryForge) {
        return new DankMenu(ModMenuTypes.portable_dank_7_container, id, inv, dankInventoryForge);
    }

    @Override
    public void setFrequency(int freq) {
        Utils.getOrCreateSettings(bag).putInt(Utils.FREQ, freq);
    }

}

