package net.iupac.info_and_controle;

import java.util.*;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

public class Locants {
    public List<Integer> terminalAtom(IAtomContainer molecule){
        List<Integer> terminalAtomNo = new ArrayList<>();
        for (IAtom atom : molecule.atoms()) {
            if (!atom.getSymbol().equals("C")) continue;

            int heavyAtomNeighbors = 0;
            
            for (IAtom neighbor : molecule.getConnectedAtomsList(atom)) {
                if (neighbor.getSymbol().equals("C")) {
                    heavyAtomNeighbors++;
                }
            }

            boolean isTerminal = heavyAtomNeighbors == 1;

            if (isTerminal) {
                terminalAtomNo.add(molecule.indexOf(atom));
            }
            
        }

        return terminalAtomNo;
    }

    
}

