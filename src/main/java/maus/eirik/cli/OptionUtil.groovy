package maus.eirik.cli

import groovyjarjarcommonscli.CommandLine
import groovyjarjarcommonscli.GnuParser
import groovyjarjarcommonscli.Options

import java.lang.reflect.Field


/**
 * Helper class to parse a command line into a populated settings object
 *
 */
class OptionUtil {
    public static <T> T parseParameters(T config, String[] args) {
        Options cliOptions = readAnnotatedOptionFields(config)

        // todo: look at config.class for annotation deciding which parser to use
        GnuParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(cliOptions, args);

        // todo: validate and print usage if not okay.

        commandLine.options.each { o ->
             if (o.isRequired() && o. takesParameter(o.field) && o.valuesList.isEmpty()) {
                 printUsage(config);
                 throw new IllegalArgumentException("illegal arguent");
             }
        }

        // todo put values into config
        commandLine.options.each { o ->
            o.field.accessible true
            o.field.set(config, toFieldType(o.field, o.values));
        }
        return config;
    }

    private static <T> T toFieldType(Field f, List<String> values) {
        switch (f.type) {
            case Boolean.class:
            case Boolean.TYPE:
                return values[0].startsWith("+") ? false : true;
            case Integer.class:
            case Integer.TYPE:
                return Integer.valueOf(values[0]);
        }
        return null;
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
            final Option o = f.getAnnotation(Option.class);
            if (o != null) {
                options.addOption(createCommonsCliOption(o, f));
            }
        }
    }


    private static groovyjarjarcommonscli.Option createCommonsCliOption(Option o, Field f) {
        return new CommonsCliOption(o, f);
    }

    static class CommonsCliOption extends groovyjarjarcommonscli.Option {
        Option o;
        Field f;

        CommonsCliOption(Option o, Field f) {
            super(o.shortHand(), o.value(), takesParameter(f), o.help());
            this.o = o;
            this.f = f;
            setRequired(!o.optional());
            if (!f.type.isArray()) {
                setArgs(takesParameter(f) ? 1 : 0)
            }
        }

        def annotation = o;
        def field = f;
    }
    static boolean takesParameter(Field f) {
        return ![Boolean.TYPE, Boolean.class].contains(f.type);
    }
}
