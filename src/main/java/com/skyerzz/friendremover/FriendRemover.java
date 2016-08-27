package com.skyerzz.friendremover;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.collection.parallel.ParIterableLike;

/**
 * Created by sky on 26-8-2016.
 */
@Mod(modid = FriendRemover.MODID, version = FriendRemover.VERSION)
public class FriendRemover
{
    public static final String MODID = "hypixellib";
    public static final String VERSION = "1.0";
    public static String APIKEY = null;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ClientCommandHandler.instance.registerCommand(new RemoveAllFriendsCommand());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void chatListener(ClientChatReceivedEvent e){
        //System.out.print("CHATRECIEVED  ");
        if(e.getType()!=0){
            return;
        }
        //System.out.print("NOTNULL  ");
        String chat = e.getMessage().getUnformattedText();
        if(chat.contains("Your new API key is ")){
            this.APIKEY = chat.replace("Your new API key is ", "").trim();
            System.out.print("[Friend Remover] FOUNDAPIKEY: " + this.APIKEY);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(ChatFormatting.RED + "[Friend Remover] Succesfully set your API key to " + this.APIKEY));
        }
    }
}
