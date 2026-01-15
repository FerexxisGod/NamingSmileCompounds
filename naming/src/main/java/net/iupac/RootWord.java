package net.iupac;

import net.iupac.info_and_controle.Locants;

class RootWord{
    Locants locants = new Locants(Input.Molecule);


    public String rootWord = rootWordDeterminer();
    
        
    private String rootWordDeterminer(){
        int chainLength = locants.LongestChain.size();
        if(chainLength > 20&& chainLength <= 100){
            String tens = switch(chainLength / 10){
                case 2: yield "eicos";
                case 3: yield "triacont";
                case 4: yield "tetracont";
                case 5: yield "pentacont";
                case 6: yield "hexacont";
                case 7: yield "heptacont";
                case 8: yield "octacont";
                case 9: yield "nonacont";
                case 10: yield "hect";
                default: yield "0";
            };
            String units = switch(chainLength % 10){
                case 1: yield "hen";
                case 2: yield "do";
                case 3: yield "tri";
                case 4: yield "tetra";
                case 5: yield "penta";
                case 6: yield "hexa";
                case 7: yield "hepta";
                case 8: yield "octa";
                case 9: yield "nona";
                default: yield "";
            };
            return units + tens;
        }
        else
            switch(chainLength){
                case 1: return "meth"; 
                case 2: return "eth"; 
                case 3: return "prop"; 
                case 4: return "but"; 
                case 5: return "pent"; 
                case 6: return "hex"; 
                case 7: return "hept"; 
                case 8: return "oct"; 
                case 9: return "non"; 
                case 10: return "dec"; 
                case 11: return "undec"; 
                case 12: return "dodec"; 
                case 13: return "tridec"; 
                case 14: return "tetradec"; 
                case 15: return "pentadec"; 
                case 16: return "hexadec"; 
                case 17: return "heptadec"; 
                case 18: return "octadec"; 
                case 19: return "nonadec"; 
                case 20: return "icos"; 
                default: return ""; 
            }

    }
} 