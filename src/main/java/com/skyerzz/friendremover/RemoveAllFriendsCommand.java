package com.skyerzz.friendremover;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 26-8-2016.
 */
public class RemoveAllFriendsCommand implements ICommand {

    public RemoveAllFriendsCommand(){
        alias = new ArrayList<String>();
    }

    List<String> alias;

    @Override
    public String getCommandName() {
        return "removeallfriends";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "removeallfriends";
    }

    @Override
    public List<String> getCommandAliases() {
        return alias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==1){
            if(args[0].equalsIgnoreCase("true")){
                new RemoveFriends().start();
                return;
            }
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(ChatFormatting.RED + "[Friend Remover] Are you sure you want to remove ALL YOUR FRIENDS? Use \"/RemoveAllFriends true\" if so. THIS CANNOT BE UNDONE."));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return alias;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
