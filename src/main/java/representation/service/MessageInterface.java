package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */
public class MessageInterface {
    protected String MessageID;
    protected MessageInterfaceType Type;
    protected Message In;
    protected Message Out;

    public MessageInterface() {
    }

    public MessageInterface(String MessageID, MessageInterfaceType type, Message in, Message out) {
        this.MessageID = MessageID;
        Type = type;
        In = in;
        Out = out;
    }

    public MessageInterface(String MessageID, MessageInterfaceType type) {
        this.MessageID = MessageID;
        Type = type;
    }
    
    

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String messageID) {
        this.MessageID = messageID;
    }

    public MessageInterfaceType getType() {
        return Type;
    }

    public void setType(MessageInterfaceType type) {
        Type = type;
    }

    public Message getIn() {
        return In;
    }

    public void setIn(Message in) {
        In = in;
    }

    public Message getOut() {
        return Out;
    }

    public void setOut(Message out) {
        Out = out;
    }
}
