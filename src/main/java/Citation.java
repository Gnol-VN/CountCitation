public class Citation implements Comparable{
    private int numberOfRepetiton;
    private String citeSymbol;

    public Citation(String citeSymbol) {
        this.citeSymbol = citeSymbol;
    }

    public int getNumberOfRepetiton() {
        return numberOfRepetiton;
    }

    public void setNumberOfRepetiton(int numberOfRepetiton) {
        this.numberOfRepetiton = numberOfRepetiton;
    }

    public String getCiteSymbol() {
        return citeSymbol;
    }

    public void setCiteSymbol(String citeSymbol) {
        this.citeSymbol = citeSymbol;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
