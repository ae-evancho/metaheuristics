import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.control.TextField;

public class PopulationBee {
    ArrayList<Bee> populationVect;
    HashSet<doubleWrapper> tabooT;
    ArrayList<doubleWrapper> danceT;
    int n;
    int nbChances;
    public int nbIteration;


    public PopulationBee(int n, ArrayList<int[]> clauses, int maxChances)
    {
        int[] temp = new int[75];
        this.populationVect = new ArrayList<Bee>();
        this.n = n;
        this.nbChances=maxChances;
        this.nbIteration = 0;
        tabooT= new HashSet<>();
        danceT = new ArrayList<>();
        Bee rBee;
        for (int i = 0; i < this.n; i++)
        {
            rBee = new Bee();
            this.populationVect.add(rBee);
        }
    }

    public ArrayList<Bee> getPopulationVect()
    {
        return this.populationVect;
    }


    public int [][] calculSearchArea(int [] sRef, int flip){
        int [][] searchArea=new int[n][75];
        int h=0;
        while(h < n && h < flip){
            int[] s= sRef.clone();
            int p=0;
            while(75 > flip*p+h){
                s[flip*p+h]=s[flip*p+h]^1;
                p++;
            }
            searchArea[h]=s;
            h++;
        }
        int k=0;
        int oldh=h;
        while(h<n && k<oldh){
            for(int i=0;i<75;i++) searchArea[h][i]=searchArea[k][i]^1;
            k++;h++;
        }
        int p=0;int h2=0;k=h;
        while(h<n && h2<flip){
            int[] s= sRef.clone();
            while(75 > flip*p+h2){
                s[p]=s[p]^1;
                p++;
            }
            searchArea[h]=s;
            h++;  h2++;
        }
        oldh=h;
        while(h<n && k<oldh){
            for(int i=0;i<75;i++) searchArea[h][i]=searchArea[k][i]^1;
            k++;h++;
        }
        while(h<n){
            for(int i=0;i<75;i++) searchArea[h][i]=ThreadLocalRandom.current().nextInt(0,2);
            h++;
        }
        return searchArea;
    }

    public void initBees(int [][] searchArea){
        int i=0;
        for(Bee rBee : this.populationVect){
            rBee.position=searchArea[i];
            i++;
        }
    }
    public void updateDance(){
        doubleWrapper d;
        for(Bee rBee : this.populationVect){
            d= new doubleWrapper(rBee.bestSol, rBee.bestFit);
            this.danceAdd(d);
        }
    }

    public void danceAdd(doubleWrapper d){
        boolean b=false;
        for (int i = 0; i <danceT.size() ; i++) {
            if(danceT.get(i).compareTo(d)>=1) continue;
            danceT.add(i,d);
            return;
        }
        danceT.add(d);
    }

    public doubleWrapper selectRef(doubleWrapper sRefOld, int maxChances, ArrayList<int[]> clauses){
        doubleWrapper bestQ=getBest(clauses);
        doubleWrapper bestD;
        int deltaF=bestQ.fitness-sRefOld.fitness;
        doubleWrapper sRef;
        if(deltaF>0){
           sRef=new doubleWrapper(bestQ.bin, bestQ.fitness);
            if(this.nbChances<maxChances) this.nbChances=maxChances;
        }
        else {
            this.nbChances--;
            if(this.nbChances>0) sRef=new doubleWrapper(bestQ.bin, bestQ.fitness);
            else{
                this.nbChances=maxChances;
                bestD=getDiverse(sRefOld);
                sRef=new doubleWrapper(bestD.bin, bestD.fitness);
            }
        }

        return sRef;
    }

    public doubleWrapper getDiverse(doubleWrapper sRef){
        doubleWrapper best=null;
        int dist=0;
        for (doubleWrapper dw : tabooT){
            int d=Bee.hammDistance(sRef.bin, dw.bin);
            if(d<=dist){
                dist=d;
                best=dw;
            }
        }

        return best;
    }

    public doubleWrapper getBest(ArrayList<int[]> clauses){
        int i=0;
        boolean b=false;
        while (i <danceT.size()) {
            if(tabooT.contains(danceT.get(i))){ b=true; break;}
            if(b) { b=false; i++;}
            else break;
        }
        if(i<danceT.size()) return danceT.get(i);
        else {
            Bee rBee=new Bee();
            for (int j = 0; j < 75 ; j++) rBee.setBeePosBit(j, ThreadLocalRandom.current().nextInt(0,2));
            rBee.calculFitness(clauses);

            return new doubleWrapper(rBee.position, rBee.fitness);
        }


    }

    public boolean terminCond(int maxIter, doubleWrapper sRef){
        if(this.nbIteration>= maxIter || sRef.fitness==325)
            return  true;
        else return false;
    }

    public void printPopulation() {
        for (int i = 0; i < this.n; i++) {
            this.getPopulationVect().get(i).printBee();
            System.out.println("  Iteration:" + this.nbIteration + "\n");
        }
    }

    public void printSolution(doubleWrapper sRef, TextField result){
        String s= "";
        for (int i = 0; i <75 ; i++) {
            s=s+sRef.bin[i];
        }

        result.setText(s);
        System.out.println(s);
    }

}


 class doubleWrapper implements Comparable<doubleWrapper>{
    int fitness;
    int[] bin;

    public doubleWrapper (int[] b, int f){
        this.fitness=f;
        this.bin=b;
    }
    public int compareTo(doubleWrapper d){
        if(d.fitness<this.fitness) return 1;
        else if(d.fitness>this.fitness) return -1;
        else return  0;

    }

     public boolean equals(Object d){
        for (int i = 0; i <75 ; i++) {
             if(bin[i]!=((doubleWrapper)d).bin[i]) return false;
         }
        return true;
     }

}

class singleWrapper{
    int[] bin;

    public singleWrapper(int[] b){
        this.bin=b;
    }
}