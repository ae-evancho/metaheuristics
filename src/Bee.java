import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bee {
    int[] position;
    int fitness;
    int[] bestSol;
    int bestFit;

    public Bee() {
        position = new int[75];
    }

    public Bee(Bee bee){
        this.position=bee.position;
        this.fitness=bee.fitness;
    }

    public void setBeePosBit(int i, int j){ position[i] = j; }

    public int getBeePosBit(int i) { return position[i]; }

    public void printBee(){
        System.out.println("Position :"); printPosition(this.position);
        System.out.println("Fitness: "+this.fitness);
    }

    public static void printPosition(int[] position){
        for(int i=0; i<75; i++){
            System.out.print(position[i]);
        }
        System.out.println();
    }

    public boolean checkClause(int[] clause){
        for(int i=0; i<75; i++){
            for(int k=0; k<3; k++){
                if(this.abs(clause[k])==(i+1)) {
                        if (clause[k] < 0 && this.position[i] == 0) {
                            return true;
                        } else if (clause[k] > 0 && this.position[i] == 1) {
                            return true;
                        }
                }
            }
        }
        return false;
    }

    public int calculFitness(ArrayList<int[]> clauses) {
        int f = 0; boolean ct = false;
        for (int j = 0; j < clauses.size(); j++) {
            if (checkClause(clauses.get(j)) == true) {
                f++;
            }
        }
        this.fitness = f;
        return this.fitness;
    }

    public int abs (int a)
    {
        return Math.abs(a);
    }

    public int getFitness() {
        return this.fitness ;
    }

    public void localSearch(int searchIter, ArrayList<int[]> clauses){
        int bit;
        Bee temp= new Bee(this);
        int fit=this.calculFitness(clauses);
        int att=0;
        for(int i = 0; i < searchIter; i++){
            att++;
            if(att>searchIter*100) break;
            bit = ThreadLocalRandom.current().nextInt(0,75);
            temp.position[bit]= temp.position[bit]^1;


            int fitTemp=temp.calculFitness(clauses);
            if(fit>fitTemp) {
                temp.position[bit]= temp.position[bit]^1;
                i--;
            }
            else {
                this.bestSol=temp.position.clone();
                this.bestFit=temp.fitness;
            }
        }
    }

    public static int hammDistance(int[] p1, int[] p2){
        int nb=0;
        for (int i=0; i<75; i++)
            nb=nb+(p2[i]^p1[i]);

        return nb;
    }

    }
