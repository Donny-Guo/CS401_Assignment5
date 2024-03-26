import java.io.Serializable;

public class Message implements Serializable {
    protected String type;
    protected String status;
    protected String text;

    public Message(){
        this.type = "Undefined";
        this.status = "Undefined";
        this.text = "Undefined";
    }

    public Message(String type, String status, String text){
        this.type = setType(type);
        this.status = setStatus(status);
        this.text = setText(text);
    }

    private void setType(String type){
    }

    private void setStatus(String status){
    }

    private void setText(String text){
    }

    public String getType(){
    }

    public String getStatus(){
    }

    public String getText(){
    }

}
