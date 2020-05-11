import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        Mcusto mCusto = new Mcusto();
        Mtransferencia mTransf = new Mtransferencia();
        //matrixToExel(mTransf,mCusto);

        mkmodel(mTransf,mCusto);

    }


    private static void matrixToExel(Mtransferencia transf,Mcusto custo){
        String FILE_NAME;

        FILE_NAME = "tmp.xlsx";

        //FILE_NAME = "Mcustos.xlsx";


        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("transfere 3");
        double[][] matriz = custo.getMatrizCusto(6);



        int rowNum = 0;
        int colNum = 0;


        for (int l = 0; l < 169;l++){
            Row row = sheet.createRow(rowNum++);
            colNum = 0;
            for(int c = 0; c < 169;c++) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(matriz[l][c]);
            }
        }



        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");




    }



    public static void mkmodel(Mtransferencia transf,Mcusto custo){
        double[] Fn = new double[169];    // alterado
        double[] FnCurr = new double[169];    // alterado
        int[] logs = new int[169];
        double[][] Qn = new double[169][7];        // alterado
        double[][] Vn = new double[169][7]; // alterado
           // alterado
        double[][] tmptransf;          // alterado
        double[][] tmpcusto;           // alterado
        int maxiter = 200;

        double[][] Dn = new double[maxiter][169];
        //calcular Qn -- pode ser calculado fora visto que nunca mais é alterado
        Qn = calculateQn(transf,custo);




        for(int iter = 0; iter < maxiter; iter++){
            double[][] Pnfn_1 = new double[169][7];
            //calcular Pnfn_1 = Pn * Fn-1
            for(int i = 0; i < 7;i++) { // isto é o ciclo tem vai buscar as 7 matrizes de probabilidade 7
                tmptransf = transf.getMatriz(i);
                for (int l = 0; l < 169; l++) { //169
                    for (int c = 0; c < 169; c++) {
                        Pnfn_1[l][i] += tmptransf[l][c] * Fn[c];
                    }

                }
            }

            //calcular Vn
            for(int i = 0; i < 7;i++) { // isto é o ciclo tem vai buscar as 7 matrizes de probabilidade 7
                for (int l = 0; l < 169; l++) { // 169
                    Vn[l][i] = Pnfn_1[l][i] + Qn[l][i];
                }
            }

            //calcular Fn
            for (int count = 0; count < 169; count ++) { //169
                double tmp = Vn[count][0];
                for (int matrix = 1; matrix < 7; matrix ++) { // 7
                    if(tmp > Vn[count][matrix]){ // nao tenho a certeza se é para escolher o > ou o menor
                        tmp = Vn[count][matrix];
                    }
                }
                FnCurr[count] = tmp;
            }


            //calcular Dn
            for(int c = 0; c < 169; c ++){ // 169
                Dn[iter][c] = FnCurr[c] - Fn[c];
            }

            // atualizar o fn
            for(int u = 0; u < 169; u ++){ // 169
                Fn[u] = FnCurr[u];
            }



        }


        System.out.print("\n");
        System.out.println("print do dn");
        for(int b = 0; b < maxiter;b++) {
            for (int a = 0; a < 169; a++) { // 169
                System.out.print(Dn[b][a]);
                System.out.print(", ");
            }
            System.out.print("\n");

        }

    }






    private static double[][] calculateQn(Mtransferencia transf,Mcusto custo){
        double[][] Qn = new double[169][7];
        double[][] tmptransf;
        double[][] tmpcusto;
        for(int i = 0; i < 7;i++) {
            tmptransf = transf.getMatriz(i);
            tmpcusto = custo.getMatrizCusto(i);
            for (int l = 0; l < 169; l++) {
                for (int c = 0; c < 169; c++) {
                    Qn[l][i] += tmptransf[l][c] * tmpcusto[l][c];
                }
                if(Qn[l][i] <0){
                    Qn[l][i] = Qn[l][i]*(-1);
                }


            }
        }

        return Qn;
    }

    // made to just verifie if the lines are correctly done
    private static void sumline(double[][] m,int linhas,int colunas){
        double count = 0.0;
        for(int a = 0; a <linhas; a++){

            for(int b =0; b<colunas;b++) {
                count += m[a][b];

            }
            System.out.println(count);
            count = 0;

        }

    }

    // made to just verifie if the matrix is correctly done
    public static void printMatrix(double[][] matrix,int linhas, int colunas){
        for(int i = 0; i< linhas;i++){
            for(int j = 0; j< colunas;j++){
                System.out.print(matrix[i][j]+",");
            }
            System.out.print("\n");
        }
    }
}
