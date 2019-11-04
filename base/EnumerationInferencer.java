package base;

import core.Assignment;
import core.BayesianNetwork;
import core.Distribution;
import core.Inferencer;
import core.RandomVariable;
import core.Value;

import java.util.ArrayList;
import java.util.List;


public class EnumerationInferencer implements Inferencer {

    double alpha;

    public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
        Distribution distribution = new base.Distribution(X);
        List<RandomVariable> randomVariables = network.getVariablesSortedTopologically();
        double totalFractionProb = 0.0;

        for (Value v : X.getDomain()) {
            e.put(X,v);
            double prob = dfs(randomVariables, e, network);
            totalFractionProb += prob;
            distribution.put(v,prob);
            e.remove(X);
        }
        alpha = 1/totalFractionProb;
        for (Value v : X.getDomain()) distribution.put(v, distribution.get(v)*alpha);


        return distribution;
    }

    public double dfs(List<RandomVariable> curList, Assignment curE, BayesianNetwork nw) {
        if (curList.size() == 0) return 1;

        RandomVariable curVar = curList.get(0);
        List<RandomVariable> nextList = new ArrayList<>();

        for (int i = 1; i < curList.size(); i++) {
            nextList.add(curList.get(i));
        }

        if(curE.containsKey(curVar)) return nw.getProbability(curVar, curE)*dfs(nextList, curE, nw);
        else {
            double sum = 0.0;
            for(Value v: curVar.getDomain()) {
                curE.put(curVar, v);
                sum += nw.getProbability(curVar, curE)*dfs(nextList, curE, nw);
                curE.remove(curVar);
            }
            return sum;
        }
    }
}
