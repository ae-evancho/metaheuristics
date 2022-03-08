import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class PopulationPart {
    ArrayList<Particule> populationVect;
    int n;
    int[] gBest;
    int gBestFitness;
    public int nbIteration;
    double inertia, c1, c2;
    int vMax;

    public PopulationPart(int n, double w, double c1, double c2, int vMax, ArrayList<int[]> clauses) {
        int[] temp = new int[75];
        this.populationVect = new ArrayList<Particule>();
        this.n = n;
        this.inertia = w;
        this.c1 = c1;
        this.c2 = c2;
        this.vMax = vMax;
        this.nbIteration = 0;
        gBestFitness = 0;
        Particule rPart;
        for (int i = 0; i < this.n; i++) {
            rPart = new Particule(vMax);
            for (int j = 0; j < 75; j++) rPart.setPartBit(j, ThreadLocalRandom.current().nextInt(0, 2));

            int fit = rPart.calculFitness(clauses, 1);
            rPart.pBestFitness = fit;
            if (fit > gBestFitness) {
                gBestFitness = fit;
                gBest = rPart.position;
            }

            this.populationVect.add(rPart);
        }

    }

    public ArrayList<Particule> getPopulationVect() {
        return this.populationVect;
    }


    public boolean terminCond(int maxIter) {
        if (this.nbIteration >= maxIter || this.gBestFitness == 325)
            return true;
        else return false;
    }

    public void printPopulation() {
        for (int i = 0; i < this.n; i++) {
            this.getPopulationVect().get(i).printPart();
            System.out.println("  Iteration:" + this.nbIteration + "\n");
        }
    }

    public void printSolution() {
        System.out.println("Best: ");
        Particule.printPosition(gBest);
        System.out.println("Fitness:" + this.gBestFitness + "   Iteration:" + this.nbIteration + "\n");
    }

    public void updateVP(Particule part, int vMax) {
        double r1 = ThreadLocalRandom.current().nextDouble(0, 1);
        double r2 = ThreadLocalRandom.current().nextDouble(0, 1);
        part.velocity = (int) (part.velocity * this.inertia + (this.c1 * r1 * part.hammDistance(part.pBest)) + (this.c2 * r2 * part.hammDistance(this.gBest)));

        if (part.velocity > vMax) part.velocity = vMax;
    }

    public void flip(Particule part, ArrayList<int[]> clauses) {
        int bit;
        Particule temp = new Particule(part);
        int fit = part.getFitness();
        for (int i = 0; i < part.velocity; i++) {
            bit = ThreadLocalRandom.current().nextInt(0, 75);
            temp.position[bit] = temp.position[bit] ^ 1;
            int fitTemp = temp.calculFitness(clauses, 1);
            if (fit > fitTemp) {
                temp.position[bit] = temp.position[bit] ^ 1;
                i--;
            } else {
                part = temp;
            }
        }
    }

        public void updateBest (Particule part, ArrayList<int[]> clauses){
            int fit = part.calculFitness(clauses, 1);
            if (fit > part.pBestFitness) {
                part.pBest = part.position;
                part.pBestFitness = fit;
                if (fit > this.gBestFitness) {
                    this.gBest = part.position;
                    this.gBestFitness = fit;

                }
            }
        }

    }



