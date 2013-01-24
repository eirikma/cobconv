package maus.eirik.cobconv

import org.junit.Test

import static junit.framework.Assert.assertEquals

/**
 */
class ConfigTest {

    @Test
    def void defaultConfigShouldContainDefaultSettings() {
        //assertThat Config.defaults().getConfigFile(), isNull();
        // todo: more asserts
    }

    @Test
    def void findOptionShouldFindFieldForBothSettingAndUnsetting() {
        assertEquals(Config.class.getDeclaredField("inputCharset"), Config.findOption("-i"))
    }

    @Test
    def void checkListToStackConversion() {
        Stack<String> s = new Stack<String>();
        s.addAll(["a", "b", "c"].reverse());
        assertEquals("a", s.pop());
    }
    @Test
    def void helpOptionShouldPrintSomeInfo() {
    }
}
