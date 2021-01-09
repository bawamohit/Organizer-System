package UseCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import Entities.Message;

public class MessageManager {
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
        Message message = new Message(sender, receiver, content);
        if (sender.equals(receiver)) {
            addMessage(sender, receiver, message);
        } else {
            addMessage(sender, receiver, message);
            addMessage(receiver, sender, message);
        }
    }

    // Helper method, adds the message to the HashMap chats
    private void addMessage(String sender, String receiver, Message message) {
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

    /** Adds a Message from sender to receiver with the content, at the set time, and only records in sender's chat
     *
     * @param sender Sender of Message
     * @param receiver Receiver of Message
     * @param content Content of Message
     * @param time Time of Message
     */
    public void addToSenderChat(String sender, String receiver, String content, LocalDateTime time) {
        Message message = new Message(sender, receiver, content, time);
        addMessage(sender, receiver, message);
    }

    /** Adds a Message from sender to receiver with the content, at the set time, and only records in receiver's chat
     *
     * @param sender Sender of Message
     * @param receiver Receiver of Message
     * @param content Content of Message
     * @param time Time of Message
     */
    public void addToReceiverChat(String sender, String receiver, String content, LocalDateTime time) {
        Message message = new Message(sender, receiver, content, time);
        addMessage(receiver, sender, message);
    }

    /** Sends a message from the User to everyone in the userList
     *
     * @param sender Sender of Message
     * @param userList List of Receivers
     * @param content Content of Message
     */
    public void messageAll(String sender, List<String> userList, String content) {
        for (String user : userList) {
            sendMessage(sender, user, content);
        }
    }

    /**
     * Returns the list of users that have chats.
     *
     * @return List of usernames
     */
    public List<String> getUsers() {
        return new ArrayList<>(chats.keySet());
    }

    /** Implements Getter for getting the inboxes of a user
     * If the user hasn't messaged anyone, add the sender key to the HashMap first.
     * @param user Username of the user requesting to view their inboxes
     * @return List of usernames - the people that user has been messaging
     */
    public List<String> getInboxes(String user) {
        addSenderChat(user);
        return sortInbox(user, new ArrayList<>(chats.get(user).keySet()));
    }

    /** Helper to sort inboxes by latest message time
     * @param user User of which we want to sort inbox
     * @param inboxList Inboxes of user
     * @return sorted inbox list of User
     */
    private List<String> sortInbox(String user, List<String> inboxList) {
        List<Message> lastMessageList = new ArrayList<>();
        List<Message> lastMessageListSorted = new ArrayList<>();
        List<String> lastMessageTimeList = new ArrayList<>();
        List<String> inboxListSorted = new ArrayList<>();
        for (String contact : inboxList) {
            int chatSize = chats.get(user).get(contact).size();
            Message lastMessage = chats.get(user).get(contact).get(chatSize - 1);
            lastMessageList.add(lastMessage);
            if (!lastMessageTimeList.contains(lastMessage.getTime().toString())) {
                lastMessageTimeList.add(lastMessage.getTime().toString());
            }
        }
        Collections.sort(lastMessageTimeList);
        for (String time : lastMessageTimeList) {
            for (Message message : lastMessageList) {
                if (time.equals(message.getTime().toString())) {
                    lastMessageListSorted.add(message);
                }
            }
        }
        for (Message message : lastMessageListSorted) {
            for (String contact : inboxList) {
                if (chats.get(user).get(contact).contains(message)) {
                    inboxListSorted.add(contact);
                }
            }
        }
        return inboxListSorted;
    }

    /** Implements Getter for getting the inbox between 2 users
     *
     * @param firstUser One user of the inbox
     * @param secondUser Other user of the inbox
     * @return List of Messages between the 2 users
     */
    public List<Message> getInbox(String firstUser, String secondUser) {
        return chats.get(firstUser).get(secondUser);
    }

    /** Implements Getter for getting the inbox between 2 users, but in String format
     *
     * @param firstUser One user of the inbox
     * @param secondUser Other user of the inbox
     * @return List of Messages' Contents between the 2 users
     */
    public List<List<String>> getInboxStringGUI(String firstUser, String secondUser){
        List<Message> messages = chats.get(firstUser).get(secondUser);
        List<List<String>> inbox = new ArrayList<>();
        if(messages == null) return inbox;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(Message message : messages){
            List<String> messageInfo = new ArrayList<>();
            messageInfo.add(message.getSender());
            messageInfo.add(message.getTime().format(formatter));
            messageInfo.add(message.getContent());
            inbox.add(messageInfo);
        }
        return inbox;
    }

    /** Deletes inbox between 2 Users
     *
     * @param username1 User 1
     * @param username2 User 2
     */
    public void deleteMutualThread(String username1, String username2){
        chats.get(username1).remove(username2);
        chats.get(username2).remove(username1);
    }
}
