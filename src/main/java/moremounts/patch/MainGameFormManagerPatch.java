package moremounts.patch;

import moremounts.MountPack;
import moremounts.form.ToggleNightVisionComponent;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.MainGameFormManager;
import net.bytebuddy.asm.Advice;

public class MainGameFormManagerPatch {

    public static int toolbarHeight;
    public static MainGameFormManager formManager;

    @ModMethodPatch(target = MainGameFormManager.class, name = "setup", arguments = {})
    public static class setupPatch {

        @Advice.OnMethodExit
        public static void onExit(@Advice.This MainGameFormManager setupFormManager) {

            toolbarHeight = setupFormManager.toolbar.getHeight();
            formManager = setupFormManager;

            MountPack.toggleNightVision = setupFormManager.addComponent(new Form("toggle", 200, 30));
            MountPack.toggleNightVision.drawBase = false;
            MountPack.toggleNightVision.addComponent(new ToggleNightVisionComponent(MountPack.toggleNightVision.getWidth() - 10));
            MountPack.toggleNightVision.setHidden(true);
        }
    }

    @ModMethodPatch(target = MainGameFormManager.class, name = "frameTick", arguments = {})
    public static class frameTickPatch {

        @Advice.OnMethodExit
        public static void onExit() {

            GameWindow window = WindowManager.getWindow();

            if(formManager.inventory.isHidden()) {
                MountPack.toggleNightVision.setPosMiddle(window.getHudWidth() / 2, window.getHudHeight() - formManager.toolbar.getHeight() - 50);
            } else {
                MountPack.toggleNightVision.setPosMiddle(window.getHudWidth() / 2, window.getHudHeight() - formManager.toolbar.getHeight() - 230);
            }
        }
    }

    @ModMethodPatch(target = MainGameFormManager.class, name = "onWindowResized", arguments = {})
    public static  class windowResizePatch {
        @Advice.OnMethodExit
        public static void onExit() {

            if(MountPack.toggleNightVision == null) return;

            GameWindow window = WindowManager.getWindow();

            MountPack.toggleNightVision.setPosMiddle(window.getHudWidth() / 2, window.getHudHeight() - toolbarHeight - 50);
        }
    }
}
