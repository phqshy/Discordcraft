package me.phqsh.minecraftbridge.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.Collection;

public class DGetOnline extends SlashCommand {
    private final JavaPlugin plugin;

    public DGetOnline(JavaPlugin plugin){
        this.name = "online";
        this.help = "Gets the online players on the server!";
        this.plugin = plugin;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        String playerString = "";
        for (Player p : players){
            playerString = playerString + "-" + p.getName() + "\n";
        }

        MessageEmbed eb = new EmbedBuilder()
                .setAuthor("System", null, "https://mc-heads.net/avatar/UncleJaym")
                .addField("Online players", playerString, false)
                .setColor(new Color(0x4D79E3))
                .setFooter(this.plugin.getConfig().getString("embedFooter"))
                .build();

        event.replyEmbeds(eb).setEphemeral(true).queue();
    }
}
