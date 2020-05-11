import java.util.ArrayList;

public class Mtransferencia {

    private double[] pedidos1 = {0.0564, 0.1036,0.1496,0.1208,0.1184,0.1076,0.0976,0.0712,0.0620,0.0504,0.0312,0.0232,0.0080};
    private double[] entregas1= {0.0128,0.0636,0.1176,0.1780,0.2072,0.1564,0.1136 ,0.0708,0.0440,0.0208,0.0088,0.0044,0.0020};
    private double[] pedidos2={0.0340,0.0724,0.1204,0.1456,0.1356,0.1240,0.1004,0.0828,0.0612,0.0512,0.0368,0.0284,0.0072};
    private double[] entregas2= {0.0224,0.0828,0.1788,0.2112,0.1836,0.1452,0.0920,0.0468,0.0264,0.0072,0.0024,0.0004,0.0008};
    public double[][] nTmax;
    public double[][] recebe1F1_F2_MAX;
    public double[][] recebe2F1_F2_MAX;
    public double[][] recebe3F1_F2_MAX;
    public double[][] transfere1F1_F2_MAX;
    public double[][] transfere2F1_F2_MAX;
    public double[][] transfere3F1_F2_MAX;



    private double[][] mudaestado(int filial){
        double[][] matrix = new double[13][13];
        double p= 0;
        int count;
        for(int inicial = 0; inicial < 13;inicial++ ) { // faz a natrix td

            for (int finall = 0; finall < 13; finall ++) { // faz uma linha

                p += getParticularCase(inicial, finall, filial); // faz os casos particulares

                for (count = inicial; count <= 12; count++) { // faz as contas para uma posicao

                    if (filial == 1) { // isto é so para ver qual das filiais é que estamos a fazer
                        p += entregas1[finall] * pedidos1[count];
                    } else if (filial == 2) {
                        p += entregas2[finall] * pedidos2[count];
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
        }else{ // caso a diff seja maior que zero
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



    //P(a,b)-(c,d) = F(a,c)*F(b,d)
    private double[][] multiplyMatrix(double[][] filial1,double[][] filial2){
        double[][] matrix = new double[169][169];
        int linha = 0;
        int coluna = 0;
        for(int a = 0; a <13; a++){

            for(int b =0; b<13;b++) {

                for (int c = 0; c < 13; c++) {

                    for (int d = 0; d < 13; d++) {
                        //P(a,b)-(c,d) = F(a,c)*F(b,d)
                        matrix[linha][coluna] = filial1[a][c] * filial2[b][d];
                        coluna++;
                    }

                }
                linha++;
                coluna = 0;
            }
        }

        return matrix;
    }
    


    public double[][] getMatriz(int i){
        if(i == 0){
            return this.nTmax;
        }else if(i == 1){
            return this.recebe1F1_F2_MAX;
        }else if(i == 2){
            return this.recebe2F1_F2_MAX;
        }else if(i == 3){
            return this.recebe3F1_F2_MAX;
        }else if(i == 4){
            return this.transfere1F1_F2_MAX;

        }else if(i == 5){
            return this.transfere2F1_F2_MAX;
        }else if(i == 6){
            return this.transfere3F1_F2_MAX;
        }
        return new double[1][1];
    }

    private double[][] shiftReceberX(double[][] nt,int x){
        double[][]recebe3 = new double[13][13];
        for(int j = 0;j < x;j++ ) {
            for (int i = 0; i < 13; i++) { // primeira c's coluna l-c sao sempre 0 visto que é impossivel acontecer visto que assumimos que recebe sempre x carros
                recebe3[i][j] = 0;
            }
        }

        for(int c = x;c < 13;c++){
            for(int l = 0;l < 13;l++){
                if(c == 12){
                    recebe3[l][c] = nt[l][c-1] + nt[l][c];
                }else{
                    recebe3[l][c] = nt[l][c-1];
                }

            }

        }


        return recebe3;
    }

    private double[][] shiftTransfereX(double[][] nt,int x){

        int maxCol = 13-x;
        double[][] transferex = new double[13][13];
        double tmp = 0.0;
        for(int l = 0; l< 13;l++){
            for(int c = 0; c< maxCol;c++){
                if(c == 0){
                    transferex[l][c] = nt[l][c] + nt[l][c + 1];
                }else {
                    transferex[l][c] = nt[l][c + 1];
                }
            }
        }


        return transferex;
    }





    public Mtransferencia(){

        //nao transfere, filial 1, filial 2 e matriz 169x169 -- Verified
        double[][] nTF1= mudaestado(1);
        double[][] nTF2= mudaestado(2);
        this.nTmax = multiplyMatrix(nTF1,nTF2);

        //receber 1/2/3 filial 1 -- Verified
        double[][] recebe1F1 = shiftReceberX(nTF1,1);
        double[][] recebe2F1 = shiftReceberX(recebe1F1,2);
        double[][] recebe3F1 = shiftReceberX(recebe2F1,3);

        //transferir 1/2/3 filial 1 -- Verified
        double[][] transfere1F1 = shiftTransfereX(nTF1,1);
        double[][] transfere2F1 = shiftTransfereX(transfere1F1,1);
        double[][] transfere3F1 = shiftTransfereX(transfere2F1,1);

        //receber 1/2/3 filial 2 -- Verified
        double[][] recebe1F2 = shiftReceberX(nTF2,1);
        double[][] recebe2F2 = shiftReceberX(recebe1F2,2);
        double[][] recebe3F2 = shiftReceberX(recebe2F2,3);

        //transferir 1/2/3 filial 2 -- Verified
        double[][] transfere1F2 = shiftTransfereX(nTF2,1);
        double[][] transfere2F2 = shiftTransfereX(transfere1F2,1);
        double[][] transfere3F2 = shiftTransfereX(transfere2F2,1);

        // FILIAL 1   FILIAL 2
        //
        // recebe 1 transfere 1 -- Verified
        this.recebe1F1_F2_MAX= multiplyMatrix(recebe1F1,transfere1F2);

        // recebe 2 transfere 2 -- Verified
        this.recebe2F1_F2_MAX= multiplyMatrix(recebe2F1,transfere2F2);

        // recebe 3 transfere 3 -- Verified
        this.recebe3F1_F2_MAX= multiplyMatrix(recebe3F1,transfere3F2);

        // transfere 1 recebe 1 -- Verified
        this.transfere1F1_F2_MAX= multiplyMatrix(transfere1F1,recebe1F2);

        // transfere 2 recebe 2 -- Verified
        this.transfere2F1_F2_MAX= multiplyMatrix(transfere2F1,recebe2F2);

        // transfere 3 recebe 3 -- Verified
        this.transfere3F1_F2_MAX= multiplyMatrix(transfere3F1,recebe3F2);



    }




















}
