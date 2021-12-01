package fr.cesi.maker.bot.components;

import fr.cesi.maker.bot.Main;
import fr.cesi.maker.utils.FileLang;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RoleMenu {

    private final Main main;
    private final FileLang fileLang;

    public RoleMenu(Main main) {
        this.main = main;
        this.fileLang = main.fileLang;
    }

    public void sendMenu(ButtonClickEvent event) {
        JSONObject roles = main.CONFIGURATION.getJSONObject("roles_menu");

        SelectionMenu.Builder menuBuilder = SelectionMenu.create("menu:role:change")
                .setPlaceholder(fileLang.get("menuRolePlaceHolder"));

        for (Map.Entry<String, Object> entry : roles.toMap().entrySet()) {
            String name = entry.getKey();
            String roleID = (String) entry.getValue();

            Role role = event.getGuild().getRoleById(roleID);
            if (event.getMember().getRoles().contains(role)) {
                menuBuilder.addOptions(SelectOption.of(name, roleID).withDefault(true));
            } else {
                menuBuilder.addOptions(SelectOption.of(name, roleID).withDefault(false));
            }
        }

        menuBuilder.setRequiredRange(0,(roles.toMap().size()));
        SelectionMenu menu = menuBuilder.build();

        event.reply(fileLang.get("menuRoleText"))
                .setEphemeral(true)
                .addActionRow(menu)
                .queue();
    }

    public void updateRole(SelectionMenuEvent event) {
        System.out.println(event.getValues());

        JSONObject roles = main.CONFIGURATION.getJSONObject("roles_menu");
        List<String> values = event.getValues();

        for (Map.Entry<String, Object> entry : roles.toMap().entrySet()) {
            String roleID = (String) entry.getValue();

            Role role = event.getGuild().getRoleById(roleID);
            if (values.contains(roleID)) {
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            } else {
                event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
            }
        }

        event.reply(fileLang.get("menuRoleUpdate")).setEphemeral(true).queue();
    }
}