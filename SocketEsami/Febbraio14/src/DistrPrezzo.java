public class DistrPrezzo {
    private int prezzo;
    private int distributore;


    public int getDistributore() {
        return distributore;
    }
    public void setDistributore(int distributore) {
        this.distributore = distributore;
    }


    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public DistrPrezzo(int dist, int price) {
        this.distributore = dist;
        this.prezzo = price;
    }
}
