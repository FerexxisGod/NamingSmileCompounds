package net.iupac.info_and_controle;

import java.util.*;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

public class Locants {

    private IAtomContainer Molecule;
    public List<Integer> Terminal;
    public List<Integer> LongestChain;
    public int BranchCount;
    

    public Locants(IAtomContainer molecule){
        this.Molecule = molecule;
        this.Terminal = terminalAtom(molecule);
        this.LongestChain = branchAnalysis().get(0);
        this.BranchCount = branchAnalysis().get(1).get(0);
            

    }
    Locants(){

    }
    
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


    public List<Integer> dfsLongestChain(IAtomContainer Molecule, IAtom startAtom) {
        Set<IAtom> visited = new HashSet<>();
        List<Integer> longestChain = new ArrayList<>();
        List<Integer> currentChain = new ArrayList<>();
        dfsHelper(Molecule, startAtom, visited, currentChain, longestChain);
        return longestChain;
    }

    private void dfsHelper(IAtomContainer molecule, IAtom startAtom, Set<IAtom> visited, List<Integer> currentChain, List<Integer> longestChain) {
        visited.add(startAtom);
        currentChain.add(molecule.indexOf(startAtom));
        
        if (currentChain.size() > longestChain.size()){
            longestChain.clear();
            longestChain.addAll(currentChain);
        }

        for (IAtom neighbor : molecule.getConnectedAtomsList(startAtom)) {
            if (!visited.contains(neighbor) && "C".equals(neighbor.getSymbol())) {
                dfsHelper(molecule, neighbor, visited, currentChain, longestChain);
                
                visited.remove(neighbor);
                currentChain.remove(currentChain.size() - 1);
            }
        }
    }

    public List<List<Integer>> branchAnalysis(){
        List<List<Integer>> LCBR = new ArrayList<>();

        LCBR.add(dfsLongestChain(this.Molecule, this.Molecule.getAtom(this.Terminal.get(0))));
        LCBR.add(Arrays.asList(branchCount(LCBR.get(0))));
        for (Integer idx : this.Terminal) {
            IAtom terminalAtom = this.Molecule.getAtom(idx);
            List<Integer> chain = dfsLongestChain(this.Molecule, terminalAtom);
            List<Integer> branchInfo = Arrays.asList(branchCount(chain));
            if (chain.size() > LCBR.get(0).size()) {
                LCBR.clear();
                LCBR.add(chain);
                LCBR.add(branchInfo);

            } else if (chain.size() == LCBR.get(0).size() && branchCount(chain)>branchCount(LCBR.get(0))) {
                LCBR.clear();
                LCBR.add(chain);
                LCBR.add(branchInfo);
            }
        }
        return LCBR;
    }

    public int branchCount(List<Integer> LongestChain){
        int branches = 0;
        for (Integer idx : LongestChain) {
            IAtom a = this.Molecule.getAtom(idx);
            int substituants = 0;
            for (IAtom n : this.Molecule.getConnectedAtomsList(a)) {
                if(LongestChain.contains(this.Molecule.indexOf(n))) continue;
                else substituants++;
            }
            branches+=substituants;

        }

        return branches;
    }




    
}

