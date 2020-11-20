package UseCases;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Entities.Message;

public class MessageManager implements Serializable {
    private HashMap<String, HashMap<String, List<Message>>> chats;

    /**
     * The constructor instantiates an empty HashMap.
     * The HashMap's keys are the senders, and the values are HashMaps.
     * The nested HashMaps' keys are the receivers of the sender, and the values are the messages.
     */
    public MessageManager() { this.chats = new HashMap<>(); }

    /** Sends a Message from sender to receiver with the content, at the current time
     *
     * @param sender Sender of Message
     * @param receiver Receiver of Message
     * @param content Content of Message
     */
    public void sendMessage(String sender, String receiver, String content) {
        if (sender.equals(receiver)) {
            addMessage(sender, receiver, content);
        } else {
            addMessage(sender, receiver, content);
            addMessage(receiver, sender, content);
        }
    }

    // Helper method, adds the message to the HashMap chats
    private void addMessage(String sender, String receiver, String content) {
        Message message = new Message(sender, receiver, content);
        addSenderChat(sender);
        addReceiverChat(sender, receiver);
        chats.get(sender).get(receiver).add(message);
    }

    // Helper method, adds a sender key to the HashMap chats if it's not already a key, and map that key to an empty
    // HashMap
    private void addSenderChat(String sender) {
        if (!chats.containsKey(sender)) {
            HashMap<String, List<Message>> receivers = new HashMap<>();
            chats.put(sender, receivers);
        }
    }

    // Helper method, adds a receiver key to the nested HashMap inside chats if it's not already a key, and map that
    // key to an empty list of Messages
    private void addReceiverChat(String sender, String receiver){
        if (!chats.get(sender).containsKey(receiver)) {
            List<Message> chat = new ArrayList<>();
            chats.get(sender).put(receiver, chat);
        }
    }

    /** Deletes the Message chosen by the User
     * Removes the Message from the HashMap chats
     * @param message Message chosen by User
     */
    public void deleteMessage(Message message) {
        String sender = message.getSender();
        String receiver = message.getReceiver();
        List<Message> chat = chats.get(sender).get(receiver);
        int messageIndex = binarySearchMessage(chat, message);
        if(chat.get(messageIndex).getSender().equals(message.getSender())){
            chat.remove(messageIndex);
        } else {
            chat.remove(message);
        }
    }

    // Helper method
    private int binarySearchMessage(List<Message> chat, Message message) {
        LocalDateTime time = message.getTime();
        return binarySearchMessage(chat, 0, chat.size(), time);
    }

    // Helper method
    private int binarySearchMessage(List<Message> chat, int startIndex, int endIndex, LocalDateTime time){
        int midIndex = (startIndex + endIndex) / 2;
        LocalDateTime midMessageTime = (chat.get(midIndex)).getTime();
        if (midMessageTime.isEqual(time)) {
            return midIndex;
        } else if (time.isBefore(midMessageTime)){
            return binarySearchMessage(chat, startIndex, midIndex, time);
        } else {
            return binarySearchMessage(chat, midIndex + 1, endIndex, time);
        }
    }

    /** Implements Getter for getting the inboxes of a user
     *
     * @param user Username of the user requesting to view their inboxes
     * @return List of usernames - the people that user has been messaging
     */
    public List<String> getChats(String user) {
        addSenderChat(user);
        return new ArrayList<>(chats.get(user).keySet());
    }

    /** Implements Getter for getting the inbox between 2 users
     *
     * @param firstUser One user of the inbox
     * @param secondUser Other user of the inbox
     * @return List of Messages between the 2 users
     */
    public List<Message> getChat(String firstUser, String secondUser) {
        return chats.get(firstUser).get(secondUser);
    }

    /** Implements Getter for getting the inbox between 2 users, but in String format
     *
     * @param firstUser One user of the inbox
     * @param secondUser Other user of the inbox
     * @return List of Messages' Contents between the 2 users
     */
    public List<String> getInbox(String firstUser, String secondUser){
        List<Message> messages = chats.get(firstUser).get(secondUser);
        List<String> inbox = new ArrayList<>();
        for(Message message : messages){
            inbox.add(message.getContent());
        }
        return inbox;
    }

    public void messageEvent(String sender, List<String> userList, String content) {
        for (String user : userList) {
            sendMessage(sender, user, content);
        }
    }
}
