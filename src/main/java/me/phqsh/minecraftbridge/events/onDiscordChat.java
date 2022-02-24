package me.phqsh.minecraftbridge.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

        plugin.getLogger().info("Discord > " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay());

        Component component = Component.text()
                .content("Discord > ")
                .color(TextColor.color(0x4D79E3))
                .append(Component.text().content(event.getAuthor().getName() + ": ").color(TextColor.color(0xE5C812)))
                .append(Component.text().content(event.getMessage().getContentDisplay()).color(TextColor.color(0xFFFFFF)))
                .build();

        plugin.getServer().broadcast(component);
    }
}
