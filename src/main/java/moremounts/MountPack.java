package moremounts;

import moremounts.items.BatMountItem;
import moremounts.items.CrocodileMountItem;
import moremounts.items.SpiderMountItem;
import moremounts.mobs.BatMountMob;
import moremounts.mobs.CrocodileMountMob;
import moremounts.mobs.SpiderMountMob;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.sound.gameSound.GameSound;
import necesse.entity.mobs.hostile.bosses.EvilsProtectorMob;
import necesse.entity.mobs.hostile.bosses.QueenSpiderMob;
import necesse.entity.mobs.hostile.bosses.SwampGuardianHead;
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
        ItemRegistry.registerItem("crocodilemount", new CrocodileMountItem(), 35000, true);

        MobRegistry.registerMob("batmount", BatMountMob.class, false);
        MobRegistry.registerMob("spidermount", SpiderMountMob.class, false);
        MobRegistry.registerMob("crocodilemount", CrocodileMountMob.class, false);

        EvilsProtectorMob.lootTable.items.add(new ChanceLootItem(0.1f, "batmount"));
        QueenSpiderMob.lootTable.items.add(new ChanceLootItem(0.1f, "spidermount"));
        SwampGuardianHead.lootTable.items.add(new ChanceLootItem(0.1f, "crocodilemount"));
    }

    public void initResources() {

        toggleNightVisionEffect = GameSound.fromFile("togglenightvision");

        BatMountMob.texture = GameTexture.fromFile("mobs/batmount");
        BatMountMob.textureShadow = GameTexture.fromFile("mobs/batmount_shadow");
        BatMountMob.textureMask = GameTexture.fromFile("mobs/spidermount_mask");

        SpiderMountMob.texture = GameTexture.fromFile("mobs/spidermount");
        SpiderMountMob.textureShadow = GameTexture.fromFile("mobs/spidermount_shadow");
        SpiderMountMob.textureMask = GameTexture.fromFile("mobs/spidermount_mask");

        CrocodileMountMob.texture = GameTexture.fromFile("mobs/crocodilemount");
    }
}
