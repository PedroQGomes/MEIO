

public class Main {

    public static void main(String[] args){
        Mcusto m = new Mcusto();
        Mtransferencia mt = new Mtransferencia();
        double[][] teste = m.mudaestado(1);
        mt.sumline(teste,13,13);
        mt.printMatrix(teste,13,13);

    }
}
