package me.phqsh.minecraftbridge.events;

import me.phqsh.minecraftbridge.DiscordifyMain;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class onDiscordChat extends ListenerAdapter {
    private final JavaPlugin plugin;
    public onDiscordChat(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!(event.getChannel().getId().equals(plugin.getConfig().get("pluginChannel").toString()))) return;
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentDisplay().indexOf("&") != -1) return;

        plugin.getLogger().info("Discord > " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay());

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9Discord > &e" + event.getAuthor().getName() + ": &f" + event.getMessage().getContentDisplay()));
    }
}
