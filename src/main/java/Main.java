
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MGN04582
 */
public class Main {
    
    private Socket mSocket;
    private String mUsername;
    private Boolean isConnected = true;
    
    void onConnection(){
        
        ApplicationChat app = new ApplicationChat();
        mSocket  = app.getSocket();
        mSocket.on (Socket.EVENT_CONNECT, onConnect);
        mSocket.on (Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on (Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on (Socket.EVENT_CONNECT_TIMEOUT, onConnectError);               

        mSocket.on ("agent_message", onNewMessage);        
//        mSocket.on ("user left", onUserLeft);
        mSocket.on ("send_users", onSearchUsers);
//        mSocket.on ("stop typing", onStopTyping);
        mSocket.connect ();
        
    }   
    
    void onDestroy(){
        
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("agent_message", onNewMessage);
        mSocket.on ("send_users", onSearchUsers);
//        mSocket.off("user joined", onUserJoined);
//        mSocket.off("user left", onUserLeft);
//        mSocket.off("typing", onTyping);
//        mSocket.off("stop typing", onStopTyping);
        
    }
    
    private void leave() {
        mUsername = null;
        mSocket.disconnect();        
    }
    
    private void disconnetUser(){
        
//        User is taken from connected user list(is not created, create from event onSearchUsers)
//        mSocket.emit("disconnet_user",user);
        
    }
    
    private void sendMessage(){
        
        try{
            
            JSONObject data = new JSONObject();
            data.append("agent", mUsername);
            
            //This values are obtained from event onSearchUsers(handle method) and the message box from view
//            data.append("user", username);
//            data.append("message", text);
            
            mSocket.emit("private_message", data);
            
        } catch(JSONException e)      {
            
            e.printStackTrace();
            
        }
        
    }
    
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            
                    if(!isConnected) {
                        if(null!=mUsername){
                            try{
                                
                                mSocket.emit("new_agent",mUsername);
                                
                                JSONObject data = data = new JSONObject();
                                data.append("room", mUsername);
                                
                                mSocket.emit("get_users", data);
                                
                            } catch(JSONException e){
                                
                                e.printStackTrace();
                                
                            }
                                                                               
                            isConnected = true;
                        }                            
                        
                    }
             
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
                    System.out.println("diconnected");                    
                    isConnected = false;                                   
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
                    System.out.println("Error connecting");                    
                }
                    
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            
                    JSONObject data = (JSONObject) args[0];                    
                    String message;
                    try {
                        
                        message = data.getString("message");
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
//                  create method to print message on screen
//                  addMessage(username, message);
               
        }
    };
    
    private Emitter.Listener onSearchUsers = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            
            System.out.println("args = " + args[0]);
                    
            JSONObject data = (JSONObject) args[0];
            String users;
            try {
                users = data.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            
            //Create method to refresh users conected
            //refresh(user);
        }                                                
        
    };   

//    private Emitter.Listener onAgentJoined = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    int numUsers;
//                    try {
//                        username = data.getString("username");
//                        numUsers = data.getInt("numUsers");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                    
//                            
//        }
//    };

//    private Emitter.Listener onUserLeft = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    int numUsers;
//                    try {
//                        username = data.getString("username");
//                        numUsers = data.getInt("numUsers");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                    //Add method to remove the user
//                    
////                    removeTyping(username);
//             
//        }
//    };   

}
