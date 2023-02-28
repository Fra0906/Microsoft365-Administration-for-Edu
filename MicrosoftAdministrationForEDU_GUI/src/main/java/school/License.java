package school;

public class License {


    private String nome;
    private Integer totali;
    private Integer usati;

    @Override
    public String toString() {
        return nome + " [usati:" + usati + "/"  + totali + "]";
    }

    public License(String nome, Integer totali, Integer usati) {
        this.nome = nome;
        this.totali = totali;
        this.usati = usati;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTotali() {
        return totali;
    }

    public void setTotali(Integer totali) {
        this.totali = totali;
    }

    public Integer getUsati() {
        return usati;
    }

    public void setUsati(Integer usati) {
        this.usati = usati;
    }






}
