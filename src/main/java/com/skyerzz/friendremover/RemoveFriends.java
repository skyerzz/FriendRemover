package com.skyerzz.friendremover;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import scala.collection.parallel.ParIterableLike;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SyncFailedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sky on 26-8-2016.
 */
public class RemoveFriends {

    public long systemTimeOnLastHunderdStart = System.currentTimeMillis();
    public int friendsRemoved = 0;
    public int msDelay = 200;

    public RemoveFriends(){

    }

    public void start(){
        if(FriendRemover.APIKEY==null){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(ChatFormatting.RED + "[Friend Remover] Please use /api first! We need your API key to remove friends!"));
            return;
        }
        try {
            startRemovingFriends(getJSON(getURL(Minecraft.getMinecraft().thePlayer.getUniqueID().toString().replace("-", ""))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(String name){
        System.out.println("Removing Friend " + name + " #" + friendsRemoved);
        System.out.println("Current: " + System.currentTimeMillis() + " old:" + systemTimeOnLastHunderdStart);
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/f remove " + name);
    }

    public void startRemovingFriends(JsonObject json){
        if(json==null){
            return;
        }
        final JsonArray array = json.get("records").getAsJsonArray();
        new Thread(new Runnable() {

            @Override
            public void run() {

                for(JsonElement element: array) {

                    if(friendsRemoved==0 || friendsRemoved%100!=0 || systemTimeOnLastHunderdStart+60000 < System.currentTimeMillis()) {
                        String uuid = element.getAsJsonObject().get("uuidReceiver").getAsString();
                        if (uuid.replace("-", "").equals(Minecraft.getMinecraft().thePlayer.getUniqueID().toString().replace("-", ""))) {
                            removeFriend(getPlayerName(element.getAsJsonObject().get("uuidSender").getAsString()));
                        } else {
                            removeFriend(getPlayerName(element.getAsJsonObject().get("uuidReceiver").getAsString()));
                        }
                    }

                    try {
                        Thread.sleep(msDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public URL getStatURL(String UUID){
                String url = "https://api.hypixel.net/player";
                url += "?key=" + FriendRemover.APIKEY + "&uuid=" + UUID;
                try {
                    return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerName(String UUID){
        JsonObject json;
        try {
            json = getJSON(getStatURL(UUID));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json.get("player").getAsJsonObject().get("playername").getAsString();
    }

    public URL getURL(String UUID){
        String url = "https://api.hypixel.net/friends";
        url += "?key=" + FriendRemover.APIKEY + "&uuid=" + UUID;
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String readURL(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = reader.read()) != -1) {
            sb.append((char) i);
        }
        return sb.toString();
    }


    public JsonObject getJSON(URL webLink) throws IOException{
        HttpsURLConnection con = (HttpsURLConnection)webLink.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String jsonText = readURL(rd);
        JsonObject json = new JsonParser().parse(jsonText).getAsJsonObject();
        rd.close();
        if(this.friendsRemoved%100==0){
            this.systemTimeOnLastHunderdStart = System.currentTimeMillis();
        }
        this.friendsRemoved++;

        if(!json.get("success").getAsBoolean()) {
            System.out.println("ERROR: Wrong API key or No Valid Player!");
            Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(ChatFormatting.RED + "[Friend Remover] Your API key is not valid, or something went terribly wrong!"));
            return null;
        }

        return json;
    }



}
