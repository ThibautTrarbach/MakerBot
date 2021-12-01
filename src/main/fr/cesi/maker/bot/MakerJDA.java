package fr.cesi.maker.bot;

import fr.cesi.maker.bot.listener.ListenerAdapters;
import fr.cesi.maker.bot.listener.OnGuildMemberJoin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class MakerJDA {

    private final Logger log = JDALogger.getLog(MakerJDA.class);

    private final Main main;

    public MakerJDA(Main main) {
        this.main = main;
    }

    public JDA getJDA() throws LoginException {

        String token = Main.CONFIGURATION.getString("bot_token", "Token Bot");
        log.info("Initialization du bot avec le token : "+token);
        JDABuilder jdaBuilder = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class));
        jdaBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        jdaBuilder.setActivity(Activity.playing(Main.CONFIGURATION.getString("defaultActivity", "loading...")));

        jdaBuilder.addEventListeners(new ListenerAdapters(main));
        jdaBuilder.addEventListeners(new OnGuildMemberJoin(main));

        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        log.info("Build du bot en cours");
        return jdaBuilder.build();
    }

}
