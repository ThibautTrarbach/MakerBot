package fr.cesi.maker.bot.command;

import fr.cesi.maker.bot.Main;
import fr.cesi.maker.utils.FileLang;

public class CommonCommand {

    private final FileLang fileLang;

    public CommonCommand(Main main) {
        this.fileLang = main.fileLang;
    }
}