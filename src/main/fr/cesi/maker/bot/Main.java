package fr.cesi.maker.bot;

import fr.cesi.maker.bot.music.MusicManager;
import fr.cesi.maker.bot.utils.Create;
import fr.cesi.maker.utils.Configuration;
import fr.cesi.maker.utils.FileLang;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class Main {


    public static Configuration CONFIGURATION = null;

    public final JDA jda;
    private final Logger log = JDALogger.getLog(Main.class);
    public final FileLang fileLang = new FileLang();
    public final MusicManager manager = new MusicManager(fileLang);

    public Main() throws IllegalArgumentException, InterruptedException, LoginException {
        final long time1 = System.currentTimeMillis();

        try {
            File directory = new File("config");
            if (!directory.exists()) {
                directory.mkdir();
            }
            CONFIGURATION = new Configuration("config/config.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(CONFIGURATION == null) {
            log.error("Le fichier de configuration n'a pas été chargé !");
            System.exit(0);
        }

        MakerJDA makerJDA = new MakerJDA(this);
        this.jda = makerJDA.getJDA();

        jda.awaitReady();

        //new Create(this).setupRoleChannel();

        final long time2 = System.currentTimeMillis();
        log.info("Temps d'initialisation du bot : " + (time2 - time1) + " ms");
        jda.getPresence().setActivity(null);
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
            CONFIGURATION.save();
        }
        CONFIGURATION.save();
    }

}
