import java.io.Serializable;

public class Message implements Serializable {
    protected final MessageType type;
    protected Status status;
    protected String text;

    public Message(){
        this.type = MessageType.TEXT;
        this.status = Status.UNDEFINED;
        this.text = "Undefined";
    }

    public Message(MessageType type, Status status, String text){
        this.type = type;
        this.status = status;
        this.text = text;
    }

    public void setStatus(Status status){
    	this.status = status;
    }

    public void setText(String text){
    	this.text = text;
    }

    public MessageType getType(){
    	return this.type;
    }

    public Status getStatus(){
    	return this.status;
    }

    public String getText(){
    	return this.text;
    }

}
