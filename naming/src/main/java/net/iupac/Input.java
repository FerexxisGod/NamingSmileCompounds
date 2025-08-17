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

    static final String smiles = "CC(C(=CCl)C(Cl)(Br))CCC"; 
    static IAtomContainer molecule = input(smiles);



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
        System.out.println(isValidInput(molecule)? "Valid input" : "Invalid input");
        Locants locants = new Locants();
        System.out.println("Terminal atoms: " + locants.terminalAtom(molecule));
    }
}
