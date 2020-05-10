public class Mcusto {

    private double[] pedidos1 = {0.0564, 0.1036,0.1496,0.1208,0.1184,0.1076,0.0976,0.0712,0.0620,0.0504,0.0312,0.0232,0.0080};
    private double[] entregas1= {0.0128,0.0636,0.1176,0.1780,0.2072,0.1564,0.1136 ,0.0708,0.0440,0.0208,0.0088,0.0044,0.0020};
    private double[] pedidos2={0.0340,0.0724,0.1204,0.1456,0.1356,0.1240,0.1004,0.0828,0.0612,0.0512,0.0368,0.0284,0.0072};
    private double[] entregas2= {0.0224,0.0828,0.1788,0.2112,0.1836,0.1452,0.0920,0.0468,0.0264,0.0072,0.0024,0.0004,0.0008};

    public double[][] mudaestado(int filial){
        double[][] matrix = new double[13][13];
        double p= 0;
        int counter = 0;
        int count;
        for(int inicial = 0; inicial < 13;inicial++ ) { // faz a natrix td

            for (int finall = 0; finall < 13; finall ++) { // faz uma linha

                p += getParticularCase(inicial, finall, filial); // faz os casos particulares

                for (counter = count = inicial; count <= 12; count++) { // faz as contas para uma posicao

                    if (filial == 1) { // isto é so para ver qual das filiais é que estamos a fazer
                        p += 30* counter* entregas1[finall] * pedidos1[count];
                    } else if (filial == 2) {
                        p += 30* counter* entregas2[finall] * pedidos2[count];
                    }
                }
                matrix[inicial][finall] = p; // dps da conta estar feita guarda na matrix

                p = 0;

            }
            p = 0;
        }
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

        // casos que o algoritmo de cima nao cobre que dependem da diferença entre estados
        if(diff < 0 ){
            while(tmpfinal>= 0){
                if(filial == 1){
                    res += 30 * tmpinicial * entregas1[tmpfinal] * pedidos1[tmpinicial];

                }else if(filial == 2){
                    res += 30 * tmpinicial * entregas2[tmpfinal] * pedidos2[tmpinicial];

                }
                tmpfinal--;
                tmpinicial--;
            }

        }else if(diff == 0){
            for(int i = 0; i < inicial;i++){
                if(filial == 1){
                    res += 30 * i * entregas1[i] * pedidos1[i];

                }else if(filial == 2){
                    res +=  30 * i * entregas2[i] * pedidos2[i];

                }

            }
        }else{ // caso a diff seja maior que zero
            while (tmpdiff < finall) {
                if (filial == 1) {
                    res += 30 * count * entregas1[diff + count] * pedidos1[count];

                } else if (filial == 2) {
                    res += 30 * count * entregas2[diff + count] * pedidos2[count];
                }
                count++;
                tmpdiff++;
            }
        }

        // caso especial para o estado 12 que temos de fazer as contas para a possiblidades de overflow
        res += addOverflow(inicial,finall,diff,filial);

        return res;

    }

    private double addOverflow(int inicial,int finall,int diff,int filial){
        double res = 0.0;
        if(finall==12){
            int tmpoverflow = 12;
            int tmp = inicial;
            int c = 0;
            while (tmpoverflow > diff){
                while (c < tmp) {
                    if (filial == 1) {
                        res += 30 * c * entregas1[tmpoverflow] * pedidos1[c];

                    } else if (filial == 2) {
                        res += 30 * c * entregas2[tmpoverflow] * pedidos2[c];
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



}
