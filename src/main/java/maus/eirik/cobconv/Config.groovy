package maus.eirik.cobconv

import lombok.Data

/**
 * Created with IntelliJ IDEA.
 * User: Eirik
 * Date: 14.11.12
 * Time: 22:44
 */
@Data
class Config {

    @Option(value = "-c",
            help = """Read configuration from a properties file. \n
                    Options should use long form and remove leading dashes. """)
    private File configFile = null;

    @Option (
        value = "-i",
        help = """Input character set, default IBM-1144"""
    )
    private String inputCharset = "IBM-1144";


    static def defaults() {
        return new Config();
    }

    static def parse(String[] args) {
        def config = Config.defaults()
        Config.class.fields.each {  f -> f.annotations.each { a-> if (a.annotationType().equals(Option.class)) {
            f.set(config, convertTo( føkk her må vi kunne lese neste i lista ved behov ...f.getAnnotation(Option.class)
        }}}
    }
}
