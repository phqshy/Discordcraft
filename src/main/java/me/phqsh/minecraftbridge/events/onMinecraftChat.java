package me.phqsh.minecraftbridge.events;

import me.phqsh.minecraftbridge.DiscordifyMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public class onMinecraftChat implements Listener {
    private final String channel;
    private final JavaPlugin plugin;

    public onMinecraftChat(JavaPlugin plugin){
        this.channel = plugin.getConfig().get("pluginChannel").toString();
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
         MessageEmbed eb = new EmbedBuilder()
                 .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                 .setDescription(event.getMessage())
                 .setColor(new Color(0x4D79E3))
                 .setFooter(this.plugin.getConfig().get("embedFooter").toString())
                 .build();

        DiscordifyMain.jda.getTextChannelById(this.channel).sendMessageEmbeds(eb).queue();
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                .setDescription(event.getJoinMessage().substring(2))
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().get("embedFooter").toString())
                .build();

        DiscordifyMain.jda.getTextChannelById(this.channel).sendMessageEmbeds(eb).queue();
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getPlayer().getName(), null, "https://mc-heads.net/avatar/" + event.getPlayer().getName())
                .setDescription(event.getQuitMessage().substring(2))
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().get("embedFooter").toString())
                .build();

        DiscordifyMain.jda.getTextChannelById(this.channel).sendMessageEmbeds(eb).queue();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor(event.getEntity().getName(), null, "https://mc-heads.net/avatar/" + event.getEntity().getName())
                .setDescription(event.getDeathMessage())
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().get("embedFooter").toString())
                .build();

        DiscordifyMain.jda.getTextChannelById(this.channel).sendMessageEmbeds(eb).queue();
    }
}
