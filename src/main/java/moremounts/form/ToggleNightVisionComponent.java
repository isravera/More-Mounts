package moremounts.form;

import moremounts.MountPack;
import moremounts.mobs.BatMountMob;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.ui.ButtonColor;

public class ToggleNightVisionComponent extends FormLocalTextButton {

    public static boolean enableSound = false;

    int counter = 1;

    public ToggleNightVisionComponent(int width) {
        super("ui", "toggleNV", 5, 5, width, FormInputSize.SIZE_24, ButtonColor.BASE);
        onClicked(event -> {

            if(enableSound && counter % 2 != 0) {
                SoundManager.playSound(MountPack.toggleNightVisionEffect, SoundEffect.globalEffect().volume(0.6f));
            }

            System.out.println(enableSound);

            counter++;

            toggleNightVision();
        });
    }

    public void toggleNightVision() {
        BatMountMob.enableNightVision = !BatMountMob.enableNightVision;
    }
}
