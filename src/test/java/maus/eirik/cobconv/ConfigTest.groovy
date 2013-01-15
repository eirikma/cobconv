package maus.eirik.cobconv

import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: Eirik
 * Date: 15.01.13
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
class ConfigTest {

    @Test
    def void defaultConfigShouldContainDefaultSettings() {
        assertThat Config.defaults().getConfigFile(), isNull();
        // todo: more asserts
    }

    @Test
    def void helpOptionShouldPrintSomeInfo() {
        Config.
    }
}
