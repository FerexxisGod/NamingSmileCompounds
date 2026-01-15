package net.iupac.info_and_controle;

import java.util.*;

import org.openscience.cdk.interfaces.*;

public class Locants {

    private IAtomContainer Molecule;
    public List<Integer> Terminal;
    public List<Integer> LongestChain;
    public int BranchCount;
    public Map<IAtom, Integer> LocantMap = new HashMap<>();

    public Locants(IAtomContainer molecule){
        this.Molecule = molecule;
        this.Terminal = terminalAtom(molecule);
        this.LongestChain = branchAnalysis().get(0);
        this.BranchCount = branchAnalysis().get(1).get(0);
        this.LocantMap = setLocants();

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

    // Find the longest chain that contains a specific functional group (double/triple bond)
    public List<Integer> dfsLongestChainWithDoubleBond(IAtomContainer Molecule, IAtom startAtom, int atom1, int atom2) {
        Set<IAtom> visited = new HashSet<>();
        List<Integer> longestChain = new ArrayList<>();
        List<Integer> currentChain = new ArrayList<>();
        dfHelperWithBond(Molecule, startAtom, visited, currentChain, longestChain, atom1, atom2);
        return longestChain;
    }

    private void dfHelperWithBond(IAtomContainer molecule, IAtom currentAtom, Set<IAtom> visited, List<Integer> currentChain, List<Integer> longestChain, int bondAtom1, int bondAtom2) {
        visited.add(currentAtom);
        currentChain.add(molecule.indexOf(currentAtom));
        
        // Check if current chain contains the bond
        boolean hasDoubleBond = currentChain.contains(bondAtom1) && currentChain.contains(bondAtom2);
        
        // Update longest chain if current chain is longer AND contains the bond
        if (hasDoubleBond && currentChain.size() > longestChain.size()) {
            longestChain.clear();
            longestChain.addAll(currentChain);
        }

        for (IAtom neighbor : molecule.getConnectedAtomsList(currentAtom)) {
            if (!visited.contains(neighbor) && "C".equals(neighbor.getSymbol())) {
                dfHelperWithBond(molecule, neighbor, visited, currentChain, longestChain, bondAtom1, bondAtom2);
                visited.remove(neighbor);
                currentChain.remove(currentChain.size() - 1);
            }
        }
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
        
        // Find all double/triple bonds in the molecule
        List<int[]> functionalBonds = new ArrayList<>();
        for (IBond bond : this.Molecule.bonds()) {
            if (bond.getOrder() == IBond.Order.DOUBLE || bond.getOrder() == IBond.Order.TRIPLE) {
                int idx1 = this.Molecule.indexOf(bond.getBegin());
                int idx2 = this.Molecule.indexOf(bond.getEnd());
                functionalBonds.add(new int[]{idx1, idx2});
            }
        }
        
        // If molecule has double/triple bonds, find the longest chain containing them
        if (!functionalBonds.isEmpty()) {
            int maxLength = 0;
            List<Integer> bestChain = new ArrayList<>();
            int maxBranches = 0;
            
            // Try each bond
            for (int[] bond : functionalBonds) {
                // Try starting from each carbon
                List<Integer> carbonAtoms = new ArrayList<>();
                for (IAtom atom : this.Molecule.atoms()) {
                    if (atom.getSymbol().equals("C")) {
                        carbonAtoms.add(this.Molecule.indexOf(atom));
                    }
                }
                
                for (Integer atomIdx : carbonAtoms) {
                    IAtom atom = this.Molecule.getAtom(atomIdx);
                    List<Integer> chain = dfsLongestChainWithDoubleBond(this.Molecule, atom, bond[0], bond[1]);
                    
                    if (!chain.isEmpty()) {
                        int branches = branchCount(chain);
                        if (chain.size() > maxLength) {
                            maxLength = chain.size();
                            bestChain = new ArrayList<>(chain);
                            maxBranches = branches;
                        } else if (chain.size() == maxLength && branches > maxBranches) {
                            bestChain = new ArrayList<>(chain);
                            maxBranches = branches;
                        }
                    }
                }
            }
            
            if (!bestChain.isEmpty()) {
                LCBR.add(bestChain);
                LCBR.add(Arrays.asList(maxBranches));
                return LCBR;
            }
        }
        
        // If no chains with functional groups, use standard longest chain search
        List<Integer> carbonAtoms = new ArrayList<>();
        for (IAtom atom : this.Molecule.atoms()) {
            if (atom.getSymbol().equals("C")) {
                carbonAtoms.add(this.Molecule.indexOf(atom));
            }
        }
        
        int maxLength = 0;
        List<Integer> bestChain = new ArrayList<>();
        int maxBranches = 0;
        
        for (Integer atomIdx : carbonAtoms) {
            IAtom atom = this.Molecule.getAtom(atomIdx);
            List<Integer> chain = dfsLongestChain(this.Molecule, atom);
            int branches = branchCount(chain);
            
            if (chain.size() > maxLength) {
                maxLength = chain.size();
                bestChain = new ArrayList<>(chain);
                maxBranches = branches;
            } else if (chain.size() == maxLength && branches > maxBranches) {
                bestChain = new ArrayList<>(chain);
                maxBranches = branches;
            }
        }
        
        LCBR.add(bestChain);
        LCBR.add(Arrays.asList(maxBranches));
        return LCBR;
    }

    // Count functional groups (double/triple bonds) on the chain
    public int countFunctionalGroupsOnChain(List<Integer> chain) {
        int count = 0;
        for (IBond bond : this.Molecule.bonds()) {
            if (bond.getOrder() == IBond.Order.DOUBLE || bond.getOrder() == IBond.Order.TRIPLE) {
                int beginIdx = this.Molecule.indexOf(bond.getBegin());
                int endIdx = this.Molecule.indexOf(bond.getEnd());
                if (chain.contains(beginIdx) && chain.contains(endIdx)) {
                    count++;
                }
            }
        }
        return count;
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

    public Map<IAtom, Integer> setLocants(){
        Map<IAtom, Integer> Helper = new HashMap<>();
        int a = 1;
        for(int i: this.LongestChain){
            IAtom atom = this.Molecule.getAtom(i);
            Helper.put(atom, a++);
        }
        return Helper;
    }




    
}

