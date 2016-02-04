package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */
public class MessageInterface {
    protected String InterfaceID;
    protected MessageInterfaceType Type;
    protected Message In;
    protected Message Out;

    public MessageInterface() {
    }

    public MessageInterface(String InterfaceID, MessageInterfaceType type, Message in, Message out) {
        this.InterfaceID = InterfaceID;
        Type = type;
        In = in;
        Out = out;
    }

    public MessageInterface(String InterfaceID, MessageInterfaceType type) {
        this.InterfaceID = InterfaceID;
        Type = type;
    }
    
    

    public String getInterfaceID() {
        return InterfaceID;
    }

    public void setInterfaceID(String interfaceID) {
        this.InterfaceID = interfaceID;
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
