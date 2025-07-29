package net.iupac;


import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;
import java.util.*;

public class SmilesParserExample {

    public static int getAtomCount(String smiles) throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer molecule = sp.parseSmiles(smiles);
        return molecule.getAtomCount();
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter SMILES string: ");
            String smiles = sc.next(); // 2-chloropropane
            int atomCount = getAtomCount(smiles);
            System.out.print("Enter a number: ");
            int n = sc.nextInt();
            System.out.println("The number "+n+" is " + (isEven(n) ? " even." : " odd."));
            System.out.println("SMILES: " + smiles);
            System.out.println("Atom count: " + atomCount);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEven(int n){
        return n % 2 == 0;
    }
}

