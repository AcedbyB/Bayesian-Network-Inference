import base.BooleanDomain;
import base.BooleanValue;
import base.NamedVariable;
import core.*;
import org.xml.sax.SAXException;
import parser.XMLBIFParser;
import util.ArraySet;

import javax.lang.model.element.VariableElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

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
            Value value = ((base.Domain)variable.getDomain()).getValueByString(args[cnt++]);
            e.put(variable, value);
        }

        System.out.println("Which tool would you like to use(Enter 1-4): ");
        System.out.println("1.Exact Inference");
        System.out.println("2.Rejection Sampling");
        System.out.println("3.Likelihood Weighting");
        System.out.println("4.Gibbs Sampling");
        int a = sc.nextInt();
        if(a == 1) {

        } else {
            System.out.println("Enter the number of sample: ");
            a = sc.nextInt();
        }
//        Inferencer exact = new base.EnumerationInferencer();
//        a = new base.Assignment();
//        a.put(J, TRUE);
//        a.put(M, TRUE);
//        Distribution dist = exact.query(B, a, bn);
//        System.out.println(dist);
    }

}
