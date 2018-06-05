package com.nidjo123.treasury.reports;

import com.nidjo123.treasury.util.Config;
import de.nixosoft.jlr.JLRConverter;
import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

abstract public class TreasuryReport implements Report {
    protected String templateName;
    protected String outputName;

    public TreasuryReport() {

    }

    public TreasuryReport(String templateName, String outputName) {
        this.templateName = templateName;
        this.outputName = outputName;
    }

    abstract protected void fillData(JLRConverter converter);

    public void generate() throws ReportException {
        try {
            Config config = Config.getInstance();

            Path workingDir = Paths.get(config.get("workingDir"));
            Path tmpDir = Files.createTempDirectory(workingDir, "tmp");
            Path report = tmpDir.resolve(outputName);

            Path template = workingDir.resolve(templateName);

            JLRConverter converter = new JLRConverter(workingDir.toFile());

            // child class knows how to fill in the data
            fillData(converter);

            converter.parse(template.toFile(), report.toFile());

            Path dest = Paths.get(config.get("pdfDestination"));

            JLRGenerator generator = new JLRGenerator();
            generator.deleteTempFiles(true, true, true);

            Path pdfLatex = Paths.get(config.get("pdflatex"));

            generator.generate(pdfLatex.toFile(), report.toFile(), dest.toFile(), tmpDir.toFile());

            //Files.deleteIfExists(tmpDir);

            JLROpener.open(generator.getPDF());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
