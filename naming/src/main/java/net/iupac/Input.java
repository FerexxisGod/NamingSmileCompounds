package net.iupac;

import net.iupac.info_and_controle.Locants;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.AtomTypeAwareSaturationChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class Input {

    public static final String smiles = "C#CC(C=C)(CCC)CCC"; 
    public static IAtomContainer Molecule = isValidInput(input(smiles)) ? input(smiles): null;



    public static IAtomContainer input(String molecule){
       SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
       IAtomContainer atomContainer = null;
       try {
           atomContainer = sp.parseSmiles(molecule);
       } catch (Exception e) {
           System.out.println("Error parsing SMILES: " + e.getMessage());
           return null;
       }
       return atomContainer;
    }

    public static boolean isValidInput(IAtomContainer molecule) {
        try {
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        } catch (CDKException e) {
            e.printStackTrace();
        }
        AtomTypeAwareSaturationChecker checker = new AtomTypeAwareSaturationChecker();
        
        for (IAtom atom : molecule.atoms()) {

            try {
                if (!checker.isSaturated(atom, molecule)) {
                    
                    return false;
                }
            } catch (CDKException e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isValidInput(Molecule)? "Valid input" : "Invalid input");
        Locants locants = new Locants(Molecule);
        Suffix suffix = new Suffix();
        FinalBuilder fb = new FinalBuilder();
        System.out.println("Terminal atoms: " + locants.Terminal);
        System.out.println("Longest chain: "+ locants.LongestChain);
        System.out.println("Chain length: " + locants.LongestChain.size());
        System.out.println("Branch count: "+ locants.BranchCount);
        System.out.println("\nSuffix: " + suffix.getSuffix());
        System.out.println("Root word: " + new RootWord().rootWord);
        System.out.println("Final name: " + fb.finalName);
    }
}
