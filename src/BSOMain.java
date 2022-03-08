import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class BSOMain implements Runnable {
    public static int iteration;
    public static int fitness;
    public static int nbrpopul;
    public static int itermax;
    public static int flip;
    public static int maxchances;
    public static int searchiter;
    public static TextField result;
    public static TextField time;
    public String p;

    public BSOMain(int a,int b,int c,int d,int e, TextField r, TextField t,String p){
        this.p=p;
        nbrpopul=a;
        itermax=b;
        flip=c;
        maxchances=d;
        searchiter=e;
        result=r;
        time=t;
    }
    public BSOMain(int a,int b,int c,int d,int e,String p){
        this.p=p;
        nbrpopul=a;
        itermax=b;
        flip=c;
        maxchances=d;
        searchiter=e;
        result=new TextField();
        time=new TextField();
    }
    @Override
    public void run() {
        int nbrPopul=nbrpopul;
        int iterMax=itermax;
        int flip=this.flip;
        int maxChances=maxchances;
        int searchIter=searchiter;
        int x=0,y=0;


        String path = this.p;
        File cnf = new File(path);
        ArrayList<int[]> clauses= new ArrayList<int[]>();
        try {
            clauses = extractClause(cnf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PopulationBee p = new PopulationBee(nbrPopul, clauses, maxChances);


        double start = System.currentTimeMillis();

        Bee beeInit = new Bee();

        for (int j = 0; j < 75; j++) beeInit.setBeePosBit(j, ThreadLocalRandom.current().nextInt(0, 2));
        beeInit.calculFitness(clauses);

        int searchArea[][];
        doubleWrapper sRef = new doubleWrapper(beeInit.position, beeInit.fitness);


        while (!p.terminCond(iterMax, sRef)) {
            p.tabooT.add(sRef);
            searchArea = p.calculSearchArea(sRef.bin, flip);
            p.initBees(searchArea);
            for (Bee rBee : p.populationVect) {
                rBee.localSearch(searchIter, clauses);
            }
            p.updateDance();
            sRef = p.selectRef(sRef, maxChances, clauses);

            p.nbIteration++;

            System.out.println("Iteration: "+p.nbIteration+" Fitness: "+sRef.fitness);
            iteration = p.nbIteration;
            fitness = sRef.fitness;

        }

        if (sRef.fitness == 325) {
            System.out.println("Solution found");
            p.printSolution(sRef, result);
        } else
            result.setText("No solution was found");
        double end = System.currentTimeMillis();
        time.setText((end-start)+"ms");



    }

    public static ArrayList<int[]> extractClause(File cnf) throws FileNotFoundException {
        Scanner reader = new Scanner(cnf);
        ArrayList<String> fich = new ArrayList<>();
        while (reader.hasNextLine()) {
            String clause = reader.nextLine();
            if (!clause.startsWith("c") && !clause.startsWith("p") && !clause.startsWith("%"))
                fich.add(clause);
        }
        reader.close();

        fich.remove("\n");
        fich.remove(fich.size() - 1);
        fich.remove(fich.size() - 1);

        ArrayList<int[]> clauses = new ArrayList<int[]>();
        int[] temp = new int[3];


        String[] s = fich.get(0).split(" ");
        temp[0] = Integer.parseInt(s[1]);
        temp[1] = Integer.parseInt(s[2]);
        temp[2] = Integer.parseInt(s[3]);
        clauses.add(temp);

        int i = 1;
        while (i < fich.size()) {
            s = fich.get(i).split(" ");
            temp = new int[3];
            temp[0] = Integer.parseInt(s[0]);
            temp[1] = Integer.parseInt(s[1]);
            temp[2] = Integer.parseInt(s[2]);
            clauses.add(temp);
            i++;
        }
        return clauses;
    }

}
