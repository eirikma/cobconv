package maus.eirik.cli ;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test class for options
 */
public class OptionsTest {

    @Test
    public void shouldSetSimpleOption() {
        class MyConfig {
            @Option(value = "define", shortHand = "d", help = "set a random flag")
            private boolean flag = false;
        }

        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-d"});
        assertThat(config.flag, is(true));
    }
}
