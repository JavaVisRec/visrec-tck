package jsr381.tck;

import org.testng.annotations.Test;

import javax.tools.Tool;

/**
 * @author Kevin Berendsen
 */
public class TestRunner {

    @Test
    public void run() {
        final Tool runner = new TCKRunner();
        runner.run(System.in, System.out, System.err, new String[0]);
    }
}
