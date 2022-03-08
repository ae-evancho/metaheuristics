import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class PopulationGA {
    ArrayList<Individual> popul;
    int n;
    public int generation;
    int [] y= new int[325];
    int [] w= new int[325];
    int [][] A= new int[325][5];

    public PopulationGA(int n)
    {
        this.popul = new ArrayList<Individual>();
        this.n = n;
        this.generation = 0;
        Individual indiv;
        Random rn = new Random();
        for (int i = 0; i < this.n; i++)
        {
            indiv = new Individual();
            for (int j = 0; j < 75; j++) indiv.setIndivGene(j, ThreadLocalRandom.current().nextInt(0,2));
            this.popul.add(indiv);
        }
        for(int i=0;i<325;i++)
            this.w[i]=1;
    }

    public ArrayList<Individual> getPopul()
    {
        return this.popul;
    }

    public void createNewGeneration(double crossRate, ArrayList<int[]> clauses){

        int crossSize=(int)(crossRate*this.n);
        int j=this.n-1;
        for (int i=0; i<crossSize; i++) {
            int range = ThreadLocalRandom.current().nextInt(0, 4);
            int indiv1 = 0;
            int indiv2 = 0;
            if (range > 0) {
                indiv1 = ThreadLocalRandom.current().nextInt(0, crossSize);
                indiv2 = ThreadLocalRandom.current().nextInt(0, crossSize);
            } else if (range == 0) {

                indiv1 = ThreadLocalRandom.current().nextInt(crossSize , this.n);
                indiv2 = ThreadLocalRandom.current().nextInt(crossSize , this.n);
            }

            Individual child = Individual.crossOver(this.popul.get(indiv1), this.popul.get(indiv2)) ;
            this.popul.remove(j);
            this.popul.add(child);
            j--;

        }
        this.generation++;

    }


    public void createNewGeneration2(double crossRate, ArrayList<int[]> clauses){
        int crossSize=(int)(crossRate*this.n);
        int k=this.n-1;
        for (int i = 0; i <crossSize ; i++) {

            int fitTotal = 0;
            for (int j = 0; j < this.n; j++) {
                fitTotal = fitTotal + this.getPopul().get(j).getFitness();
            }

            int roulette = ThreadLocalRandom.current().nextInt(fitTotal);
            int sum = 0, i1 = this.n;
            while (sum < roulette) {
                i1--;
                sum = sum + this.getPopul().get(i1).getFitness();

            }

            roulette = ThreadLocalRandom.current().nextInt(fitTotal);
            sum = 0;
            int i2 = this.n;
            while (sum < roulette) {
                i2--;
                sum = sum + this.getPopul().get(i2).getFitness();

            }

            if (i1 > this.n-1) i1 =  this.n-1;
            if (i2 > this.n-1) i2 =  this.n-1;
            //   System.out.println(i1+" "+i2);
            Individual parent1 = this.getPopul().get(i1), parent2 = this.getPopul().get(i2);
            Individual temp = Individual.crossOver(parent1, parent2);
            Individual offspring1 = new Individual(), offspring2 = new Individual();
            offspring1 = temp;
            //  offspring2 = temp[1];

            this.popul.remove(k);
            //  this.popul.remove(lastIndex - 1);
            this.popul.add(offspring1);
            //  this.popul.add(offspring2);
            k--;

        }

        this.generation++;
}


    public void selection(ArrayList<int[]> clauses){
        y= new int[325];
        int[] yTemp= new int[325];
        for (Individual ii : this.popul) {
            yTemp=ii.fitness(clauses, this.w);
            for(int i=0;i<325;i++)
                y[i]=y[i]+yTemp[i];
        }

        this.popul.sort(Comparator.comparingInt(ind -> ind.getFitness()));

        this.claculW();
    }

    public void mutation(double mutationRate){
        boolean verif[]= new boolean[this.n];
        int muteSize= (int) (this.n*mutationRate);
        int rand;
        for (int i = 0; i < muteSize ; i++) {
            rand=ThreadLocalRandom.current().nextInt(this.n);
            while(verif[rand]==true)  rand=ThreadLocalRandom.current().nextInt(this.n);
            this.popul.get(rand).mutateGenes(0.01);
            verif[i]=true;
        }

    }

    public boolean terminCond(int maxGen){
        if(this.generation>= maxGen || this.getPopul().get(0).getFitness()<=0)
            return  true;
        else return false;
    }

    public void repopulate(){
        this.popul = new ArrayList<Individual>();
        Individual indiv;
        for (int i = 0; i < this.n; i++)
        {
            indiv = new Individual();
            for (int j = 0; j < 75; j++) indiv.setIndivGene(j, ThreadLocalRandom.current().nextInt(0,2));
            this.popul.add(indiv);
        }
    }

    public void printPopulation(){
        for (int i = 0; i < this.n ; i++) {
            this.getPopul().get(i).printIndiv();
            System.out.println(this.getPopul().get(i).getFitness()+"   Gen:"+ this.generation+"\n");
        }
    }

    public void claculW(){
        int max, mIndex;
            for (int j = 0; j < 5; j++) {
                max=0; mIndex=0;
                for (int i = 0; i < 325; i++) {
                    if (max < this.y[i]){ max = this.y[i]; mIndex = i;}
                }
                this.y[mIndex] = 0;
                this.w[mIndex] = 5;
            }


        for (int i = 0; i <325 ; i++) {
            this.A[i][this.generation%5]=this.w[i];
        }

        for (int i = 0; i <5 ; i++) {
            for (int j = 0; j <325 ; j++) {
                this.A[j][i]=this.w[j];
            }
        }


    }

}
