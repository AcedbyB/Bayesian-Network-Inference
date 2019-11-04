package base;

import core.Assignment;
import core.BayesianNetwork;
import core.Distribution;
import core.RandomVariable;
import core.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RejectionSampling {

    public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network,int numSample) {
        Distribution distribution = new base.Distribution(X);
        for(Value v: X.getDomain()) distribution.put(v,0.0);
        int validSample = 0;
        for (int i = 1; i<= numSample; i++) {
            Assignment curSample = randomSample(network);
            if(curSample.containsAll(e)) {
                validSample++;
                Value v = curSample.get(X);
                double cur  = (distribution.get(v) + 1.0);
                distribution.put(v, cur);
            }
        }
        for (Value v : X.getDomain()) distribution.put(v, distribution.get(v)/validSample);
        return distribution;
    }

    public  Assignment randomSample(BayesianNetwork nw) {
        Random random = new Random();
        Assignment a = new base.Assignment();
        List<RandomVariable> randomVariables = nw.getVariablesSortedTopologically();
        for(RandomVariable x: randomVariables) {
            double total = 0.0;
            for(Value v: x.getDomain()) {
                a.put(x, v);
                double num = nw.getProbability(x, a);
                total +=  num;
                a.remove(x);
            }
            double choose = random.nextDouble()*total;
            for(Value v: x.getDomain()) {
                a.put(x, v);
                double num = nw.getProbability(x, a);
                choose -= num;
                if(choose < 0.0) {
                    break;
                }
                a.remove(x);
            }
        }
        return a;
    }
}
