package dev.zontreck.otemod.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;

public class Database {
    private OTEMod mod;
    private Connection connection;


    public Database (OTEMod instance) throws DatabaseConnectionException, SQLException
    {
        mod=instance;
        try{
            this.connect();
        }catch(Exception e){
            throw new DatabaseConnectionException(e.getMessage());
        }

        Thread tx = new Thread(new Runnable(){
            public void run()
            {
                while(true){

                // Watch the connection for disconnections
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(!OTEMod.ALIVE)return;// Exit now. We are being torn down/server is going down
                try {
                    if(!OTEMod.DB.isConnected())
                    {
                        OTEMod.LOGGER.info("/!\\ Lost connection to data provider. Reconnecting...");
                        // Refresh
                        try {
                            OTEMod.DB.connect();

                            if(OTEMod.DB.isConnected())
                            {
                                OTEMod.LOGGER.info("/!\\ Reconnected!");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                }
            }
        });

        if(isConnected()){
            tx.start();
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
