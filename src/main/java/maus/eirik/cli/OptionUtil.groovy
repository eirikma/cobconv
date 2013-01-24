package maus.eirik.cli

import groovyjarjarcommonscli.GnuParser
import groovyjarjarcommonscli.Options


/**
 * Helper class to parse a command line into a populated settings object
 *
 */
class OptionUtil {
    public static <T> T parseParameters(T config, String[] args) {
        Options cliOptions = readAnnotatedOptionFields(config)
        GnuParser parser = new GnuParser();
        parser.parse(cliOptions, args);
        if (cliOptions.)
    }

    static def <T> Options readAnnotatedOptionFields(T configObject) {
        Options o = new Options()
        Class<T> type = configObject.class
        collectOptionsFrom(type, o);
        return o;
    }

    static def <T> void collectOptionsFrom(Class<T> tClass, Options options) {
        if (!tClass.superclass.equals(Object.class)) {
            collectOptionsFrom(tClass.superclass, options)
        }
        tClass.declaredFields.each { f ->
            Option o = f.getAnnotation(Option.class);
            if (o != null) {
                options.addOption(new groovyjarjarcommonscli.Option(
                        o.shortHand(),
                        o.value(),
                        ![Boolean.TYPE, Boolean.class].contains(f.type),
                        o.help()
                ));
            }
        }
    }
}
