package me.timeswitcher.lupin.mods;

import java.util.Set;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Sets;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.entity.player.PlayerModelPart;

public class SkinBlinker extends Mod {

    private final Slider delay = new Slider(
            "Delay",
            25.0f,
            0.0f,
            100.0f,
            1000.0f,
            true
    );

    private final Mode blink = new Mode("Blink");
    private final Mode derp = new Mode("Derp");

    private final CheckBox hat = new CheckBox("Hat", true);
    private final CheckBox jacket = new CheckBox("Jacket", true);
    private final CheckBox leftSleeve = new CheckBox("LeftSleeve", true);
    private final CheckBox rightSleeve = new CheckBox("RightSleeve", true);
    private final CheckBox leftPantsLeg = new CheckBox("LeftPantsLeg", true);
    private final CheckBox rightPantsLeg = new CheckBox("RightPantsLeg", true);

    private final PlayerModelPart[] modelParts = {
            PlayerModelPart.HAT,
            PlayerModelPart.JACKET,
            PlayerModelPart.LEFT_SLEEVE,
            PlayerModelPart.RIGHT_SLEEVE,
            PlayerModelPart.LEFT_PANTS_LEG,
            PlayerModelPart.RIGHT_PANTS_LEG
    };

    private final CheckBox[] checkBoxes = {
            hat,
            jacket,
            leftSleeve,
            rightSleeve,
            leftPantsLeg,
            rightPantsLeg
    };

    private final Set<PlayerModelPart> previousModelParts = Sets.newHashSet();

    private final Time toggleTimer = new Time();

    private int part;

    public SkinBlinker(String name) {
        super(
                name,
                Category.PLAYER,
                GLFW.GLFW_KEY_UNKNOWN,
                "Toggles skin layers to create a flashing effect."
        );

        setCurrentMode(blink);

        getModes().add(blink);
        getModes().add(derp);

        getSliders().add(delay);

        getCheckBoxes().add(hat);
        getCheckBoxes().add(jacket);
        getCheckBoxes().add(leftSleeve);
        getCheckBoxes().add(rightSleeve);
        getCheckBoxes().add(leftPantsLeg);
        getCheckBoxes().add(rightPantsLeg);
    }

    @Override
    public void onEnable() {
        previousModelParts.clear();
        previousModelParts.addAll(mc.gameSettings.getModelParts());

        part = 0;
        toggleTimer.reset();
    }

    @Override
    public void onDisable() {
        for (PlayerModelPart modelPart : mc.gameSettings.getModelParts()) {
            mc.gameSettings.setModelPartEnabled(modelPart, false);
        }

        for (PlayerModelPart modelPart : previousModelParts) {
            mc.gameSettings.setModelPartEnabled(modelPart, true);
        }

        part = 0;
        toggleTimer.reset();
    }

    @Override
    public void onUpdate() {
        if (!toggleTimer.isDelayComplete(delay.getReturnValue())) {
            return;
        }

        if (getCurrentMode() == blink) {
            blink();
        } else if (getCurrentMode() == derp) {
            derp();
        }
    }

    private void blink() {
        int checkedParts = 0;

        while (checkedParts < modelParts.length) {
            if (checkBoxes[part].isChecked()) {
                mc.gameSettings.switchModelPartEnabled(modelParts[part]);

                part = (part + 1) % modelParts.length;

                toggleTimer.reset();
                return;
            }

            part = (part + 1) % modelParts.length;
            checkedParts++;
        }

        toggleTimer.reset();
    }

    private void derp() {
        for (int i = 0; i < modelParts.length; i++) {
            if (!checkBoxes[i].isChecked()) {
                continue;
            }

            mc.gameSettings.setModelPartEnabled(
                    modelParts[i],
                    R.nextBoolean()
            );
        }

        toggleTimer.reset();
    }
}