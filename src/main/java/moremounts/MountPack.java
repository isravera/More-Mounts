package moremounts;

import moremounts.items.BatMountItem;
import moremounts.items.SpiderMountItem;
import moremounts.mobs.BatMountMob;
import moremounts.mobs.SpiderMountMob;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.sound.gameSound.GameSound;
import necesse.entity.mobs.hostile.ZombieMob;
import necesse.entity.mobs.hostile.bosses.EvilsProtectorMob;
import necesse.entity.mobs.hostile.bosses.QueenSpiderMob;
import necesse.gfx.forms.Form;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;

@ModEntry
public class MountPack {

    public static Form toggleNightVision;
    public static GameSound toggleNightVisionEffect;

    public void init() {
        System.out.println("We have more mounts!");

        ItemRegistry.registerItem("batmount", new BatMountItem(), 35000, true);
        ItemRegistry.registerItem("spidermount", new SpiderMountItem(), 35000, true);

        MobRegistry.registerMob("batmount", BatMountMob.class, false);
        MobRegistry.registerMob("spidermount", SpiderMountMob.class, false);

        EvilsProtectorMob.lootTable.items.add(new ChanceLootItem(0.1f, "batmount"));
        QueenSpiderMob.lootTable.items.add(new ChanceLootItem(0.1f, "spidermount"));

        ZombieMob.lootTable.items.add(new ChanceLootItem(1, "partyhat"));
        ZombieMob.lootTable.items.add(new ChanceLootItem(1, "pumpkinmask"));
        ZombieMob.lootTable.items.add(new ChanceLootItem(1, "vulturemask"));
    }

    public void initResources() {

        toggleNightVisionEffect = GameSound.fromFile("togglenightvision");

        BatMountMob.texture = GameTexture.fromFile("mobs/batmount");
        BatMountMob.textureShadow = GameTexture.fromFile("mobs/batmount_shadow");
        BatMountMob.textureMask = GameTexture.fromFile("mobs/spidermount_mask");

        SpiderMountMob.texture = GameTexture.fromFile("mobs/spidermount");
        SpiderMountMob.textureShadow = GameTexture.fromFile("mobs/spidermount_shadow");
        SpiderMountMob.textureMask = GameTexture.fromFile("mobs/spidermount_mask");
    }
}
