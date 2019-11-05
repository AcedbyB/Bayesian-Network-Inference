import base.*;
import core.*;
import core.Assignment;
import core.BayesianNetwork;
import core.Distribution;
import core.Value;
import org.xml.sax.SAXException;
import parser.XMLBIFParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        XMLBIFParser parser = new XMLBIFParser();
        Scanner sc = new Scanner(System.in);
        BayesianNetwork bn = parser.readNetworkFromFile(("src\\examples\\" + args[0]));
        RandomVariable queryVariable = bn.getVariableByName(args[1]);
        Assignment e = new base.Assignment();
        int cnt = 2;
        while (cnt < args.length) {
            RandomVariable variable = bn.getVariableByName(args[cnt]);
            cnt++;
            Value value = ((base.Domain) variable.getDomain()).getValueByString(args[cnt++]);
            e.put(variable, value);
        }

        System.out.println("Which tool would you like to use(Enter 1-4): ");
        System.out.println("1.Exact Inference");
        System.out.println("2.Rejection Sampling");
        System.out.println("3.Likelihood Weighting");
        System.out.println("4.Gibbs Sampling");
        int a = sc.nextInt();
        Distribution dist;
        if (a == 1) {
            EnumerationInferencer inferencer = new EnumerationInferencer();
            dist = inferencer.query(queryVariable, e, bn);

        } else {
            System.out.println("Enter the number of sample: ");
            int numSample = sc.nextInt();
            if (a == 2) {
                RejectionSampling rs = new RejectionSampling();
                dist = rs.query(queryVariable, e, bn, numSample);
            } else if (a == 3) {
                LikelihoodWeighting lw = new LikelihoodWeighting();
                dist = lw.query(queryVariable, e, bn, numSample);
            } else {
                GibbsSampling gibb = new GibbsSampling();
                dist = gibb.query(queryVariable, e, bn, numSample);
            }
        }
        System.out.println(dist);
    }

}
