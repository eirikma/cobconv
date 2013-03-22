package maus.eirik.cli ;

import groovyjarjarcommonscli.MissingOptionException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Test class for options
 */
public class OptionsTest {

    @Test
    public void shouldSetSimpleOption() {
        class MyConfig {
            @Option(value = "define", shortHand = "d", help = "set a random flag")
            private boolean flag = false;

            @Option(value = "other", shortHand = "o", help = "set a other flag", optional = true)
            private boolean other = false;
        }

        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-d"});
        assertThat(config.flag, is(true));
    }
    @Test
    public void shouldSetSimpleOptionUsingLongForm() {
        class MyConfig {
            @Option(value = "define", shortHand = "d", help = "set a random flag")
            private boolean flag = false;

            @Option(value = "other", shortHand = "o", help = "set a other flag", optional = true)
            private boolean other = false;
        }

        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"--define"});
        assertThat(config.flag, is(true));
    }

    @Test (expected = MissingOptionException.class)
    public void shouldRejectIfMandatoyOptionSNotSet() {
        class MyConfig {
            @Option(value = "define", shortHand = "d", help = "set a random flag")
            private boolean flag = false;
            @Option(value = "other", shortHand = "o", help = "set a other flag", optional = true)
            private boolean other = false;
        }

        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-o"});
        fail("should have thrown on missing mandatory argument '-d'");
    }

    @Test
    public void shouldSetNumericOption() {
        class MyConfig {
            @Option(value = "num", shortHand = "n", help = "set a numeric parameter")
            private int number = 2;

            @Option(value = "other", shortHand = "o", help = "set a other flag", optional = true)
            private boolean other = false;
        }

        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-n3"});
        assertThat(config.number, is(3));
        class MyConfig2 {
            @Option(value = "num", shortHand = "n", help = "set a numeric parameter")
            private Integer number = 2;

            @Option(value = "other", shortHand = "o", help = "set a other flag", optional = true)
            private boolean other = false;
        }

        MyConfig2 config2 = OptionUtil.parseParameters(new MyConfig2(), new String[]{"-n 3"});
        assertThat(config2.number, is(3));
    }

    @Test
    public void shouldSetStringOption() {
        class MyConfig {
            @Option(value = "parameter", shortHand = "p", help = "set some parameter")
            private String parameter = "default_value";
        }
        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-p", "NonDefault"});
        assertThat(config.parameter, is("NonDefault"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSetStringOptionMoreThanOnce() {
        class MyConfig {
            @Option(value = "parameter", shortHand = "p", help = "set some parameter")
            private String parameter = "default_value";
        }
        MyConfig config = OptionUtil.parseParameters(new MyConfig(),
                new String[]{"-p", "NonDefault", "-p", "somethingElse"});
        assertThat(config.parameter, is("NonDefault"));
    }

    @Test
    public void shouldSetStringArrayOption() {
        class MyConfig {
            @Option(value = "parameter", shortHand = "p", help = "set some parameter")
            private String[] parameters = {"default_value"};
        }
        MyConfig config = OptionUtil.parseParameters(new MyConfig(),
                new String[]{"-p", "NonDefault", "-p", "somethingElse"});
        assertThat(config.parameters[0], is("NonDefault"));
    }
    @Test
    public void shouldSetAllowToSetOptionMoreThanOnceIfHeldByArray() {
        class MyConfig {
            @Option(value = "parameter", shortHand = "p", help = "set some parameter")
            private String[] parameters = null;
        }
        MyConfig config = OptionUtil.parseParameters(new MyConfig(),
                new String[]{"-p", "NonDefault", "-p", "somethingElse"});
        assertThat(Arrays.asList(config.parameters), equalTo(Arrays.asList("NonDefault", "somethingElse")));
    }

    @Test
    public void shouldSetOptionWithParameterValueImmediatlyAfterFlag() {
        class MyConfig {
            @Option(value = "parameter", shortHand = "p", help = "set some parameter")
            private String parameter = "default_value";
        }

        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-pNonDefault"});
        assertThat(config.parameter, is("NonDefault"));
    }

    @Test
    public void shouldSetFileOption() throws IOException {
        class MyConfig {
            @Option(value = "file", shortHand = "f", help = "set some file parameter")
            private File someFile = null;
        }

        File tempFile = File.createTempFile(getClass().getSimpleName() + ".shouldSetFileOption", ".tmp");
        tempFile.deleteOnExit();
        MyConfig config = OptionUtil.parseParameters(new MyConfig(), new String[]{"-f", tempFile.getAbsolutePath()});
        assertThat(config.someFile, is(tempFile));
    }

    @Test
    public void shouldAllowASetOfFilesToBeLastArguments() {
        fail("not implemented");
    }
}
