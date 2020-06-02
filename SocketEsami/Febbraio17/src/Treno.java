public class Treno {

    private int id;
    private int ritardo;
    private String identita; // identita ultima stazione che ha salvato i dati

    public Treno(int id) {
        this.id = id;
    }

    public int getRitardo() {
        return ritardo;
    }

    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    public String getIdentita() {
        return identita;
    }

    public void setIdentita(String identita) {
        this.identita = identita;
    }
}
