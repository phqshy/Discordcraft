package me.phqsh.minecraftbridge;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.phqsh.minecraftbridge.commands.DGetOnline;
import me.phqsh.minecraftbridge.events.MinecraftEvents;
import me.phqsh.minecraftbridge.events.onDiscordChat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.awt.*;


public class DiscordifyMain extends JavaPlugin {
    public static JDA jda;
    public static String channel;
    public static MemberCachePolicy memberCache;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new MinecraftEvents(this), this);

        this.saveDefaultConfig();
        FileConfiguration config;

        config = this.getConfig();

        try {
            memberCache = MemberCachePolicy.ALL;
            CommandClientBuilder client = new CommandClientBuilder()
                        .addSlashCommands(new DGetOnline(this))
                        .setOwnerId(config.getString("botOwnerID"));

            if (config.getString("discordGuild").equals("none")){
                throw new IllegalArgumentException("You need to specify a discord guild in config.yml!");
            }

            if (!(config.getString("activityStatus") == null)){
                client.setActivity(Activity.watching(config.getString("activityStatus")));
            } else {
                client.setActivity(null);
            }

            client.forceGuildOnly(config.getString("discordGuild"));

            jda = JDABuilder.createDefault(config.getString("botToken"))
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(memberCache)
                    .addEventListeners(new onDiscordChat(this), client.build())
                    .build();

            jda.awaitReady();

            getLogger().fine("Plugin online");
            channel = config.getString("pluginChannel");

            MessageEmbed eb = new EmbedBuilder()
                    .setAuthor("System", null, "https://images-ext-1.discordapp.net/external/frbyI-322yUo52YdCakBWK1STEZcr6yrBFZa-uWr7fU/https/mc-heads.net/avatar/UncleJaym")
                    .setDescription("Server started!")
                    .setColor(new Color(0x4D79E3))
                    .setFooter(getConfig().getString("embedFooter"))
                    .build();

            getLogger().info(channel);
            DiscordifyMain.jda.getTextChannelById(channel).sendMessageEmbeds(eb).queue();
        } catch (LoginException e) {
            getLogger().severe("***WARNING!!! BOT TOKEN INVALID! EDIT THE CONFIG.YML TO FIX THIS.***");
            this.setEnabled(false);
        } catch (InterruptedException e) {
            getLogger().severe("There was a problem initializing the Discord bot, try restarting the server.");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor("System", null, "https://images-ext-1.discordapp.net/external/frbyI-322yUo52YdCakBWK1STEZcr6yrBFZa-uWr7fU/https/mc-heads.net/avatar/UncleJaym")
                .setDescription("Server closed.")
                .setColor(new Color(0x4D79E3))
                .setFooter(getConfig().getString("embedFooter"))
                .build();

        DiscordifyMain.jda.getTextChannelById(channel).sendMessageEmbeds(eb).queue();
    }
}