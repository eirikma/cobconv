package maus.eirik.cli

import groovyjarjarcommonscli.CommandLine
import groovyjarjarcommonscli.GnuParser
import groovyjarjarcommonscli.GroovyPosixParser
import groovyjarjarcommonscli.Options
import groovyjarjarcommonscli.Parser
import groovyjarjarcommonscli.PosixParser

import java.lang.reflect.Field


/**
 * Helper class to parse a command line into a populated settings object
 *
 */
class OptionUtil {
    public static <T> T parseParameters(T config, String[] args) {
        Options cliOptions = readAnnotatedOptionFields(config)

        // todo: look at config.class for annotation deciding which parser to use
        Parser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(cliOptions, args);

        // todo: validate and print usage if not okay.

        commandLine.options.each { o ->
            CommonsCliOption cco = o;
             if (isValidOption(cco)) {
                 printUsage(config);
                 throw new IllegalArgumentException("illegal arguent");
             }
        }


        commandLine.options.each { o ->
            CommonsCliOption cco = ((CommonsCliOption)o);
            cco.field.setAccessible(true);
            cco.field.set(config, toFieldType(cco.field, cco.field.get(config),  cco));
        }
        return config;
    }

    private static boolean isValidOption(CommonsCliOption cco) {
        return cco.isRequired() &&
                cco.takesParameter(cco.field) &&
                cco.valuesList.isEmpty()
    }

    private static <T> T toFieldType(Field f, Object currentValue, CommonsCliOption option) {
        switch (f.type) {
            case String.class:
                return option.value;
                break;
            case Boolean.class:
            case Boolean.TYPE:
                return currentValue == false ? true : currentValue == true ? false : currentValue == null ? true : true;
            case Integer.class:
            case Integer.TYPE:
                return  toInteger(currentValue, option.getValue())
            case File.class:
                 return option.value == null ? currentValue : new File(option.value);

            case File[].class:

            default:
                return null;
        }
        return null;
    }

    private static Integer toInteger(Object currentValue, String value) {
        return value == null ? currentValue : value == null ? null :  Integer.parseInt(value.trim());
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
            super(o.shortHand(), o.value(), OptionUtil.takesParameter(f), o.help());
            this.o = o;
            this.f = f;
            setRequired(!o.optional());
            if (!f.type.isArray()) {
                setArgs(OptionUtil.takesParameter(f) ? 1 : 0)
            }
        }

        def annotation = o;
        def field = f;
    }

    static boolean takesParameter(Field f) {
        return ![Boolean.TYPE, Boolean.class].contains(f.type);
    }
}
