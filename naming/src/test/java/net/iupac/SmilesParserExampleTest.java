package net.iupac;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SmilesParserExampleTest {

    @Test
    public void testAtomCount() throws Exception {
        String smiles = "CC(Cl)C"; // 2-chloropropane
        int atomCount = SmilesParserExample.getAtomCount(smiles);
        assertEquals(4, atomCount); // C, C, Cl, C (hydrogens are implicit)
    }

    @Test
    public void testMethane() throws Exception {
        String smiles = "C"; // methane
        int atomCount = SmilesParserExample.getAtomCount(smiles);
        assertEquals(1, atomCount); // Only carbon atom
    }
    @Test
    public void testEvenOdd(){
        assertEquals(true, SmilesParserExample.isEven(2));
    }
}
