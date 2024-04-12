package dev.loveeev.astracore.database;


import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.settings.Settings;
import lombok.Getter;

import java.sql.*;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

public class MySQL {

    public Object mySQLinteger;
    private Connection connection;


    private String username;
    private String dbname;
    private String password;
    private String host;
    private Object prop;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Getter
    private static MySQL instance = new MySQL();
    public MySQL() {
        host = Settings.MYSQL_HOST;
        username = Settings.MYSQL_USERNAME;
        password = Settings.MYSQL_PASSWORD;
        dbname = Settings.MYSQL_DATABASE;
        this.initialize();
    }



    public boolean isConnected() {
        return connection != null;
    }

    public void initialize(){
        this.prop = new Properties();
        ((Hashtable<Object, Object>) this.prop).put("autoReconnect", "true");
        ((Hashtable<String, String>) this.prop).put("useSSL", "true");
        ((Hashtable<String, String>) this.prop).put("user", username);
        ((Hashtable<String, String>) this.prop).put("password", password);
        ((Hashtable<String, String>) this.prop).put("useUnicode", "true");
        ((Hashtable<String, String>) this.prop).put("characterEncoding", "utf8");
        message();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Ошибка при загрузке JDBC драйвера MySQL", e);
        }

    }
    public ScheduledExecutorService getExecutor(){
        return this.executorService;
    }
    public Connection getCon() {
        try {
            if (this.connection != null && !this.connection.isClosed() && this.connection.isValid(20)){
                return this.connection;
            }
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":3306/" + this.dbname, (Properties) this.prop );
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Ошибка при подключении к базе данных MySQL", ex);
        }
        return this.connection;
    }
    public ResultSet executeQuery(String s, Object... objs){
        Callable<?> eq = () -> {
            PreparedStatement ps = this.getCon().prepareStatement(s);
            for (int i = 0; i < objs.length; ++i){
                ps.setObject(i+1,objs[i]);
            }
            return ps.executeQuery();
        };
        try {
            return (ResultSet) eq.call();
        } catch (Exception var5){
            Main.getInstance().getLogger().log(Level.SEVERE, "Ошибка при выполнении запроса MySQL", var5);
            return null;
        }
    }


    public void executeUpdate(String s, Object... objs){
        this.executorService.execute(() -> {
            try {
                PreparedStatement e = this.getCon().prepareStatement(s);
                for (int i = 0; i < objs.length; ++i) {
                    e.setObject(i + 1, objs[i]);
                }
                e.executeUpdate();
                e.close();
            } catch (SQLException var5) {
                Main.getInstance().getLogger().log(Level.SEVERE, "Ошибка при выполнении запроса на обновление MySQL", var5);
            }
        });
    }



    public void update(String database, String table, String where, Object znah, String search) {
        String query = "UPDATE " + database + " SET " + table + " = ? WHERE " + where + " = ?";
        this.executeUpdate(query,znah,search);
    }

    public void updateint(String database, String table, String where, Integer znah, String search) {
        String query = "UPDATE " + database + " SET " + table + " = ? WHERE " + where + " = ?";
        this.executeUpdate(query,znah,search);
    }


    public void selectstring(String database, String table, String search, String get, String method) {
        String query = "SELECT * FROM `" + database + "` WHERE " + table + " = ? ";
        ResultSet resultSet = this.executeQuery(query,search);
        try {
            if (!resultSet.next()) {
                Main.getInstance().getLogger().warning("Ошибка базы данных.");
            } else {
                if (method == "integer") {
                    this.mySQLinteger = resultSet.getInt(get);
                }else if(method == "string"){
                    this.mySQLinteger = resultSet.getString(get);
                } else if (method == "double") {
                    this.mySQLinteger = resultSet.getDouble(get);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void message(){
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5#########################################################################");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |                                                                   | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |                           ASTRA GROUP                             | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |                 © 2020-2024 - Creativity Community                | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |                                                                   | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# +-------------------------------------------------------------------+ #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5#########################################################################");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# +-------------------------------------------------------------------+ #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |                  Web-Site: https://astraworld.su                  | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |                   VK: https://astraworld.su/vk                    | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# |              Discord: https://astraworld.su/discord               | #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5# +-------------------------------------------------------------------+ #");
        Main.getInstance().getServer().getConsoleSender().sendMessage("§x§d§2§3§5§3§5#########################################################################");
    }
}
