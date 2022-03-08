import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PSOMain implements Runnable  {
    public static int iteration;
    public static int fitness;
    public static int nbrpopul;
    public static double w;
    public static int vmax;
    public static double c1,c2;
    public static int itermax;
    public static TextField result;
    public static TextField time;
    public String p;

    public PSOMain(int a,double b,int c,double d,double e, int f, TextField r, TextField t,String p){
        this.p=p;
        nbrpopul=a;
        w=b;
        vmax=c;
        c1=d;
        c2=e;
        itermax=f;
        result=r;
        time=t;
    }
    public PSOMain(int a,double b,int c,double d,double e, int f,String p){
        this.p=p;
        nbrpopul=a;
        w=b;
        vmax=c;
        c1=d;
        c2=e;
        itermax=f;
        result=new TextField();
        time=new TextField();
    }
    @Override
    public void run() {
        int nbrPopul = nbrpopul;
        double w = this.w;
        int vMax=vmax;
        double c1 = this.c1;
        double c2 = this.c2;
        int iterMax = itermax;


        String path = p;
        File cnf = new File(path);
        ArrayList<int[]> clauses= new ArrayList<int[]>();
        try {
            clauses = extractClause(cnf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PopulationPart p = new PopulationPart(nbrPopul, w, c1, c2, vMax, clauses);

        //   p.printSolution();
        double start=System.currentTimeMillis();

        while (!p.terminCond(iterMax)) {

            for (Particule part : p.populationVect) {
                p.updateVP(part, vMax);
                p.flip(part, clauses);
                p.updateBest(part, clauses);
                if (p.gBestFitness == 325) break;
            }
            p.nbIteration++;
            System.out.println("Iteration: "+p.nbIteration+" Fitness: "+p.gBestFitness);

            iteration=p.nbIteration;
            fitness=p.gBestFitness;
        }
        if (p.gBestFitness == 325) {
            String res="";
            System.out.println("Solution found");
            for (int i = 0; i < 75; i++) res=res+p.gBest[i];
            result.setText(res);
        } else
            result.setText("No solution was found");


        double end=System.currentTimeMillis();

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
