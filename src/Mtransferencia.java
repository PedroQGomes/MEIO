import java.util.ArrayList;

public class Mtransferencia {

    private double[] pedidos1 = {0.0564, 0.1036,0.1496,0.1208,0.1184,0.1076,0.0976,0.0712,0.0620,0.0504,0.0312,0.0232,0.0080};
    private double[] entregas1= {0.0128,0.0636,0.1176,0.1780,0.2072,0.1564,0.1136 ,0.0708,0.0440,0.0208,0.0088,0.0044,0.0020};
    private double[] pedidos2={0.0340,0.0724,0.1204,0.1456,0.1356,0.1240,0.1004,0.0828,0.0612,0.0512,0.0368,0.0284,0.0072};
    private double[] entregas2= {0.0224,0.0828,0.1788,0.2112,0.1836,0.1452,0.0920,0.0468,0.0264,0.0072,0.0024,0.0004,0.0008};
    private double[][] matrix = new double[13][13];


    public double[][] mudaestado(int filial){
        /* vamos experimentar o caso em que muda do estado (1,12) para (4,9) sendo a decisao no final do dia nao transferir
            Na filial 1 isto tem as seguintes possibilidades para isto acontecer
                - 3 entregas e 0 pedidos
                - 4 entregas e 1 pedido
                casos insatisfeitos
                - 4 entregues e 2 pedidos ( 1 bem sucedido e 1 insatisfeito)
                - 4 entregues e 3 pedidos ( 1 bem sucedido e 2 insatisfeito)
                - 4 entregues e 4 pedidos ( 1 bem sucedido e 3 insatisfeito)
                - 4 entregues e 5 pedidos ( 1 bem sucedido e 4 insatisfeito)
                - 4 entregues e 6 pedidos ( 1 bem sucedido e 5 insatisfeito)
                - 4 entregues e 7 pedidos ( 1 bem sucedido e 6 insatisfeito)
                - 4 entregues e 8 pedidos ( 1 bem sucedido e 7 insatisfeito)
                - 4 entregues e 9 pedidos ( 1 bem sucedido e 8 insatisfeito)
                - 4 entregues e 10 pedidos ( 1 bem sucedido e 9 insatisfeito)
                - 4 entregues e 11 pedidos ( 1 bem sucedido e 10 insatisfeito)
                - 4 entregues e 12 pedidos ( 1 bem sucedido e 11 insatisfeito)
        */
        // vamos assumir que estamos sempre na decisao de nao transferir carros da filial 1 para a 2 - equivale - a 1 decisao e fazer os 13 estados e as 13 possilidades para a filial 1 para
        // mas ainda existe o sentido posto da 2 para 1
        // sendo a deicsao nao transferir carros em qualquer decisao
        // os estados possiveis no final do dia(estagio) sao
        // vou fazer a primeira linha a onde o estado inicial é 0 e pode passar para 0 1 2 3 ... 12
        // depois de fazer a 1 linha falta fazer as restantes 12 para a filial 1 e de seguida fazer a final 2 e fazer "merge" para ter a matrix 169 por 169
        double[][] matrix = new double[13][13];
        double p= 0;
        double total = 0;
        int count;
        for(int inicial = 0; inicial < 13;inicial++ ) { // faz a natrix td

            for (int finall = 0; finall < 13; finall ++) { // faz uma linha

                p += getParticularCase(inicial, finall, filial);

                for (count = inicial; count <= 12; count++) { // faz uma conta

                    if (filial == 1) {
                        p += entregas1[finall] * pedidos1[count];
                    } else if (filial == 2) {
                        p += entregas2[finall] * pedidos2[count];
                    }
                }
                matrix[inicial][finall] = p;
                total += p;
                p = 0;

            }
            System.out.println(inicial +" : " + total);
            total = 0;
            p = 0;
        }
        //System.out.println(total);
        //printMatrix(filial);
        return matrix;
    }

    private double getParticularCase(int inicial, int finall,int filial){
        double res = 0.0;
        int count = 0;
        int diff = finall - inicial;
        int tmpdiff = diff;
        int tmpinicial = inicial;
        int tmpfinal = finall;
        tmpfinal--;
        tmpinicial--;

        if(diff < 0 ){
            while(tmpfinal>= 0){
                if(filial == 1){
                    res += entregas1[tmpfinal] * pedidos1[tmpinicial];

                }else if(filial == 2){
                    res += entregas2[tmpfinal] * pedidos2[tmpinicial];

                }
                tmpfinal--;
                tmpinicial--;
            }



        }else if(diff == 0){
            for(int i = 0; i < inicial;i++){
                if(filial == 1){
                    res += entregas1[i] * pedidos1[i];

                }else if(filial == 2){
                    res += entregas2[i] * pedidos2[i];

                }

            }
        }else{
            while (tmpdiff < finall) {
                if (filial == 1) {
                    res += entregas1[diff + count] * pedidos1[count];

                } else if (filial == 2) {
                    res += entregas2[diff + count] * pedidos2[count];
                }
                count++;
                tmpdiff++;
            }
        }
        //System.out.println("res" + finall + ":"+ res);
        if(finall==12){
            int tmpoverflow = 12;
            int tmp = inicial;
            int c = 0;
            while (tmpoverflow > diff){
                while (c < tmp) {
                    if (filial == 1) {
                        res += entregas1[tmpoverflow] * pedidos1[c];

                    } else if (filial == 2) {
                        res += entregas2[tmpoverflow] * pedidos2[c];
                    }
                    c++;
                }
                c = 0;
                tmpoverflow--;
                tmp--;
            }

        }



        return res;

    }


    private void printMatrix(double[][] matrix,int linhas, int colunas){
        for(int i = 0; i< linhas;i++){
            for(int j = 0; j< colunas;j++){
                System.out.println(matrix[i][j]);
            }
        }
    }


    private double[][] multiplyMatrix(double[][] filial1,double[][] filial2){
        double[][] matrix = new double[169][169];



        return matrix;
    }


    public static void main(String[] args){
        Mtransferencia m = new Mtransferencia();
        double[][] filial1= m.mudaestado(1);
        double[][] filial2= m.mudaestado(2);



    }




















}
