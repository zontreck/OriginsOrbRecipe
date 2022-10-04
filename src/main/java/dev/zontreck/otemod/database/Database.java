package dev.zontreck.otemod.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;

public class Database {
    private OTEMod mod;
    private Connection connection;


    public Database (OTEMod instance) throws DatabaseConnectionException
    {
        mod=instance;
        try{
            this.connect();
        }catch(Exception e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    public void connect() throws Exception
    {
        connection = DriverManager.getConnection("jdbc:mariadb://"+OTEServerConfig.HOST_ADDR.get()+":"+OTEServerConfig.PORT.get()+"/" + OTEServerConfig.DATABASE.get() + "?useSSL=false", OTEServerConfig.USERNAME.get(), OTEServerConfig.PASSWORD.get());
    }


    public class DatabaseConnectionException extends Exception
    {
        public DatabaseConnectionException(String X) {
            super(X);
        }
    }

    public boolean isConnected() throws SQLException
    {
        if(connection != null)
        {
            if(connection.isClosed())
            {
                return false;
            }else return true;
        }else return false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() throws SQLException
    {
        if(isConnected())
        {
            try{
                connection.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
