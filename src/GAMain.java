import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GAMain implements Runnable {
    public static int iteration;
    public static int fitness;
    public static int nbrpopul;
    public static double muterate;
    public static double crossrate;
    public static int genmax;
    public static TextField result;
    public static TextField time;
    public String p;

    public GAMain(int a,double b,double c,int d, TextField r, TextField t,String p){
        this.p=p;
        nbrpopul=a;
        muterate=b;
        crossrate=c;
        genmax=d;
        result=r;
        time=t;
    }
    public GAMain(int a,double b,double c,int d,String p){
        this.p=p;
        nbrpopul=a;
        muterate=b;
        crossrate=c;
        genmax=d;
        result=new TextField();
        time=new TextField();
    }
    @Override
    public void run() {
        int nbrPopul=nbrpopul;
        double muteRate=muterate;
        double crossRate=crossrate;
        int genMax=genmax;

        String path = p;
        File cnf= new File(path);
        ArrayList<int[]> clauses= new ArrayList<int[]>();
        try {
            clauses=extractClause(cnf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PopulationGA pop = new PopulationGA(nbrPopul);
        pop.selection(clauses);


        int test=0;
        int cte=0;

        double start=System.currentTimeMillis();

        while(pop.terminCond(genMax)==false) {

            pop.createNewGeneration(crossRate, clauses);
            pop.mutation(muteRate);
            pop.selection(clauses);

            if(cte%1250==0) {

                if (test == pop.getPopul().get(0).getFitness()) {
                    pop.repopulate();
                    pop.selection(clauses);
                }

                test=pop.getPopul().get(0).getFitness();
            }
            cte++;

            System.out.println("Iteration: "+pop.generation+" Fitness: "+pop.getPopul().get(0).ff);

            iteration=pop.generation;
            fitness=pop.getPopul().get(0).ff;
        }

        if(pop.getPopul().get(0).getFitness()==0) {
            result.setText(pop.getPopul().get(0).printIndiv());
        }
        else
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
