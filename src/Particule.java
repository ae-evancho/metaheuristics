import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Particule {
    int[] position;
    int fitness;
    int[] pBest;
    int pBestFitness;
    int velocity;

    public Particule(int vMax) {
        position = new int[75];
        this.velocity = ThreadLocalRandom.current().nextInt(0,vMax+1);
        this.pBest = this.position;
    }

    public Particule(Particule part){
        this.position=part.position;
        this.velocity=part.velocity;
        this.pBest=part.pBest;
        this.pBestFitness=part.pBestFitness;
        this.fitness=part.fitness;
    }

    public void setPartBit(int i, int j){ position[i] = j; }

    public int getPartBit(int i) { return position[i]; }

    public void printPart(){
        System.out.println("Position :"); printPosition(this.position);

        System.out.println("Best Position :"); printPosition(this.pBest);

        System.out.print("Velocity:"+this.velocity+" Fitness:"+this.fitness);
    }

    public static void printPosition(int[] position){
        for(int i=0; i<75; i++){
            System.out.print(position[i]);
        }
        System.out.println();
    }

    public boolean checkClause(int[] clause, int pp){
        for(int i=0; i<75; i++){
            for(int k=0; k<3; k++){
                if(this.abs(clause[k])==(i+1)) {
                    if(pp == 1) {
                        if (clause[k] < 0 && this.position[i] == 0) {
                            return true;
                        } else if (clause[k] > 0 && this.position[i] == 1) {
                            return true;
                        }
                    }
                    else {
                        if (clause[k] < 0 && this.pBest[i] == 0) {
                            return true;
                        } else if (clause[k] > 0 && this.pBest[i] == 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int calculFitness(ArrayList<int[]> clauses, int i) {
        int f = 0; boolean ct = false;
        for (int j = 0; j < clauses.size(); j++) {
            if (checkClause(clauses.get(j), i) == true) {
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

    public int hammDistance(int[] p){
        int nb=0;
        for (int i=0; i<75; i++)
            nb=nb+(this.position[i]^p[i]);

        return nb;
    }

    }
