
# Organic Compound Namer

This Java-based project allows users to **compute the IUPAC name of organic compounds** based on their **molecular formula** or **structural descriptors** (e.g., SMILES, InChI, condensed formula). It utilizes cheminformatics libraries like **CDK (Chemistry Development Kit)** to parse, analyze, and name organic molecules.

---

## 🚀 Features

- Parse organic molecules from:
  - Molecular formula (e.g., `C2H6O`)
  - SMILES (e.g., `CCO`)
  - Condensed structural formulas
- Identify parent chains and functional groups
- Generate IUPAC-style names (basic support)
- Designed to be extendable for more advanced naming rules

---

## 🧰 Technologies Used

- **Java 8+**
- **CDK (Chemistry Development Kit)** – core cheminformatics
- **Maven** – build and dependency management
- (Optional) **JUnit** – unit testing

---

## 🗂 Project Structure

```
src/
 ├── main/
 │    └── java/
 │         └── org/
 │              └── anujchem/
 │                   ├── parser/
 │                   ├── model/
 │                   └── naming/
 └── test/
      └── java/
           └── ...
```

- `parser/` handles input parsing (e.g., SMILES, formulas)
- `model/` holds molecular data models
- `naming/` contains the logic for IUPAC name generation

---

## 🔧 Installation

### Clone the Repo
```bash
git clone https://github.com/yourusername/organic-namer.git
cd organic-namer
```

### Build with Maven
```bash
mvn clean install
```

### Run the Program
```bash
java -jar target/naming-1.0.0.jar
```

---

## 💡 Usage Example

```java
MolecularNamer namer = new MolecularNamer();
String name = namer.nameFromSmiles("CC(Cl)C");
System.out.println(name); // e.g., 2-chloropropane
```

---

## 📚 References

- [CDK Documentation](https://cdk.github.io)
- [IUPAC Naming Rules](https://iupac.org/what-we-do/nomenclature/)
- [SMILES Notation](https://www.daylight.com/dayhtml/doc/theory/theory.smiles.html)

---

## 📌 TODO

- [ ] Add InChI support
- [ ] Handle stereochemistry
- [ ] Improve naming accuracy for branched chains and rings

---

## 🧪 Tests

Run tests with:
```bash
mvn test
```

---

## 👤 Author

**Anuj Anand**  
Feel free to contribute or raise issues!

---

## 📄 License

MIT License – use it freely, just give credit.
