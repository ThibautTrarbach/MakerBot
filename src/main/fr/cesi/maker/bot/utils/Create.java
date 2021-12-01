package fr.cesi.maker.bot.utils;

import fr.cesi.maker.bot.Main;
import fr.cesi.maker.utils.FileLang;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;

public class Create {

    private final Main main;
    private final FileLang fileLang;

    public Create(Main main) {
        this.main = main;
        this.fileLang = main.fileLang;
    }


    public void setupRoleChannel() {
        TextChannel textChannel = main.jda.getTextChannelById(826393971332481039L);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setDescription(fileLang.get("roleEmbedMenu"));
        textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.success("menu:role:open", fileLang.get("roleEmbedMenuButton"))).queue();
    }

}
