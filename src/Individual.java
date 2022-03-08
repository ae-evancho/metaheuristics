
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Individual {
    int [] indiv;
    int fitness;
    int ff;

    public Individual() {
        this.indiv = new int[75];
    }

    public void setIndivGene(int i, int j){
        indiv[i]=j;
    }

    public int getIndivGene(int i) {
        return indiv[i];
    }

    public String printIndiv(){
        String res="";
        for(int i=0; i<75;i++) res=res+this.indiv[i];

        return res;
    }

    public boolean checkClause(int[] clause){
        for(int i=0; i<75;i++){
            for(int k=0;k<3;k++){
                if(this.abs(clause[k])==(i+1)) {
                    if (clause[k] < 0 && this.indiv[i] == 0) {
                         return true;}
                    else if (clause[k] > 0 && this.indiv[i] == 1)  {
                        return true;}
                }
            }
        }
        return false;
    }

    public int [] fitness(ArrayList<int[]> clauses, int [] w) {
        int f=0; int ff=0; boolean ct=false;
        int [] y= new int[325];
        for (int j = 0; j < clauses.size(); j++) {
            if (checkClause(clauses.get(j)) == false) {
                f=f+w[j]; y[j]=1;
            }
            else ff=ff+1;
        }
        this.fitness=f;
        this.ff=ff;
        return y;
    }

    public static Individual crossOver(Individual i1, Individual i2){
        Individual offs1 = new Individual();
        int crossPoint = ThreadLocalRandom.current().nextInt(5,70);

        for (int i = 0; i < 75; i++) {
            if(i<crossPoint)
            offs1.setIndivGene(i, i2.getIndivGene(i));
        else
            offs1.setIndivGene(i, i1.getIndivGene(i));
        }

        return offs1;
    }

    public void mutateGenes(double mutePer){
 //     boolean ovfl=false;
        for(int j=0;j<75*mutePer;j++) {

                int i = ThreadLocalRandom.current().nextInt(75);
                //   if(i>66) ovfl=true;

                if (this.getIndivGene(i) == 0) this.setIndivGene(i, 1);
                else this.setIndivGene(i, 0);
            }

//      if(!ovfl)i++; else i--;


    }

        public int abs (int a)
        {
            return java.lang.Math.abs(a);
        }

        public boolean bool (int a)
        {
            if(a==0) return false;
            else if(a==1) return true;
            else return true;
        }

        public boolean neg (int a)
        {
            if(a<0) return true;
            else return false;
        }

        public boolean not (boolean a)
        {
            if(a==true) return false;
            else return true;
        }

    public int getFitness() {
        return this.fitness ;
    }



}
