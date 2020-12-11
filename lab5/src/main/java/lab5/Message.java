package lab5;

public class Message {
    private long time;
    private String url;
    public Message(String url, long time){
        this.time = time;
        this.url = url;
    }
    public long getTime(){
        return this.time;
    }
    public void setTime(long time){
        this.time = time;
    }
    public String getUrl(){
        return this.url;
    }
    public void setUrl(String url){
        this.url = url;
    }
}
