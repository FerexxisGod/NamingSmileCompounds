package net.iupac;

public class FinalBuilder {
    Suffix suffix = new Suffix();
    RootWord rootWord = new RootWord();

    String finalName = rootWord.rootWord + suffix.getSuffix();
}
