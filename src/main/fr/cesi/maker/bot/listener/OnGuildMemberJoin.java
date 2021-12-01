package fr.cesi.maker.bot.listener;

import fr.cesi.maker.bot.Main;
import fr.cesi.maker.utils.FileLang;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

public class OnGuildMemberJoin extends ListenerAdapter {

    private final Main main;
    private final FileLang fileLang;

    public OnGuildMemberJoin(Main main) {
        this.main = main;
        this.fileLang = main.fileLang;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        addRoles(event);
        sendMessage(event);
    }

    private void addRoles(GuildMemberJoinEvent event) {
        JSONArray welecomRole = main.CONFIGURATION.getJSONArray("roles");

        welecomRole.forEach(data -> {
            String roleID = (String) data;
            Role role = event.getGuild().getRoleById(roleID);
            event.getGuild().addRoleToMember(event.getMember(), role).queue();
        });
    }

    private void sendMessage(GuildMemberJoinEvent event) {

        String channelID = Main.CONFIGURATION.getString("channel_welecom", "false");

        TextChannel textChannel = event.getGuild().getTextChannelById(channelID);

        String msgBienvenue = fileLang.get("msgBienvenue");
        String mention = event.getMember().getAsMention();
        String target = ":mention";
        msgBienvenue = msgBienvenue.replace(target, mention);
        textChannel.sendMessage(msgBienvenue).queue();
    }
}

