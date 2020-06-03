public class Msg {
    private String message;
    private String sender;
    private String receiver;

    public Msg(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessage() {
        return this.message;
    }
    
    public String getSender() {
        return this.sender;
    }

    public String getReceiver() {
        return this.receiver;
    }
}
