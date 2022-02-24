package me.phqsh.minecraftbridge.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.text.PaperComponents;
import me.phqsh.minecraftbridge.DiscordifyMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MinecraftEvents implements Listener {
    private final String channel;
    private final JavaPlugin plugin;

    public MinecraftEvents(JavaPlugin plugin){
        this.channel = plugin.getConfig().getString("pluginChannel");
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event){
         MessageEmbed eb = new EmbedBuilder()
                 .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                 .setDescription(PaperComponents.plainTextSerializer().serialize(event.message()))
                 .setColor(new Color(0x4D79E3))
                 .setFooter(this.plugin.getConfig().getString("embedFooter"))
                 .build();

        sendMessage(eb);
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                .setDescription(PaperComponents.plainTextSerializer().serialize(event.joinMessage()))
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().getString("embedFooter"))
                .build();

        sendMessage(eb);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                .setDescription(PaperComponents.plainTextSerializer().serialize(event.quitMessage()))
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().getString("embedFooter"))
                .build();

        sendMessage(eb);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getEntity().getName(), null, "https://mc-heads.net/avatar/" + event.getEntity().getName())
                .setDescription(PaperComponents.plainTextSerializer().serialize(event.deathMessage()))
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().getString("embedFooter"))
                .build();

        sendMessage(eb);
    }

    @EventHandler
    public void onAdvancementGain(PlayerAdvancementDoneEvent event){
        String advName;
        try{
            NamespacedKey advancement = event.getAdvancement().getKey();
            String key = "advancements." + advancement.toString().replace('/', '.').substring(10) + ".title";

            InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream("advancements.json");
            JsonObject json = JsonParser.parseReader(new JsonReader(new InputStreamReader(jsonStream))).getAsJsonObject();

            advName = json.get(key).getAsString();
        } catch (Exception e) {
            return;
        }

        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                .setDescription(event.getPlayer().getName() + " has completed the advancement **" + advName + "**!")
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().getString("embedFooter"))
                .build();

        sendMessage(eb);
    }

    @SuppressWarnings("unused")
    private void sendMessage(String message){
        DiscordifyMain.jda.getTextChannelById(this.channel).sendMessage(message).queue();
        return;
    }

    private void sendMessage(MessageEmbed message){
        DiscordifyMain.jda.getTextChannelById(this.channel).sendMessageEmbeds(message).queue();
        return;
    }
}
