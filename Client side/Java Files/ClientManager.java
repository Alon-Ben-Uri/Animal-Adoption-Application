package com.example.myapplication;

/* Singleton design pattern to get access to the client from every class. */
public class ClientManager {
    private static Client client;

    public static Client getClient(){
        if(client == null){
            client = new Client();
        }
        return client;
    }

    public static void setClient(Client client){
        ClientManager.client = client;
    }
}
