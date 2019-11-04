package base;

import core.Assignment;
import core.BayesianNetwork;
import core.Distribution;
import core.RandomVariable;
import core.Value;

import java.util.List;
import java.util.Random;


public class LikelihoodWeighting {

    double weight;

    public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network, int numSample) {
        Distribution distribution = new base.Distribution(X);
        for (Value v : X.getDomain()) distribution.put(v, 0.0);
        double totalprob = 0.0;

        for (int i = 1; i <= numSample; i++) {
            weight = 1.0;
            Assignment curSample = evidenceSample(network, e);
            Value v = curSample.get(X);
            double cur = (distribution.get(v) + weight);
            totalprob += weight;
            distribution.put(v, cur);
        }
        for (Value v : X.getDomain()) distribution.put(v, distribution.get(v) / totalprob);
        return distribution;
    }

    public Assignment evidenceSample(BayesianNetwork nw, Assignment e) {
        Random random = new Random();
        Assignment a = e.copy();
        List<RandomVariable> randomVariables = nw.getVariablesSortedTopologically();
        for (RandomVariable x : randomVariables) {
            if (a.containsKey(x)) {
                weight *= nw.getProbability(x, a);
            } else {
                double total = 0.0;
                for (Value v : x.getDomain()) {
                    a.put(x, v);
                    double num = nw.getProbability(x, a);
                    total += num;
                    a.remove(x);
                }
                double choose = random.nextDouble() * total;
                for (Value v : x.getDomain()) {
                    a.put(x, v);
                    double num = nw.getProbability(x, a);
                    choose -= num;
                    if (choose < 0.0) {
                        break;
                    }
                    a.remove(x);
                }
            }
        }
        return a;
    }
}
