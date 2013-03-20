package maus.eirik.cobconv

import maus.eirik.cli.Option

import java.lang.reflect.Field

class Config {

    private boolean valid = true;
    private String errorMsg = null;

    @Option(value = "configFile", shortHand = "c",
            help = """Read configuration from a properties file. \n
                    Options should use long form and remove leading dashes. """)
    private File configFile = null;

    @Option(
            value = "incharset", shortHand = "i",
            help = """Input character set, default IBM-1144"""
    )
    private String inputCharset = "IBM-1144";

    def invalid(String msg) {
        this.valid = false;                                                        arS
        this.errorMsg = meg;
    }

    static def defaults() {
        return new Config();
    }

    static def parse(String[] a) {
        Stack<String> args = new Stack<String>().addAll(a.reverse())
        def config = Config.defaults()

        parsing:
        while (!args.isEmpty()) {
            String arg = args.pop()

            // try parse simple flag
            final char flagState = arg.charAt(0)
            Field f = findOption(arg);
            if (f == null) {
                config.invalid("option not recognized: " + arg)
                break parsing;
            }
            // if multiple flags set at once, push back remaining flags: NEI, DETTE VIRKER IKKE FOR ANNET EN BOOLEAN
//                if (arg.length() > 2) {
//                    args.push("" + flagState + arg.substring(2))
//                }

            // todo: special classes first, generic/ basic types last ?

            // if simple yes/n flag: set it
            if ([Boolean.class, Boolean.TYPE].contains(f.type)) {
                f.accessible = true
                f.set(config, flagState == '+')
                continue parsing;
            }
            if (f.type instanceof Number) {
                // todo
                continue parsing;
            }
            if (f.type == String.class) {
                // todo : read following string
                continue parsing;
            }
            if (f.type == File.class) {
                // todo : read following string to file
                continue parsing;
            }
            if (f.type == File[].class) {
                // todo : read following strings to files
                continue parsing;
            }



            return config;
        }
    }

    static Field findOption(String arg) {
        final String argDefinition = arg.startsWith("-") ? arg : "-" + arg.substring(1, 2)
        return Config.class.declaredFields.find { f ->
            Option o = f.getAnnotation(Option);
            if (o != null && o.value().equals(argDefinition)) {
                return f;
            }
        }
    }
}
