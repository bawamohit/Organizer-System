package Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class is one of the entity classes for this program, specifically for room.
 *
 */
public class Room {
    private final String roomName;
    private int capacity;
    private HashMap<UUID, List<LocalDateTime>> schedule;

    /**
     * The constructor takes name and maximum capacity of attendees allowed of a room and assigns each variable.
     * It also instantiates the schedule, a map of event scheduled at a corresponding time.
     *
     * @param roomName name of room
     * @param capacity capacity of attendees allowed in the room
     */

    public Room(String roomName, int capacity){
        this.roomName = roomName;
        this.capacity = capacity;
        schedule = new HashMap<>();
    }

    /**
     * Implements Getter, getRoomName for roomName.
     *
     * @return name of room
     */
    public String getRoomName(){
        return roomName;
    }

    /**
     * Implements Getter, getCapacity, for capacity.
     *
     * @return maximum capacity of attendees allowed in the room
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * Implements Getter, getRoomEventIDs, for keys of schedule.
     *
     * @return list of events (identified by ID) occurring in the room
     */
    public List<UUID> getRoomEventIDs(){
        return new ArrayList<>(schedule.keySet());
    }

    /**
     * Implements Getter, getSchedule, for schedule.
     *
     * @return schedule for room
     */
    public HashMap<UUID, List<LocalDateTime>> getSchedule(){
        return schedule;
    }

    /**
     * Implements Setter, setRoomSchedule, for this room.
     *
     * @param schedule updated schedule of room
     */
    public void setRoomSchedule(HashMap<UUID, List<LocalDateTime>> schedule){
        this.schedule = schedule;
    }

}
