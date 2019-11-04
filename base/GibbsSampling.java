package base;

import core.Assignment;
import core.BayesianNetwork;
import core.Distribution;
import core.RandomVariable;
import core.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GibbsSampling {

    public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network, int numSample) {
        Distribution distribution = new base.Distribution(X);
        List<RandomVariable> allVar = network.getVariablesSortedTopologically();
        List<RandomVariable> notEvidence = new ArrayList<>();
        for (Value v : X.getDomain()) distribution.put(v, 0.0);
        for (RandomVariable rv : allVar) {
            if (!e.containsKey(rv)) notEvidence.add(rv);
        }
        Assignment curState = e.copy();
        for (RandomVariable rv : notEvidence)
            curState.put(rv, rv.getDomain().iterator().next());

        for (int i = 1; i <= numSample; i++) {
            for (RandomVariable rv : notEvidence) {
                curState = MutateSampling(network, curState, rv);
                Value V = curState.get(X);
                distribution.set(V, distribution.get(V) + 1.0);
            }
        }
        for (Value v : X.getDomain()) distribution.set(v, distribution.get(v) / (numSample*notEvidence.size()));
        return distribution;
    }

    public Assignment MutateSampling(BayesianNetwork nw, Assignment e, RandomVariable curV) {
        Random random = new Random();
        Distribution tmp = new base.Distribution(curV);
        double total = 0.0;

        for (Value nextVal : curV.getDomain()) {
            e.put(curV, nextVal);
            double flipProb = nw.getProbability(curV, e);
            for(RandomVariable child: nw.getChildren(curV)) {
                flipProb *= nw.getProbability(child, e);
            }
            total += flipProb;
            tmp.put(nextVal, flipProb);
            e.remove(curV);
        }

        double choose = random.nextDouble() * total;
        for (Value v : curV.getDomain()) {
            double num = tmp.get(v);
            choose -= num;
            if (choose < 0.0) {
                e.put(curV, v);
                break;
            }
        }

        return e;
    }
}
