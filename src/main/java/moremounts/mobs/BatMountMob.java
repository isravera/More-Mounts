package moremounts.mobs;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import moremounts.MountPack;
import moremounts.form.ToggleNightVisionComponent;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.world.WorldEntity;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.summon.summonFollowingMob.mountFollowingMob.MountFollowingMob;
import necesse.gfx.GameResources;
import necesse.gfx.PlayerSprite;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.forms.Form;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.CollisionFilter;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.util.stream.Stream;

public class BatMountMob extends MountFollowingMob {

    public static GameTexture texture;
    public static GameTexture textureShadow;
    public static GameTexture textureMask;

    public static boolean enableNightVision;

    public BatMountMob() {
        super(100);
        setSpeed(110);
        setFriction(1);
        setSwimSpeed(1);

        collision = new Rectangle(-10, -7, 20, 14);
        hitBox = new Rectangle(-14, -14, 28, 28);
        selectBox = new Rectangle(-15, -15, 30, 30);
        overrideMountedWaterWalking = true;
    }

    @Override
    public void serverTick() {
        super.serverTick();

        if(!isMounted()) {
            moveX = 0;
            moveY = 0;
        }
    }

    @Override
    public void clientTick() {
        super.clientTick();

        PlayerMob player = getFollowingPlayer();

        boolean isNight = false;

        if(player != null) {
            WorldEntity playerWorld = player.getWorldEntity();
            ToggleNightVisionComponent.enableSound = playerWorld.isNight();
            isNight = playerWorld.isNight();
        }


        if(player != null && player.getLevel() != null && player == getLevel().getClient().getPlayer() && isMounted() && enableNightVision) {

            if(GameResources.debugColorShader != null && isNight) {

                GameResources.debugColorShader.use();
                GameResources.debugColorShader.pass1f("green", 1.6f);
                GameResources.debugColorShader.pass1f("contrast", 0.6f);
                GameResources.debugColorShader.stop();
            }

        } else {

            if(GameResources.debugColorShader != null) {

                GameResources.debugColorShader.use();
                GameResources.debugColorShader.pass1f("green", 1);
                GameResources.debugColorShader.pass1f("contrast", 1);
                GameResources.debugColorShader.stop();
            }
        }

        MountPack.toggleNightVision.setHidden(player != null && player.getLevel() == null || player != getLevel().getClient().getPlayer() || !isMounted());
    }

    @Override
    public int getFlyingHeight() {
        return 1000;
    }

    @Override
    public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        PlayerMob player = getFollowingPlayer();
        GameLight light = level.getLightLevel(x / 32, y / 32);
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 40;
        Point sprite;

        if(player != null) {
            sprite = getAnimSprite(x, y, player.getDir());
        } else {
            sprite = getAnimSprite(x, y, 1);
        }

        drawY += getBobbing(x, y);

        final TextureDrawOptionsEnd options = texture.initDraw().sprite(sprite.x, sprite.y, 64).light(light).pos(drawX, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) { }

            @Override
            public void drawBehindRider(TickManager tickManager) {
                options.draw();
            }
        });

        topList.add(tm -> {
            options.draw();

            if(player != null && isMounted()) {

                DrawOptions playerSprite = PlayerSprite.getDrawOptions(player, x, y, light, camera, null);
                playerSprite.draw();
            }
        });

        addShadowDrawables(topList, x, y, light, camera);
    }

    @Override
    public Point getAnimSprite(int x, int y, int dir) {
        return new Point((int)(getWorldEntity().getTime() / getRockSpeed()) % 4, dir % 4);
    }

    @Override
    public TextureDrawOptions getShadowDrawOptions(int x, int y, GameLight light, GameCamera camera) {
        GameTexture shadowTexture = textureShadow;
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 40;
        drawY += getBobbing(x, y);
        drawY += 70;
        PlayerMob player = getFollowingPlayer();

        if(player != null) {
            return shadowTexture.initDraw().sprite(0, player.getDir(), 64).light(light).pos(drawX, drawY);
        } else {
            return shadowTexture.initDraw().sprite(0, 1, 64).light(light).pos(drawX, drawY);
        }
    }

    @Override
    public int getRockSpeed() {
        return 500;
    }

    @Override
    public int getWaterRockSpeed() {
        return  500;
    }

    @Override
    public Point getSpriteOffset(int spriteX, int spriteY) {
        Point point = new Point(0, 0);

        if(spriteX == 0 || spriteX == 2) {
            point.y = -1;
        }

        point.x += getRiderDrawXOffset();
        point.y += getRiderDrawYOffset();

        return point;
    }

    @Override
    public int getRiderDrawYOffset() {
        return -8;
    }

    @Override
    public int getRiderArmSpriteX() {
        return 1;
    }

    @Override
    public int getRiderDir(int startDir) {
        return (startDir) % 4;
    }

    @Override
    public GameTexture getRiderMask() {
        return textureMask;
    }

    @Override
    public int getRiderMaskYOffset() {
        return -9;
    }

    @Override
    public CollisionFilter getLevelCollisionFilter() {

        PlayerMob player = getFollowingPlayer();

        if(player != null) {

            if(player.getLevel() != null && player.getLevel().isCave) {
                return super.getLevelCollisionFilter().overrideFilter(tp -> ((tp.object()).object.isWall || (tp.object()).object.isRock));
            }
        }

        return super.getLevelCollisionFilter().overrideFilter(tp -> ((tp.object()).object.isWall));
    }

    @Override
    public Stream<ModifierValue<?>> getDefaultRiderModifiers() {
        return Stream.of(
            new ModifierValue<>(BuffModifiers.TRAVEL_DISTANCE, 3),
            new ModifierValue<>(BuffModifiers.WATER_WALKING, true)
        );
    }
}
