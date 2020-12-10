package neo2057.github.io.git.neomultiworld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class NeoMultiWorldCommands implements CommandExecutor {
    private final NeoMultiWorld instance;
    public NeoMultiWorldCommands(NeoMultiWorld instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = instance.getConfig();
        if (sender instanceof Player){
            Player pl = (Player) sender;
            if(args.length == 0)
                return false;
            //へるぷ
            if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                if(args.length != 1) {
                    pl.sendMessage("/nmw help");
                    return false; }
                pl.sendMessage("/nmw help\n" +
                        "/nmw create [World Name]\n" +
                        "/nmw unload [World Name]\n" +
                        "/nmw list\n" +
                        "");
                return true;
            }//ホーム
            if(args[0].equalsIgnoreCase("home")) {
                World w = Bukkit.getWorld("world");
                Location loc = new Location(w,w.getSpawnLocation().getX(),w.getSpawnLocation().getY(),w.getSpawnLocation().getZ());
                pl.teleport(loc);
                return true;
            }
            //ワールドの生成
            if(args[0].equalsIgnoreCase("create")) {
                if(args.length != 2) {
                    pl.sendMessage("[NMW]/nmw create [worldName]");
                    return false; }

                for(Player p:Bukkit.getOnlinePlayers())
                    p.sendMessage("ワールドを生成しています...");

                WorldCreator wc = new WorldCreator(args[1]);
                wc.createWorld();

                config.set("Worlds."+ args[1] +".name",args[1]);
                instance.saveConfig();
                instance.reloadConfig();

                for(Player p:Bukkit.getOnlinePlayers())
                    p.sendMessage("ワールドの生成が終了いたしました。");
                return true;
            }
            //ワールドの読み込み停止
            if(args[0].equalsIgnoreCase("unload")) {
                if(args.length != 2) {
                    pl.sendMessage("[NMW]/nmw unload [worldName]");
                    return false; }
                World w = Bukkit.getWorld(args[1]);
                Bukkit.unloadWorld(w,true);

                config.set("Worlds."+args[1],null);
                instance.saveConfig();
                instance.reloadConfig();
                return true;
            }
            //読み込まれているワールドのリスト表示
            if(args[0].equalsIgnoreCase("list")) {
                if(args.length != 1) {
                    pl.sendMessage("ミスってるぞ");
                    return false; }


                if (Objects.requireNonNull(config.getConfigurationSection("Worlds")).getKeys(false).size() != 0) {
                    for (String name : Objects.requireNonNull(config.getConfigurationSection("Worlds")).getKeys(false))
                        pl.sendMessage(name);
                }else{
                    pl.sendMessage("[NMW]リストに登録されているワールドがありません。\n[NMW]/nmw create [worldName]で作成してください。");
                }

                return true;
            }
            //ワールドのテレポート
            if(args[0].equalsIgnoreCase("tp")) {
                if(args.length != 2 && args.length != 3) {
                    pl.sendMessage("[NMW]/nmw tp [worldName] or /nmw tp [worldName] [playerName]");
                    return false; }

                if(Bukkit.getWorld(args[1]) == null){
                    pl.sendMessage("[NMW]"+args[1]+"というワールドは読み込まれていません");
                    return false;
                }
                World w = Bukkit.getWorld(args[1]);
                assert w != null;
                Location loc = new Location(w,w.getSpawnLocation().getX(),w.getSpawnLocation().getY(),w.getSpawnLocation().getZ());

                if (args.length == 3){
                    Player player = Bukkit.getPlayer(args[2]);
                    assert player != null;
                    if(Objects.isNull(player)){
                        System.out.println(args[2]);
                        return false;
                    }
                    player.teleport(loc);
                } else{
                    pl.teleport(loc);
                }
                return true;
            }
        }
        return false;
    }
}
