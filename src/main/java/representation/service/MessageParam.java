package representation.service;

/**
 * Created by Timur on 1/14/2016.
 */
public class MessageParam {
    protected String Name;
    protected MessageParamType Type;

    public MessageParam() {
    }

    public MessageParam(String name, MessageParamType type) {
        Name = name;
        Type = type;
    }

    public MessageParamType getType() {
        return Type;
    }

    public void setType(MessageParamType type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
