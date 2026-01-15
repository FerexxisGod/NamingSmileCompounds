package net.iupac;

import net.iupac.info_and_controle.Locants;

class RootWord{
    public String rootWord = "";

    Locants locants = new Locants(Input.Molecule);
    
    RootWord(){
        rootWordDeterminer();
    }
    private void rootWordDeterminer(){
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
            rootWord = units + tens;
        }
        else
            switch(chainLength){
                case 1: rootWord = "meth"; break;
                case 2: rootWord = "eth"; break;
                case 3: rootWord = "prop"; break;
                case 4: rootWord = "but"; break;
                case 5: rootWord = "pent"; break;
                case 6: rootWord = "hex"; break;
                case 7: rootWord = "hept"; break;
                case 8: rootWord = "oct"; break;
                case 9: rootWord = "non"; break;
                case 10: rootWord = "dec"; break;
                case 11: rootWord = "undec"; break;
                case 12: rootWord = "dodec"; break;
                case 13: rootWord = "tridec"; break;
                case 14: rootWord = "tetradec"; break;
                case 15: rootWord = "pentadec"; break;
                case 16: rootWord = "hexadec"; break;
                case 17: rootWord = "heptadec"; break;
                case 18: rootWord = "octadec"; break;
                case 19: rootWord = "nonadec"; break;
                case 20: rootWord = "icos"; break;
                default: rootWord = ""; break;
            }

    }
} 