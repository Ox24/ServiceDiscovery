package representation.service;

import java.util.List;

/**
 * Created by Timur on 1/14/2016.
 */
public class Message {
    String ID;
    MessageType Type;
    List<MessageParam> Params;

    public Message(String ID, MessageType type, List<MessageParam> params) {
        this.ID = ID;
        Type = type;
        Params = params;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public MessageType getType() {
        return Type;
    }

    public void setType(MessageType type) {
        Type = type;
    }

    public List<MessageParam> getParams() {
        return Params;
    }

    public void setParams(List<MessageParam> params) {
        Params = params;
    }
}
