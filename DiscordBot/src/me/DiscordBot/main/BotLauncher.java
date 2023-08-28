package me.DiscordBot.main;

import me.DiscordBot.main.commands.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.security.auth.login.LoginException;

public class BotLauncher {

    public static void main(String args[]) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault("MTEzNzAyMTg0NTUyMzE1NzE4Mw.Gfe6Es.52WmFSQOwWvbCWu77T2NG8_bIUIo-UhhDziAMc")
                .setActivity(Activity.listening("Heilag Vagga"))
                .addEventListeners(new Commands())
                .build();

        // ID da guilda
        Guild guild = jda.getGuildById("1128820149567369237");

        if(guild != null) {
            // Escolher ritual
            guild.upsertCommand("ritual", "Use um ritual")
                    .addOption(OptionType.STRING,"nome", "digite o nome do ritual")
                    .queue();

        }

    }
}
