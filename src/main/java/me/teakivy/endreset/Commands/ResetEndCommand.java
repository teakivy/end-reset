package me.teakivy.endreset.Commands;

import me.teakivy.endreset.Main;
import me.teakivy.endreset.Utils.AbstractCommand;
import me.teakivy.endreset.Utils.EndReset;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ResetEndCommand extends AbstractCommand {
    public ResetEndCommand() {
        super("resetend", "/resetend", "resetend", Collections.singletonList("endreset"));
    }

    Main main = Main.getPlugin(Main.class);
    String worldName = main.getConfig().getString("world-folder-name");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Please confirm that you would like to reset " + worldName + ".\nType /resetend confirm to continue.");
            return true;
        }

        if (args[0].equalsIgnoreCase("confirm")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Resetting " + worldName + "..." + "\n" + "Depending on the size of the world, this could take a few minutes.");
            EndReset.resetEnd(sender, worldName);
        }


        return false;
    }


}
