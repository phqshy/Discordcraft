package me.phqsh.minecraftbridge;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.phqsh.minecraftbridge.commands.DGetOnline;
import me.phqsh.minecraftbridge.events.onMinecraftChat;
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
        Bukkit.getServer().getPluginManager().registerEvents(new onMinecraftChat(this), this);

        FileConfiguration config = this.getConfig();
        config.addDefault("pluginChannel", "none");
        config.addDefault("botToken", "none");
        config.addDefault("discordGuild", "none");
        config.addDefault("embedFooter", "none");
        config.addDefault("activityStatus", "none");
        config.addDefault("botOwnerID", "670630054417399808");
        config.options().copyDefaults(true);
        saveConfig();

        try {
            memberCache = MemberCachePolicy.ALL;

            CommandClientBuilder client = new CommandClientBuilder()
                    .setActivity(Activity.watching(config.get("activityStatus").toString()))
                    .addSlashCommands(new DGetOnline(this))
                    .setOwnerId(config.get("botOwnerID").toString());

            if (config.get("discordGuild").toString().equals("none")){
                throw new IllegalArgumentException("You need to specify a discord guild in config.yml!");
            }

            if (!(config.get("activityStatus").toString().equals("none"))){
                client.setActivity(Activity.watching(config.get("activityStatus").toString()));
            }

            client.forceGuildOnly(config.get("discordGuild").toString());

            jda = JDABuilder.createDefault(this.getConfig().getString("botToken"))
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(memberCache)
                    .addEventListeners(new onDiscordChat(this), client.build())
                    .build();

            jda.awaitReady();
        } catch (LoginException e) {
            getLogger().severe("***WARNING!!! BOT TOKEN INVALID! EDIT THE CONFIG.YML TO FIX THIS.***");
        } catch (InterruptedException e) {
            getLogger().severe("There was a problem initializing the Discord bot, try restarting the server.");
            e.printStackTrace();
        } catch(Exception e) {

        }

        getLogger().fine("Plugin online");
        channel = config.get("pluginChannel").toString();

        MessageEmbed eb = new EmbedBuilder()
                .setAuthor("System", null, "https://mc-heads.net/avatar/UncleJaym")
                .setDescription("Server started!")
                .setColor(new Color(0x4D79E3))
                .setFooter(getConfig().get("embedFooter").toString())
                .build();

        DiscordifyMain.jda.getTextChannelById(channel).sendMessageEmbeds(eb).queue();
    }

    @Override
    public void onDisable() {
        MessageEmbed eb = new EmbedBuilder()
                .setAuthor("System", null, "https://mc-heads.net/avatar/UncleJaym")
                .setDescription("Server closed.")
                .setColor(new Color(0x4D79E3))
                .setFooter(getConfig().get("embedFooter").toString())
                .build();

        DiscordifyMain.jda.getTextChannelById(channel).sendMessageEmbeds(eb).queue();
    }
}