package fr.cesi.maker.bot.listener;

import fr.cesi.maker.bot.Main;
import fr.cesi.maker.bot.command.CommonCommand;
import fr.cesi.maker.bot.command.MusicCommand;
import fr.cesi.maker.bot.components.RoleMenu;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerAdapters extends ListenerAdapter {
    private final Main main;
    private final MusicCommand musicCommand ;
    private final CommonCommand commonCommand ;
    private final RoleMenu roleMenu;

    public ListenerAdapters(Main main) {
        this.main = main;
        musicCommand = new MusicCommand(main);
        commonCommand = new CommonCommand(main);
        roleMenu = new RoleMenu(main);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {

        System.out.println(event.getComponent().getId());

        if (event.getButton().getId().equals("menu:role:open")){
            new RoleMenu(main).sendMenu(event);
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("music")) {
            musicCommand.dispatchCMD(event);
        }
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        System.out.println(event.getComponentId());

        if (event.getComponentId().equals("menu:role:change")){
            roleMenu.updateRole(event);
        }
    }
}