package net.iupac;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.smarts.SmartsPattern;
import net.iupac.info_and_controle.Locants;
import java.util.*;

public class Suffix {
    private IAtomContainer molecule = Input.Molecule;
    private Locants locants = new Locants(molecule);
    
    
    public String getSuffix() {
        // Check in priority order (higher functional groups take precedence)
        
        // 1. Check for carboxylic acid (-COOH) - highest priority
        if (hasCarboxylicAcid()) {
            return "oic acid";
        }
        
        // 2. Check for aldehyde (-CHO)
        if (hasAldehyde()) {
            return "al";
        }
        
        // 3. Check for ketone (C=O in middle)
        String ketonesSuffix = getKetonesSuffix();
        if (!ketonesSuffix.isEmpty()) {
            return ketonesSuffix;
        }
        
        // 4. Check for alcohol (-OH)
        String alcoholsSuffix = getAlcoholsSuffix();
        if (!alcoholsSuffix.isEmpty()) {
            return alcoholsSuffix;
        }
        
        // 5. Check for amine (-NH2, -NH)
        String aminesSuffix = getAminesSuffix();
        if (!aminesSuffix.isEmpty()) {
            return aminesSuffix;
        }
        
        // 6. Check for amide (CONH2)
        String amidesSuffix = getAmidesSuffix();
        if (!amidesSuffix.isEmpty()) {
            return amidesSuffix;
        }
        
        // 7. Check for triple and double bonds (combined)
        String unsaturationSuffix = getUnsaturationSuffix();
        if (!unsaturationSuffix.isEmpty()) {
            return unsaturationSuffix;
        }
        
        // 8. Default to alkane
        return "ane";
    }
    
    // Get suffix for unsaturation (double bonds, triple bonds, or combination)
    private String getUnsaturationSuffix() {
        List<Integer> doubleBondLocants = getAllDoubleBondLocants();
        List<Integer> tripleBondLocants = getAllTripleBondLocants();
        
        // If no unsaturation, return empty
        if (doubleBondLocants.isEmpty() && tripleBondLocants.isEmpty()) {
            return "";
        }
        
        // Sort locants to get the lowest numbering
        doubleBondLocants.sort(Integer::compareTo);
        tripleBondLocants.sort(Integer::compareTo);
        
        // Build the suffix
        StringBuilder suffix = new StringBuilder();
        
        // When both double and triple bonds exist, triple bonds come first in the name
        if (!tripleBondLocants.isEmpty() && !doubleBondLocants.isEmpty()) {
            // Both exist: enyne or dienyne format
            // Triple bond comes first in suffix
            suffix.append(formatLocants(doubleBondLocants));
            suffix.append("-");
            suffix.append(getDoubleBondSuffix(doubleBondLocants.size()).substring(0, getDoubleBondSuffix(doubleBondLocants.size()).length()-1));
            suffix.append(formatLocants(tripleBondLocants));
            suffix.append("-");
            suffix.append(getTripleBondSuffix(tripleBondLocants.size()));
        } else if (!tripleBondLocants.isEmpty()) {
            // Only triple bonds: yne or diyne
            suffix.append(formatLocants(tripleBondLocants));
            suffix.append("-");
            suffix.append(getTripleBondSuffix(tripleBondLocants.size()));
        } else {
            // Only double bonds: ene or diene
            suffix.append(formatLocants(doubleBondLocants));
            suffix.append("-");
            suffix.append(getDoubleBondSuffix(doubleBondLocants.size()));
        }
        
        return suffix.toString();
    }
    
    // Get all double bond locants on the main chain
    private List<Integer> getAllDoubleBondLocants() {
        List<Integer> locants = new ArrayList<>();
        
        for (IBond bond : molecule.bonds()) {
            if (bond.getOrder() == IBond.Order.DOUBLE) {
                IAtom begin = bond.getBegin();
                IAtom end = bond.getOther(begin);
                
                int beginIdx = molecule.indexOf(begin);
                int endIdx = molecule.indexOf(end);
                
                // Skip if it's a C=O (carbonyl)
                if ((begin.getSymbol().equals("C") && end.getSymbol().equals("O")) ||
                    (begin.getSymbol().equals("O") && end.getSymbol().equals("C"))) {
                    continue;
                }
                
                // Check if both atoms of the double bond are on the main chain
                if (this.locants.LongestChain.contains(beginIdx) && 
                    this.locants.LongestChain.contains(endIdx)) {
                    // Get the lower locant
                    Integer beginLocant = this.locants.LocantMap.get(begin);
                    Integer endLocant = this.locants.LocantMap.get(end);
                    
                    if (beginLocant != null && endLocant != null) {
                        locants.add(Math.min(beginLocant, endLocant));
                    }
                }
            }
        }
        
        return locants;
    }
    
    // Get all triple bond locants on the main chain
    private List<Integer> getAllTripleBondLocants() {
        List<Integer> locants = new ArrayList<>();
        
        for (IBond bond : molecule.bonds()) {
            if (bond.getOrder() == IBond.Order.TRIPLE) {
                IAtom begin = bond.getBegin();
                IAtom end = bond.getEnd();
                
                int beginIdx = molecule.indexOf(begin);
                int endIdx = molecule.indexOf(end);
                
                // Check if both atoms of the triple bond are on the main chain
                if (this.locants.LongestChain.contains(beginIdx) && 
                    this.locants.LongestChain.contains(endIdx)) {
                    // Get the lower locant
                    Integer beginLocant = this.locants.LocantMap.get(begin);
                    Integer endLocant = this.locants.LocantMap.get(end);
                    
                    if (beginLocant != null && endLocant != null) {
                        locants.add(Math.min(beginLocant, endLocant));
                    }
                }
            }
        }
        
        return locants;
    }
    
    // Get the suffix for double bonds based on count
    private String getDoubleBondSuffix(int count) {
        return switch(count) {
            case 1 -> "ene";
            case 2 -> "diene";
            case 3 -> "triene";
            case 4 -> "tetraene";
            case 5 -> "pentaene";
            default -> count + "-ene";
        };
    }
    
    // Get the suffix for triple bonds based on count
    private String getTripleBondSuffix(int count) {
        return switch(count) {
            case 1 -> "yne";
            case 2 -> "diyne";
            case 3 -> "triyne";
            case 4 -> "tetrayne";
            case 5 -> "pentayne";
            default -> count + "-yne";
        };
    }
    
    // Format locants as a string (e.g., "2,4" or "2" or "2,4,6")
    private String formatLocants(List<Integer> locants) {
        if (locants.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locants.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(locants.get(i));
        }
        return sb.toString();
    }
    
    // Check for carboxylic acid: -COOH or -COO-
    private boolean hasCarboxylicAcid() {
        try {
            String carboxylPattern = "[CX3](=O)[OX2H1]";
            SmartsPattern pattern = SmartsPattern.create(carboxylPattern);
            return pattern.matches(molecule);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Check for aldehyde: CHO at terminal position
    private boolean hasAldehyde() {
        try {
            String aldehydePattern = "[CX3H1](=O)";
            SmartsPattern pattern = SmartsPattern.create(aldehydePattern);
            return pattern.matches(molecule);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Get suffix for multiple ketones (C=O in middle of chain)
    private String getKetonesSuffix() {
        try {
            String ketonePattern = "[#6][CX3](=O)[#6]";
            SmartsPattern pattern = SmartsPattern.create(ketonePattern);
            
            if (pattern.matches(molecule)) {
                List<Integer> ketoneLocants = new ArrayList<>();
                
                // Find all ketone groups on the main chain
                for (IAtom atom : molecule.atoms()) {
                    if (atom.getSymbol().equals("C") && 
                        locants.LongestChain.contains(molecule.indexOf(atom))) {
                        
                        // Check if this carbon has a C=O double bond
                        for (IBond bond : molecule.getConnectedBondsList(atom)) {
                            if (bond.getOrder() == IBond.Order.DOUBLE) {
                                IAtom other = bond.getOther(atom);
                                if (other.getSymbol().equals("O")) {
                                    // Check if it's not aldehyde or carboxylic acid
                                    if (!hasAldehyde() && !hasCarboxylicAcid()) {
                                        Integer locant = locants.LocantMap.get(atom);
                                        if (locant != null && !ketoneLocants.contains(locant)) {
                                            ketoneLocants.add(locant);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (!ketoneLocants.isEmpty()) {
                    ketoneLocants.sort(Integer::compareTo);
                    return formatLocants(ketoneLocants) + "-" + getKetonesSuffixName(ketoneLocants.size());
                }
            }
        } catch (Exception e) {
            // Silent fail
        }
        return "";
    }
    
    // Get suffix for multiple alcohols (-OH)
    private String getAlcoholsSuffix() {
        try {
            String alcoholPattern = "[OD2H]";
            SmartsPattern pattern = SmartsPattern.create(alcoholPattern);
            
            if (pattern.matches(molecule)) {
                List<Integer> alcoholLocants = new ArrayList<>();
                
                // Find all hydroxyl groups on the main chain
                for (IAtom atom : molecule.atoms()) {
                    if (atom.getSymbol().equals("O") && molecule.getConnectedBondsCount(atom) == 1) {
                        // This is an OH group, find the carbon it's attached to
                        for (IAtom neighbor : molecule.getConnectedAtomsList(atom)) {
                            if (neighbor.getSymbol().equals("C") && 
                                locants.LongestChain.contains(molecule.indexOf(neighbor))) {
                                Integer locant = locants.LocantMap.get(neighbor);
                                if (locant != null && !alcoholLocants.contains(locant)) {
                                    alcoholLocants.add(locant);
                                }
                            }
                        }
                    }
                }
                
                if (!alcoholLocants.isEmpty()) {
                    alcoholLocants.sort(Integer::compareTo);
                    return formatLocants(alcoholLocants) + "-" + getAlcoholsSuffixName(alcoholLocants.size());
                }
            }
        } catch (Exception e) {
            // Silent fail
        }
        return "";
    }
    
    // Get suffix for multiple amines (-NH2, -NH)
    private String getAminesSuffix() {
        try {
            String aminePattern = "[NX3,NX2]";
            SmartsPattern pattern = SmartsPattern.create(aminePattern);
            
            if (pattern.matches(molecule)) {
                List<Integer> amineLocants = new ArrayList<>();
                
                // Find all nitrogen atoms on the main chain
                for (IAtom atom : molecule.atoms()) {
                    if (atom.getSymbol().equals("N") && 
                        locants.LongestChain.contains(molecule.indexOf(atom))) {
                        Integer locant = locants.LocantMap.get(atom);
                        if (locant != null && !amineLocants.contains(locant)) {
                            amineLocants.add(locant);
                        }
                    }
                }
                
                if (!amineLocants.isEmpty()) {
                    amineLocants.sort(Integer::compareTo);
                    return formatLocants(amineLocants) + "-" + getAminesSuffixName(amineLocants.size());
                }
            }
        } catch (Exception e) {
            // Silent fail
        }
        return "";
    }
    
    // Get suffix for multiple amides (CONH2)
    private String getAmidesSuffix() {
        try {
            String amidePattern = "[CX3](=O)[NX3]";
            SmartsPattern pattern = SmartsPattern.create(amidePattern);
            
            if (pattern.matches(molecule)) {
                List<Integer> amideLocants = new ArrayList<>();
                
                // Find all amide groups on the main chain
                for (IAtom atom : molecule.atoms()) {
                    if (atom.getSymbol().equals("C") && 
                        locants.LongestChain.contains(molecule.indexOf(atom))) {
                        
                        // Check if this carbon has a C=O double bond to nitrogen
                        for (IBond bond : molecule.getConnectedBondsList(atom)) {
                            if (bond.getOrder() == IBond.Order.DOUBLE) {
                                IAtom other = bond.getOther(atom);
                                if (other.getSymbol().equals("O")) {
                                    Integer locant = locants.LocantMap.get(atom);
                                    if (locant != null && !amideLocants.contains(locant)) {
                                        amideLocants.add(locant);
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (!amideLocants.isEmpty()) {
                    amideLocants.sort(Integer::compareTo);
                    return formatLocants(amideLocants) + "-" + getAmidesSuffixName(amideLocants.size());
                }
            }
        } catch (Exception e) {
            // Silent fail
        }
        return "";
    }
    
    // Get ketone suffix name based on count
    private String getKetonesSuffixName(int count) {
        return switch(count) {
            case 1 -> "one";
            case 2 -> "dione";
            case 3 -> "trione";
            case 4 -> "tetraone";
            case 5 -> "pentaone";
            default -> count + "-one";
        };
    }
    
    // Get alcohol suffix name based on count
    private String getAlcoholsSuffixName(int count) {
        return switch(count) {
            case 1 -> "ol";
            case 2 -> "diol";
            case 3 -> "triol";
            case 4 -> "tetraol";
            case 5 -> "pentaol";
            default -> count + "-ol";
        };
    }
    
    // Get amine suffix name based on count
    private String getAminesSuffixName(int count) {
        return switch(count) {
            case 1 -> "amine";
            case 2 -> "diamine";
            case 3 -> "triamine";
            case 4 -> "tetraamine";
            case 5 -> "pentaamine";
            default -> count + "-amine";
        };
    }
    
    // Get amide suffix name based on count
    private String getAmidesSuffixName(int count) {
        return switch(count) {
            case 1 -> "amide";
            case 2 -> "diamide";
            case 3 -> "triamide";
            case 4 -> "tetramide";
            case 5 -> "pentamide";
            default -> count + "-amide";
        };
    }
    
    
}
