package jsr381.tck;

import jsr381.tck.tests.BinaryClassificationTest;
import jsr381.tck.tests.ImageClassificationTest;
import jsr381.tck.tests.ImageFactoryTest;
import jsr381.tck.tests.spi.ClassifierServiceTest;
import jsr381.tck.tests.spi.ImageFactoryServiceTest;
import jsr381.tck.tests.spi.ImplementationServiceTest;
import jsr381.tck.tests.spi.ServiceProviderTest;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.reporters.VerboseReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import javax.lang.model.SourceVersion;
import javax.tools.Tool;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kevin Berendsen
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class TCKRunner extends XmlSuite implements Tool {

    private static final String JSR_ID = "381";
    private static final String JSR_NAME = "Visual Recognition";
    private static final String JSR_VERSION = "1.0";

    /**
     * Constructor.
     */
    public TCKRunner() {
        setName("JSR" + JSR_ID + "-TCK, version " + JSR_VERSION);
        XmlTest test = new XmlTest(this);
        test.setName("TCK/Test Setup");
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass(ServiceProviderTest.class));
        classes.add(new XmlClass(ClassifierServiceTest.class));
        classes.add(new XmlClass(ImageFactoryServiceTest.class));
        classes.add(new XmlClass(ImageFactoryTest.class));
        classes.add(new XmlClass(ImplementationServiceTest.class));
        classes.add(new XmlClass(ImageClassificationTest.class));
        classes.add(new XmlClass(BinaryClassificationTest.class));
        test.setXmlClasses(classes);
    }

    /**
     * Main method to start the TCK. Optional arguments are:
     * <ul>
     *     <li>-DoutputDir for defining the output directory TestNG uses (default: ./target/tck-output).</li>
     *     <li>-Dverbose=true to enable TestNG verbose mode.</li>
     *     <li>-DreportFile=targetFile.txt for defining the TCK result summary report target file
     *     (default: ./target/tck-results.txt).</li>
     * </ul>
     *
     * @param args arguments
     */
    @Override
    public int run(InputStream in, OutputStream out, OutputStream err,
                   String... args) {
        System.out.println("-- JSR " + JSR_ID + " TCK started --");
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(new TCKRunner());
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        String outDir = System.getProperty("outputDir");
        if (outDir != null) {
            tng.setOutputDirectory(outDir);
        } else {
            tng.setOutputDirectory("./target/tck-output");
        }
        String verbose = System.getProperty("verbose");
//        if("true".equalsIgnoreCase(verbose)){
        tng.addListener(new VerboseReporter());
//        }
        String reportFile = System.getProperty("reportFile");
        File file = null;
        if (reportFile != null) {
            file = new File(reportFile);
        } else {
            file = new File("./target/tck-results.txt");
        }
        TCKReporter rep = new TCKReporter(file);
        System.out.println("Writing to file " + file.getAbsolutePath() + " ...");
        tng.addListener(rep);
        tng.run();
        rep.writeSummary();
        System.out.println("-- JSR " + JSR_ID + " TCK finished --");
        return 0;
    }

    @Override
    public final Set<SourceVersion> getSourceVersions() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                new SourceVersion[]{SourceVersion.RELEASE_8})));
    }

    public static void main(String... args) {
        TCKRunner runner = new TCKRunner();
        runner.run(System.in, System.out, System.err, args);
    }

    /**
     * Reporter implementation.
     */
    public static final class TCKReporter extends TestListenerAdapter {
        private int count = 0;
        private int skipped = 0;
        private int failed = 0;
        private int success = 0;

        private StringWriter internalBuffer = new StringWriter(3000);
        private FileWriter w;

        /**
         * Constructor of the TCK reporter, writing to the given file.
         *
         * @param file the target file, not null.
         */
        @SuppressWarnings("CallToPrintStackTrace")
        public TCKReporter(File file) {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                w = new FileWriter(file);
                w.write("********************************************************************************************\n");
                w.write("**** JSR " + JSR_ID + " - " + JSR_NAME + ", Technical Compatibility Kit, version " + JSR_VERSION + "\n");
                w.write("********************************************************************************************\n\n");
                w.write("Executed on " + new java.util.Date() + "\n\n");

                // System.out:
                internalBuffer.write("********************************************************************************\n");
                internalBuffer.write("**** JSR " + JSR_ID + " - " + JSR_NAME + ", Technical Compatibility Kit, version " + JSR_VERSION + "\n");
                internalBuffer.write("********************************************************************************\n\n");
                internalBuffer.write("Executed on " + new java.util.Date() + "\n\n");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        @Override
        public void onTestFailure(ITestResult tr) {
            failed++;
            count++;
            String location = tr.getTestClass().getRealClass().getSimpleName() + '#' + tr.getMethod().getMethodName();
            try {
                Method realTestMethod = tr.getMethod().getMethod();
                Test testAnnot = realTestMethod.getAnnotation(Test.class);
                if (!testAnnot.description().isEmpty()) {
                    if (tr.getThrowable() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter w = new PrintWriter(sw);
                        tr.getThrowable().printStackTrace(w);
                        w.flush();
                        log("[FAILED]  " + testAnnot.description() + "(" + location + "):\n" + sw.toString());
                    } else {
                        log("[FAILED]  " + testAnnot.description() + "(" + location + ")");
                    }
                } else {

                    if (tr.getThrowable() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter w = new PrintWriter(sw);
                        tr.getThrowable().printStackTrace(w);
                        w.flush();
                        log("[FAILED]  " + location + ":\n" + sw.toString());
                    } else {
                        log("[FAILED]  " + location);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }

        @Override
        public void onTestSkipped(ITestResult tr) {
            skipped++;
            count++;
            String location = tr.getTestClass().getRealClass().getSimpleName() + '#' + tr.getMethod().getMethodName();
            try {
                Method realTestMethod = tr.getMethod().getMethod();
                Test specAssert = realTestMethod.getAnnotation(Test.class);
                if (specAssert != null && !specAssert.description().isEmpty()) {
                    log("[SKIPPED] " + specAssert.description() + "(" + location + ")");
                } else {
                    log("[SKIPPED] " + location);
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }

        @Override
        public void onTestSuccess(ITestResult tr) {
            success++;
            count++;
            String location = tr.getTestClass().getRealClass().getSimpleName() + '#' + tr.getMethod().getMethodName();
            try {
                Method realTestMethod = tr.getMethod().getMethod();
                Test specAssert = realTestMethod.getAnnotation(Test.class);
                if (specAssert != null && !specAssert.description().isEmpty()) {
                    log("[SUCCESS] " + specAssert.description() + "(" + location + ")");
                } else {
                    log("[SUCCESS] " + location);
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }

        private void log(String text) throws IOException {
            w.write(text);
            w.write('\n');
            internalBuffer.write(text);
            internalBuffer.write('\n');
        }

        public void writeSummary() {
            try {
                log("\nJSR " + JSR_ID + " TCK, version " + JSR_VERSION + " Summary");
                log("------------------------------------------");
                log("\nTOTAL TESTS EXECUTED : " + count);
                log("TOTAL TESTS SKIPPED  : " + skipped);
                log("TOTAL TESTS SUCCESS  : " + success);
                log("TOTAL TESTS FAILED   : " + failed);
                w.flush();
                w.close();
                internalBuffer.flush();
                System.out.println();
                System.out.println(internalBuffer);
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }
    }
}
