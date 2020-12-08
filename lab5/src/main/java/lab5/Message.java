package lab5;

public class Message {
    private Long time;
    private String url;
    public Message(String url, Long time){
        this.time = time;
        this.url = url;
    }
    public Long getTime(){
        return this.time;
    }
    public void setTime(Long time){
        this.time = time;
    }
    public String getUrl(){
        return this.url;
    }
    public void setUrl(String url){
        this.url = url;
    }
}
