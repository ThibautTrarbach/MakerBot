package fr.cesi.maker.bot.command;

import fr.cesi.maker.bot.Main;
import fr.cesi.maker.bot.music.MusicPlayer;
import fr.cesi.maker.utils.FileLang;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class MusicCommand {

    private final Main main;
    private final FileLang fileLang;

    public MusicCommand(Main main) {
        this.main = main;
        this.fileLang = main.fileLang;
    }

    public void dispatchCMD(SlashCommandEvent event) {
        switch (event.getSubcommandName()) {
            case "skip":
                skip(event);
                break;
            case "pause":
                pause(event);
                break;
            case "clear":
                clear(event);
                break;
            case "play":
                play(event);
                break;
            case "volume":
                volume(event);
                break;
        }
    }

    private void clear(SlashCommandEvent event) {
        TextChannel textChannel = event.getTextChannel();

        MusicPlayer player = main.manager.getPlayer(textChannel.getGuild());

        if (player.getListener().getTracks().isEmpty()) {
            event.reply(fileLang.get("MusicNoAttente")).queue();
            return;
        }

        player.getListener().getTracks().clear();
        event.reply(fileLang.get("MusicVide")).queue();
    }

    private void pause(SlashCommandEvent event) {
        Guild guild = event.getGuild();

        if (!guild.getAudioManager().isConnected()) {
            event.reply(fileLang.get("MusicPlayerVide")).queue();
            return;
        }

        main.manager.getPlayer(guild).pauseTrack();
        event.reply(fileLang.get("MusicPauseStart")).setEphemeral(true).queue();
    }

    private void play(SlashCommandEvent event) {

        Guild guild = event.getGuild();
        User user = event.getUser();

        if (!guild.getAudioManager().isConnected()) {
            VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
            if (voiceChannel == null) {
                event.reply(fileLang.get("musicConChannel")).queue();
                return;
            }
            guild.getAudioManager().openAudioConnection(voiceChannel);
        }
        main.manager.loadTrack(event, event.getOption("lien").getAsString(), event.getUser());
    }

    private void skip(SlashCommandEvent event) {
        Guild guild = event.getGuild();

        if (!guild.getAudioManager().isConnected()) {
            event.reply(fileLang.get("MusicPlayerVide")).queue();
            return;
        }

        main.manager.getPlayer(guild).skipTrack();
        event.reply(fileLang.get("MusicSkip")).queue();
    }

    private void volume(SlashCommandEvent event) {
        Guild guild = event.getGuild();

        if (!guild.getAudioManager().isConnected()) {
            event.reply(fileLang.get("MusicPlayerVide")).queue();
            return;
        }

        long volume = event.getOption("volume").getAsLong();
        if (0 <= volume && volume <= 100) {
            main.manager.getPlayer(guild).volumeTrack(volume);
            event.reply(fileLang.get("MusicVolumeDe") + volume).queue();
        } else {
            event.reply(fileLang.get("MusicVolumeError")).queue();
        }
    }
}