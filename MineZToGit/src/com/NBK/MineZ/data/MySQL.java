package com.NBK.MineZ.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

public class MySQL {
	
	private String Host;
    private String Database;
    private String Username;
    private String Password;
    private Connection connection;
    static Connection c;
    
    public MySQL(final String host, final String database, final String username, final String password) {
        this.Host = host;
        this.Database = database;
        this.Username = username;
        this.Password = password;
    }
    
    public void Connect() {
    	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §aConnecting§f...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e1) {
            Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cThe required classes are missing§f!");
            e1.printStackTrace();
        }
    	final String url = "jdbc:mysql://" + this.Host + ":3306/" + this.Database + "?useUnicode=true&characterEncoding=utf-8";//UTF connection
        try {
            this.connection = DriverManager.getConnection(url, this.Username, this.Password);
            c = DriverManager.getConnection(url, this.Username, this.Password);
            Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §aConnected§f!");
        }
        catch (SQLException e2) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cThere was a connection error§f!");
            e2.printStackTrace();
        }
    }
    
    public void Disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
                Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §eThe connection to the MySQL server was successfully disconnected§f!");
            }
            else {
            	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cThe connection is already terminated!");
            }
        }
        catch (SQLException e3) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cAn error occurred while disconnecting§f!");
            e3.printStackTrace();
        }
    }
    
    public ResultSet GetResult(final String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }
            final Statement st = this.connection.createStatement();
            st.executeQuery(command);
            final ResultSet rs = st.getResultSet();
            return rs;
        }
        catch (SQLException e4) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cAn error occurred while executing the command§f!");
            e4.printStackTrace();
            return null;
        }
    }
    
    public void ExecuteCommand(final String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }
            final Statement st = this.connection.createStatement();
            st.executeUpdate(command);
        }
        catch (SQLException e5) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cAn error occurred while executing the command§f!");
            e5.printStackTrace();
        }
    }
	public Connection getConnection() {
		return this.connection;
	}
}
