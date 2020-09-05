package io.ace.nordclient.hacks.movement;

import io.ace.nordclient.NordClient;
import io.ace.nordclient.event.UpdateEvent;
import io.ace.nordclient.hacks.Hack;
import io.ace.nordclient.utilz.MathUtil;
import io.ace.nordclient.utilz.clientutil.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.ArrayList;

public class ElytraFly extends Hack {

    Setting speed;
    Setting glide;
    Setting glideSpeed;
    Setting noGlideAFK;
    Setting boost;
    Setting autoTakeoff;
    Setting flyMode;

    public ElytraFly() {
        super("ElytraFly", Category.MOVEMENT);

        NordClient.INSTANCE.settingsManager.rSetting(speed = new Setting("Speed", this, .24, 0, 2, false, "ElytraFlySpeed"));
        NordClient.INSTANCE.settingsManager.rSetting(glide = new Setting("Glide", this, true, "ElytraFlyGlide"));
        NordClient.INSTANCE.settingsManager.rSetting(glideSpeed = new Setting("GlideSpeed", this, 1, 0, 2.5, false, "ElytraFlyGlideSpeed"));
        NordClient.INSTANCE.settingsManager.rSetting(noGlideAFK = new Setting("NoGlideAFK", this, false, "ElytraFlyNoGlideAFK"));
        NordClient.INSTANCE.settingsManager.rSetting(boost = new Setting("Boost", this, true, "ElytraFlyBoost"));
        NordClient.INSTANCE.settingsManager.rSetting(autoTakeoff = new Setting("AutoTakeOff", this, true, "ElytraFlyAutoTakeOff"));

        ArrayList<String> flyModes = new ArrayList<>();
        flyModes.add("2b");
        flyModes.add("Creative");
        flyModes.add("Plane");
        NordClient.INSTANCE.settingsManager.rSetting(flyMode = new Setting("FlyModes", this, "2b", flyModes, "ElytraFlyFlyModes"));



    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if (mc.player.isElytraFlying() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
            final float yaw = GetRotationYawForCalc();
            if (flyMode.getCustomVal().equalsIgnoreCase("2b")) {
                if (mc.player.rotationPitch > 0) {
                    mc.player.motionX -= MathHelper.sin(yaw) * .05 / 10;
                    mc.player.motionZ += MathHelper.cos(yaw) * .05 / 10;


                }
            }
            if (flyMode.getCustomVal().equalsIgnoreCase("creative")) {
                final double[] dir = MathUtil.directionSpeed(speed.getValDouble());

                mc.player.motionX = dir[0];
                mc.player.motionZ = dir[1];

            }

            if (boost.getValBoolean()) {
                if (mc.player.rotationPitch < 0) {
                    mc.player.motionX -= MathHelper.sin(yaw) * .08 / 10;
                    mc.player.motionZ += MathHelper.cos(yaw) * .08 / 10;
                }
            }




                if (glide.getValBoolean()) {
                mc.player.motionY = -glideSpeed.getValDouble() / 100;
            }

        }
        if (autoTakeoff.getValBoolean()) {
            if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isElytraFlying()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));

            }
        }

        if (mc.player.isElytraFlying() && mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -.51;
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {

        }

        if (mc.player.isElytraFlying() && !mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
            if (noGlideAFK.getValBoolean()) {
                mc.player.motionY = 0;
            } else {
                mc.player.motionY = glideSpeed.getValDouble() / 100;
            }
            //
        }

    }
    /*
        thx ionar
    */
    private float GetRotationYawForCalc() {
        float rotationYaw = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float n = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            n = -0.5f;
        }
        else if (mc.player.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }


    @Override
    public String getHudInfo() {
        return flyMode.getCustomVal();
    }
}
