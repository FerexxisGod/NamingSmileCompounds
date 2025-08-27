package net.iupac;

import org.junit.jupiter.api.Test;
import org.openscience.cdk.interfaces.IAtomContainer;

import net.iupac.info_and_controle.Locants;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SmilesParserExampleTest {


    @Test
    public void testSmilesParsing() {
        String smiles = "CCCC";
        IAtomContainer molecule = Input.input(smiles);
        Locants locants = new Locants(molecule);
        assertEquals(2, locants.Terminal.size(), "Number of terminal atoms should be 2");
        assertEquals(4, locants.LongestChain.size(), "Longest chain length should be 4");
        assertEquals(0, locants.BranchCount, "Number of branches should be 0");
    }
    @Test
    public void testLocants1() {
        String smiles = "CC(C)C";
        IAtomContainer molecule = Input.input(smiles);
        Locants locants = new Locants(molecule);
        assertEquals(3, locants.Terminal.size(), "Number of terminal atoms should be 3");
        assertEquals(3, locants.LongestChain.size(), "Longest chain length should be 3");
        assertEquals(1, locants.BranchCount, "Number of branches should be 1");
    }
    @Test
    public void testLocants2() {
        String smiles = "CC(C)(C)C";
        IAtomContainer molecule = Input.input(smiles);
        Locants locants = new Locants(molecule);
        assertEquals(4, locants.Terminal.size(), "Number of terminal atoms should be 4");
        assertEquals(3, locants.LongestChain.size(), "Longest chain length should be 3");
        assertEquals(2, locants.BranchCount, "Number of branches should be 2");
    }
    @Test
    public void testLocants3() {
        String smiles = "CC(O)CC";
        IAtomContainer molecule = Input.input(smiles);
        Locants locants = new Locants(molecule);
        assertEquals(2, locants.Terminal.size(), "Number of terminal atoms should be 2");
        assertEquals(4, locants.LongestChain.size(), "Longest chain length should be 4");
        assertEquals(1, locants.BranchCount, "Number of branches should be 1");
    }
    /*@Test
    public void testLocatns4() {
        String smiles = "C1CCCCC1";
        IAtomContainer molecule = Input.input(smiles);
        Locants locants = new Locants(molecule);
        assertEquals(0, locants.Terminal.size(), "Number of terminal atoms should be 2");
        assertEquals(4, locants.LongestChain.size(), "Longest chain length should be 4");
        assertEquals(0, locants.BranchCount, "Number of branches should be 0");
    }*/
}
