package com.example.backwalk;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class BackwalkCommand extends CommandBase {
    @Override public String getCommandName() { return "backwalk"; }
    @Override public String getCommandUsage(ICommandSender s) { return "/backwalk [on|off]"; }
    @Override public int getRequiredPermissionLevel() { return 0; }
    @Override public boolean canCommandSenderUseCommand(ICommandSender s) { return true; }

    @Override public void processCommand(ICommandSender s, String[] a) {
        if (a.length > 0 && a[0].equalsIgnoreCase("on")) BackwardsWalkMod.enabled = true;
        else if (a.length > 0 && a[0].equalsIgnoreCase("off")) BackwardsWalkMod.enabled = false;
        else BackwardsWalkMod.enabled = !BackwardsWalkMod.enabled;
        s.addChatMessage(new ChatComponentText("§b[Backwalk] §f"
                + (BackwardsWalkMod.enabled ? "ligado" : "desligado")));
    }
}
