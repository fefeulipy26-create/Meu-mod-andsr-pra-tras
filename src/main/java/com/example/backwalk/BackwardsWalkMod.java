package com.example.backwalk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = BackwardsWalkMod.MODID, name = "Backwards Walk", version = "1.0",
        clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class BackwardsWalkMod {
    public static final String MODID = "backwalk";

    public static boolean enabled = true;

    private static final double BACKWARD_COS = -0.3;
    private static final double MIN_SPEED = 0.01;

    private EntityPlayer savedPlayer;
    private float savedLimbSwing;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new BackwalkCommand());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPre(RenderPlayerEvent.Pre e) {
        if (!enabled || e.isCanceled()) return;
        EntityPlayer p = e.entityPlayer;

        double dx = p.posX - p.prevPosX;
        double dz = p.posZ - p.prevPosZ;
        double speed = Math.sqrt(dx * dx + dz * dz);
        if (speed < MIN_SPEED) return;

        double yaw = Math.toRadians(p.renderYawOffset);
        double fx = -Math.sin(yaw);
        double fz = Math.cos(yaw);
        double cos = (dx * fx + dz * fz) / speed;
        if (cos >= BACKWARD_COS) return;

        savedPlayer = p;
        savedLimbSwing = p.limbSwing;
        p.limbSwing = -p.limbSwing + 2f * p.limbSwingAmount * (1f - e.partialRenderTick);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPost(RenderPlayerEvent.Post e) {
        restore();
    }

    private void restore() {
        if (savedPlayer != null) {
            savedPlayer.limbSwing = savedLimbSwing;
            savedPlayer = null;
        }
    }
}
