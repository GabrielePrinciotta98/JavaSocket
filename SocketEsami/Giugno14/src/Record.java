public class Record {

    private char tempo = '-';
    private int feedback = 0;
    private String id;

    public Record(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public char getTempo() {
        return tempo;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setTempo(char tempo) {
        this.tempo = tempo;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

}
