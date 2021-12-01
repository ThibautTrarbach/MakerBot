package fr.cesi.maker.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.cesi.maker.utils.FileLang;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {

    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final Map<String, MusicPlayer> players = new HashMap<>();
    private final FileLang fileLang;

    public MusicManager(FileLang fileLang) {
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
        this.fileLang = fileLang;
    }

    public synchronized MusicPlayer getPlayer(Guild guild) {
        if (!players.containsKey(guild.getId()))
            players.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
        return players.get(guild.getId());
    }

    public void loadTrack(SlashCommandEvent event, final String source, User user) {
        MusicPlayer player = getPlayer(event.getGuild());

        event.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());

        manager.loadItemOrdered(player, source, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(fileLang.get("musicADDPiste"));
                embedBuilder.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ") \n");
                embedBuilder.setThumbnail("https://img.youtube.com/vi/" + track.getInfo().identifier + "/0.jpg");
                embedBuilder.setFooter("Ajouté par " + user.getName(), user.getEffectiveAvatarUrl());
                embedBuilder.setColor(Color.CYAN);
                event.replyEmbeds(embedBuilder.build()).queue();
                player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Ajout d'une Playlist : ***" + playlist.getName() + "***", source);
                embedBuilder.setThumbnail("https://img.youtube.com/vi/" + playlist.getSelectedTrack().getIdentifier() + "/0.jpg");
                embedBuilder.setFooter("Ajouté par " + user.getName(), user.getEffectiveAvatarUrl());
                embedBuilder.setColor(Color.CYAN);

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < playlist.getTracks().size() && i < 10; i++) {
                    AudioTrack track = playlist.getTracks().get(i);
                    builder.append("\n  **->** ").append(track.getInfo().author).append(" : [").append(track.getInfo().title).append("](").append(track.getInfo().uri).append(")");
                    player.playTrack(track);
                }
                embedBuilder.setDescription(builder.toString());
                event.replyEmbeds(embedBuilder.build()).queue();
            }


            @Override
            public void noMatches() {
                event.reply("La piste " + source + " n'a pas été trouvée.").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.reply("Impossible de jouer la piste (raison:" + exception.getMessage() + ")").queue();
            }
        });
    }

}
