package maus.eirik.cobconv

class Cli {
    def  readFromProperties() {

    }
    public static void main(String[] args) {
        new Cli(Config.parse(args)).run(System.out);
    }
}
